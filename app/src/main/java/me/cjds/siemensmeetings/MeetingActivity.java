package me.cjds.siemensmeetings;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.Interval;


public class MeetingActivity extends ActionBarActivity {

    Meeting meeting;
    Interval meeting_time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting);
        Intent i = getIntent();
        meeting = (Meeting) i.getParcelableExtra("meeting");

    }

    public void setUpMeeting(Meeting meeting){
        TextView tv=(TextView) findViewById(R.id.meeting_name);
        tv.setText(meeting.meeting_name);

        TextView tv2=(TextView) findViewById(R.id.meeting_time);
        DateTime start=meeting_time.getStart();
        DateTime end=meeting_time.getStart();
        /* @TODO check if meeting is not on same day */
        tv2.setText(start.getHourOfDay()+":"+start.getMinuteOfHour() +" - "+ end.getHourOfDay()+":"+end.getMinuteOfHour()+" "+start.getDayOfMonth()+" "+start.getMonthOfYear()+ " " +start.getYear());

        ListView list=(ListView) findViewById(R.id.people_list);

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,R.layout.person_list_item,R.id.attendeeNameTextView
        );
        adapter.add((meeting.attendees.get(0).first_name));
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
