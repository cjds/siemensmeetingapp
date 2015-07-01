package me.cjds.siemensmeetings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;

/**
 * Created by Carl Saldanha on 6/28/2015.
 */
public class AgendaAdapter extends BaseAdapter {

        Context mContext;
        ArrayList<Meeting> meetings;
        AgendaAdapter(Context c, ArrayList<Meeting> meetings){
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
                view = LayoutInflater.from(mContext).inflate(R.layout.agenda_meeting_item, parent, false);
                TextView meeting_name =(TextView) view.findViewById(R.id.agenda_title);
                meeting_name.setText(meetings.get(position).meeting_name);
                TextView meeting_time =(TextView) view.findViewById(R.id.agenda_time);
                DateTimeFormatter formatter = DateTimeFormat.forPattern("hh:mm a");

                meeting_time.setText(meetings.get(position).meeting_time.getStart().toString(formatter)+" - "+meetings.get(position).meeting_time.getEnd().toString(formatter));
            }
            else {
                view = convertView;
            }

            return view;

        }
    }

