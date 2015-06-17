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
public class CalendarMeetingListAdapter extends BaseAdapter {
//
    Context mContext;
    ArrayList<Meeting> meetings;
    CalendarMeetingListAdapter(Context context,ArrayList<Meeting> meetings){
        this.meetings=meetings;
        mContext=context;
    }

    @Override
    public int getCount() {
        return meetings.size();
    }

    @Override
    public Object getItem(int position) {
        return meetings.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if(convertView == null) {
//            view =  LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);
//            TextView meeting_name= (TextView) view.findViewById(R.id.meeting_name);
//            meeting_name.setText(meetings[position].meeting_name);
//            TextView meeting_time= (TextView) view.findViewById(R.id.meeting_time);
//            DateTime time=meetings[position].meeting_time;
//            meeting_time.setText(time.getHourOfDay()+":"+time.getMinuteOfHour());
        }
        else {
            view = convertView;
        }
        return null;
    }
}
