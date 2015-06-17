package me.cjds.siemensmeetings;

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
import android.widget.GridView;
import android.widget.ListView;

import org.joda.time.DateTime;
import org.joda.time.Interval;

import java.util.ArrayList;
import java.util.Calendar;


public class MainActivity extends ActionBarActivity {

    GridView gv;
    Dialog dialog;
    ArrayList<ArrayList<Meeting>> meetings=new ArrayList<ArrayList<Meeting>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<Person> persons= new ArrayList<Person>();
        persons.add(new Person("Carl","Saldanha","CEO","Attending"));
        persons.add(new Person("Carl","Saldanha","CEO","Never gonna come"));
        for(int i=0;i<2;i++) {
            meetings.add(new ArrayList<Meeting>());
            for (int j = 0; j < 2; j++) {
                meetings.get(i).add(new Meeting("hello", new Interval(new DateTime(),new DateTime()),persons));
            }
        }
        gv=(GridView) findViewById(R.id.caldendarview);

        //set up dialog
        dialog=new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.setTitle("Meeting List");

        gv.setNumColumns(7);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.show();
                ListView l = (ListView) dialog.findViewById(R.id.dialog_meeting_list);
                l.setAdapter(new MeetingAdapter(dialog.getContext(), meetings.get(position)));
                l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(MainActivity.this, MeetingActivity.class);
                        intent.putExtra("meeting",meetings.get(0).get(position));
                        startActivity(intent);

                    }
                });

            }

        });
        gv.setAdapter(new GridAdapter(this, meetings, 2014, Calendar.AUGUST));


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
}
