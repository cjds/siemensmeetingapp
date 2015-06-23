package me.cjds.siemensmeetings;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;


public class MeetingActivity extends Activity {

    Meeting meeting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting);
        Intent i = getIntent();
        meeting = (Meeting) i.getParcelableExtra("meeting");

        ImageButton calendar_button=(ImageButton) findViewById(R.id.calendar_button);
        calendar_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MeetingActivity.this.finish();
            }
        });
        setUpMeeting(meeting);

    }

    public void setUpMeeting(Meeting meeting){
        TextView tv=(TextView) findViewById(R.id.meeting_name);
        tv.setText(meeting.meeting_name);

        TextView tv2=(TextView) findViewById(R.id.meeting_time);
        DateTime start=meeting.meeting_time.getStart();
        DateTime end=meeting.meeting_time.getStart();
        DateTimeFormatter formatter = DateTimeFormat.forPattern("hh:mm a");
        DateTimeFormatter formatter2=DateTimeFormat.forPattern("dd MMM, YYYY");
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
}
