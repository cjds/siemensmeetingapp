package me.cjds.siemensmeetings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;

/**
 * Created by Carl Saldanha on 6/13/2015.
 */
public class MeetingAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<Meeting> meetings;
    MeetingAdapter(Context c, ArrayList<Meeting> meetings){
        mContext=c;
        this.meetings=meetings;
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
            view = LayoutInflater.from(mContext).inflate(R.layout.calendar_meeting_dialog_item, parent, false);
            TextView meeting_name =(TextView) view.findViewById(R.id.dialog_meeting_name);
            meeting_name.setText(meetings.get(position).meeting_name);
            TextView meeting_time =(TextView) view.findViewById(R.id.dialog_meeting_time);
            DateTimeFormatter formatter = DateTimeFormat.forPattern("hh:mm a");

            meeting_time.setText(meetings.get(position).meeting_time.getStart().toString(formatter)+" - "+meetings.get(position).meeting_time.getStart().toString(formatter));
        }
        else {
            view = convertView;
        }

        return view;

    }
}
