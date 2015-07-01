package me.cjds.siemensmeetings;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;


public class AgendaActivity extends Activity implements AsyncResponse, OnTouchListener {
    ListView agenda_view;
    DataHolder dataholder;
    Calendar current;
    long timeBetweenRuns=1000;
    long switchTime=1*60*1000;
    Handler handler;
    Runnable r;
    Handler handler2;
    Runnable r2;

    boolean runningCheckThread=true;
    boolean resetCheckThread=false;
    SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);
        //agenda_list
        current=Calendar.getInstance();
        agenda_view= (ListView) findViewById(R.id.agenda_list);
        settings= PreferenceManager.getDefaultSharedPreferences(this);
        timeBetweenRuns=Long.parseLong(settings.getString("timeBetweenRuns","1"))*60*1000;
        switchTime=timeBetweenRuns;
        ImageButton settings= (ImageButton)findViewById(R.id.settings_button_agenda);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(AgendaActivity.this,SettingsActivity.class);
                i.putExtra("check",false);
                startActivity(i);
            }
        });
        dataholder=DataHolder.getInstance();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //run the check for data many times
        handler=new Handler();
         r=new Runnable() {
            public void run() {
                if(runningCheckThread){
                    //pass this which runs doSomething when this is finished
                    new DataFetchHandler(AgendaActivity.this).execute();
                    handler.postDelayed(r, timeBetweenRuns);
                }
                else{
                    if(resetCheckThread){
                        resetCheckThread=false;
                        runningCheckThread=true;
                        handler.postDelayed(r, timeBetweenRuns);
                    }

                }
            }
        };
        handler.postDelayed(r,1000);


        handler2=new Handler();
        r2=new Runnable() {
            @Override
            public void run() {
                TextView tv= (TextView) findViewById(R.id.time_agenda);
                DateTime dt=DateTime.now();
                DateTimeFormatter dtf= DateTimeFormat.forPattern("hh:mma");
                tv.setText(dt.toString(dtf));
                handler2.postDelayed(r2,60*1000);
            }
        };
        handler2.postDelayed(r2,1000);
    }

    public void populateView(){
        TextView tv= (TextView) findViewById(R.id.dateText);
        DateTimeFormatter dtf= DateTimeFormat.forPattern("EEEE, MMMMM dd yyyy");
        tv.setText(DateTime.now().toString(dtf));
        agenda_view.setAdapter(new AgendaAdapter(this, dataholder.getData()));
        agenda_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                runningCheckThread = false;
                Intent intent = new Intent(AgendaActivity.this, MeetingActivity.class);
                intent.putExtra("meeting", dataholder.getData().get(position));
                startActivity(intent);

            }
        });
    }


    @Override
    public void onResume(){
        resetCheckThread=true;
        super.onResume();
    }

    @Override
    public void onPause(){
        runningCheckThread=false;
        resetCheckThread=false;
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_agenda, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void doSomething(String jsonData) {
        //THIS CONVERTS THE JSON INTO AN ARRAYLIST OF ARRAYLISTS
        if(jsonData.startsWith("[ERROR]")){
            TextView tv= (TextView)findViewById(R.id.errortextagenda);
            tv.setText(jsonData);
            tv.setVisibility(View.VISIBLE);
        }
        else {
            this.dataholder.arrangeData(jsonData);

            populateView();
            out:
            for (int i = 0; i < this.dataholder.getData().size(); i++) {
                long meeting_start = this.dataholder.getData().get(i).meeting_time.getStart().getMillis();
                long current_time = Calendar.getInstance().getTimeInMillis();
                if (meeting_start - current_time < switchTime && meeting_start - current_time > 0) {
                    runningCheckThread = false;
                    Intent intent = new Intent(AgendaActivity.this, MeetingActivity.class);
                    intent.putExtra("meeting", this.dataholder.getData().get(i));
                    startActivity(intent);
                    break out;
                }
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        runningCheckThread=false;
        resetCheckThread=true;
        return true;
    }
}
