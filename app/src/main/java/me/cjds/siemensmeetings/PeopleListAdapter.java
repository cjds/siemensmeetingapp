package me.cjds.siemensmeetings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.joda.time.DateTime;

import java.util.ArrayList;

/**
 * Created by Carl Saldanha on 6/13/2015.
 */
public class PeopleListAdapter extends BaseAdapter {
    //
    Context mContext;
    ArrayList<Person> people;
    PeopleListAdapter(Context context,Meeting m){
        this.people=m.attendees;
        mContext=context;
    }

    @Override
    public int getCount() {
        return people.size();
    }

    @Override
    public Object getItem(int position) {
        return people.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if(convertView == null) {
            view =  LayoutInflater.from(mContext).inflate(R.layout.person_list_item, parent, false);
            TextView name= (TextView) view.findViewById(R.id.attendeeRoleTextView);
            TextView role= (TextView) view.findViewById(R.id.attendeeRoleTextView);
            TextView attending= (TextView) view.findViewById(R.id.attendeAttendingTextView);
            name.setText(people.get(position).first_name + " "+ people.get(position).last_name);
            role.setText(people.get(position).role);
            attending.setText(people.get(position).person_reply);
            TextView meeting_time= (TextView) view.findViewById(R.id.meeting_time);
//            DateTime time=meetings[position].meeting_time;
//            meeting_time.setText(time.getHourOfDay()+":"+time.getMinuteOfHour());
        }
        else {
            view = convertView;
        }
        return null;
    }
}
