package me.cjds.siemensmeetings;

import android.app.Activity;
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
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
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


public class MeetingActivity extends Activity implements AsyncResponse,View.OnTouchListener {

    Meeting meeting;
    DataHolder dataholder;
    boolean resetCheckThread=false;
    Handler handler;
    Runnable r;
    Handler handler2;
    Runnable r2;
    boolean runningCheckThread=true;
    long timeBetweenRuns=60*1000;
    long switchTime=1000 *60 ;

    SharedPreferences settings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting);
        Intent i = getIntent();
        meeting = (Meeting) i.getParcelableExtra("meeting");
        dataholder=DataHolder.getInstance();
        settings= PreferenceManager.getDefaultSharedPreferences(this);
        timeBetweenRuns=Long.parseLong(settings.getString("timeBetweenRuns", "1"))*60*1000;
        switchTime=timeBetweenRuns;
        ImageButton settings= (ImageButton)findViewById(R.id.settings_button_meeting);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(MeetingActivity.this,SettingsActivity.class);
                i.putExtra("check",false);
                startActivity(i);
            }
        });
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Button calendar_button=(Button) findViewById(R.id.calendar_button);
        calendar_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MeetingActivity.this.finish();
            }
        });
        setUpMeeting(meeting);

        handler=new Handler();
        r=new Runnable() {
            public void run() {
                if(runningCheckThread){
                    //check if meeting is nearly over
                        new DataFetchHandler(MeetingActivity.this).execute();
                        handler.postDelayed(r, timeBetweenRuns);
                }
                else{
                    if(resetCheckThread){
                        resetCheckThread=false;
                        runningCheckThread=true;
                    }
                }
            }
        };
        handler.postDelayed(r, timeBetweenRuns);

        handler2=new Handler();
        r2=new Runnable() {
            @Override
            public void run() {
                TextView tv= (TextView) findViewById(R.id.time_meeting);
                DateTime dt=DateTime.now();
                DateTimeFormatter dtf=DateTimeFormat.forPattern("hh:mm a");
                tv.setText(dt.toString(dtf));
                handler2.postDelayed(r2,60*1000);
            }
        };
        handler2.postDelayed(r2,1000);
    }

    @Override
    public boolean onTouch(View v, MotionEvent e){
        runningCheckThread=false;
        resetCheckThread=true;
        return true;
    }


    public void setUpMeeting(Meeting meeting){
        this.meeting=meeting;
        TextView tv=(TextView) findViewById(R.id.meeting_name);
        tv.setText(meeting.meeting_name);

        TextView tv2=(TextView) findViewById(R.id.meeting_time);
        DateTime start=meeting.meeting_time.getStart();
        DateTime end=meeting.meeting_time.getEnd();
        DateTimeFormatter formatter = DateTimeFormat.forPattern("hh:mm a");
        DateTimeFormatter formatter2=DateTimeFormat.forPattern("dd MMM, YYYY");
        Button b=(Button)findViewById(R.id.nextMeetingButton);
        b.setVisibility(View.INVISIBLE);
        /* @TODO check if meeting is not on same day */
        tv2.setText(start.toString(formatter)+" - "+ end.toString(formatter)+"  "+start.toString(formatter2));

        ListView list=(ListView) findViewById(R.id.people_list);

        PeopleListAdapter adapter=new PeopleListAdapter(this,meeting);

        list.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_meeting, menu);
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
            TextView tv= (TextView)findViewById(R.id.errortextmeeting);
            tv.setText(jsonData);
            tv.setVisibility(View.VISIBLE);
        }
        else{
            this.dataholder.arrangeData(jsonData);
            long currentTime=Calendar.getInstance().getTimeInMillis();
            int i=0;
            out:for( i=0;i<this.dataholder.getData().size();i++){
                Meeting currentMeeting=this.dataholder.getData().get(i);
                long startTime=currentMeeting.meeting_time.getStartMillis();


                //if startTime - currentTime  < 10 minutes startTime is greater than currentTime by less than 10 minutes
                //switch meetings
                //make sure its not the current meeting
                if(currentTime-startTime <switchTime && currentTime-startTime>0){
                    if(!meeting.equals(currentMeeting)){
                        setUpMeeting(this.dataholder.getData().get(i));
                        break out;
                    }
                }
                //if startTime - currentTime  < 10 minutes startTime is greater than currentTime by less than 10 minutes
                //show meeting as coming up
                // make sure its not the current meeting
                if(startTime-currentTime <switchTime && startTime-currentTime>0){
                    if(!meeting.equals(currentMeeting)){
                        Button b=(Button) findViewById(R.id.nextMeetingButton);
                        DateTimeFormatter formatter = DateTimeFormat.forPattern("hh:mm a");
                        String startString=currentMeeting.meeting_time.getStart().toString(formatter);
                        String endString=currentMeeting.meeting_time.getEnd().toString(formatter);
                        b.setText("Meeting Coming Up \n"+currentMeeting.meeting_name+"\n"+startString+"-"+ endString);
                        b.setVisibility(View.VISIBLE);
                    }
                }
            }

            //no meetings are coming up
            if(i==this.dataholder.getData().size()){
                long meeting_end=this.meeting.meeting_time.getEndMillis();
                //it is after the end of meeting
                if(currentTime- meeting_end>0){
                    this.finish();
                }
            }

        }
    }
}
