package me.cjds.siemensmeetings;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


public class MainActivity extends Activity implements AsyncResponse{

    GridView gv;
    Dialog dialog;
    ArrayList<ArrayList<Meeting>> meetings=new ArrayList<ArrayList<Meeting>>();
    Calendar current;
    TextView month_year;
    int currentActiveDay=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gv=(GridView) findViewById(R.id.caldendarview);
        current = Calendar.getInstance();
        current.set(Calendar.DAY_OF_MONTH,1);
        DataFetchHandler dataFetchHandler=new DataFetchHandler(this);
        dataFetchHandler.execute();
        Button back = (Button)findViewById(R.id.calendar_back_button);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int year = current.get(Calendar.YEAR);
                int month = current.get(Calendar.MONTH);
                if (month - 1 > 0) {
                    current.set(year, month - 1, 1);
                } else {
                    current.set(year - 1, 11, 1);
                }
                populateView();
            }
        });
        Button next = (Button)findViewById(R.id.calendar_next_button);
        next.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                int year=current.get(Calendar.YEAR);
                int month=current.get(Calendar.MONTH);
                if(month+1<12){
                    current.set(year,month+1,1);
                }
                else{
                    current.set(year+1,1,1);
                }
                populateView();
            }
        });

        month_year= (TextView)findViewById(R.id.month_year_text);


        dialog=new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.setTitle("Meeting List");
        //set up dialog

        gv.setNumColumns(7);

    }

    public void populateView(){

        int year = current.get(Calendar.YEAR);
        int month = current.get(Calendar.MONTH);
        month_year.setText(current.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US)+" "+ year);
        gv.setAdapter(new GridAdapter(this, meetings, year, month));
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(position +1>=current.get(Calendar.DAY_OF_WEEK)) {
                    dialog.show();
                    //move position to the one pointing to the first day of the month
                    int pos2 = position +1 - current.get(Calendar.DAY_OF_WEEK) ;
                    ListView l = (ListView) dialog.findViewById(R.id.dialog_meeting_list);
                    l.setAdapter(new MeetingAdapter(dialog.getContext(), meetings.get(pos2)));
                    currentActiveDay = pos2;
                    l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            dialog.dismiss();
                            Intent intent = new Intent(MainActivity.this, MeetingActivity.class);
                            intent.putExtra("meeting", meetings.get(currentActiveDay).get(position));
                            startActivity(intent);

                        }
                    });
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
    public void doSomething(String s) {
        //THIS CONVERTS THE JSON INTO AN ARRAYLIST OF ARRAYLISTS
        try {
            ArrayList<Person> persons= new ArrayList<Person>();
            persons.add(new Person("Carl","Saldanha","CEO","Attending"));
            persons.add(new Person("Carl","Saldanha","CEO","Never gonna come"));
            JSONArray jsonArray=new JSONArray(s);
            for(int i=0;i<current.getActualMaximum(Calendar.DAY_OF_MONTH);i++){
                meetings.add(new ArrayList<Meeting>());
                for(int j=0;i<jsonArray.length();j++) {
                    JSONObject json=jsonArray.getJSONObject(j);
                    if(json.getString("hello")=="there")
                        meetings.get(i).add(new Meeting("hello", new Interval(new DateTime(),new DateTime()),persons));
                }
            }
            populateView();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
