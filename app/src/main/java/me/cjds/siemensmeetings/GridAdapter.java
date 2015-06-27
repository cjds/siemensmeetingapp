package me.cjds.siemensmeetings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Carl Saldanha on 6/13/2015.
 */
public class  GridAdapter extends BaseAdapter {
    private Context mContext;
    private DateTime date;
    private Day[] days;
    private LayoutInflater mInflater;
    GregorianCalendar calendar;

    public GridAdapter(Context c,ArrayList<ArrayList<Meeting>> meetings,int year,int month){
        calendar=new GregorianCalendar(year,month,1);

        mContext=c;
        days=new Day[meetings.size()];

        for(int i=0;i<meetings.size();i++) {
            Long milliseconds = calendar.getTimeInMillis();
            Day d = new Day(meetings.get(i),new DateTime(milliseconds));
            days[i]=d;
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }
    }

    @Override
    public int getCount() {
        return days.length;
    }

    @Override
    public Object getItem(int position) {
        Calendar cal=calendar;
        cal.add(Calendar.DAY_OF_YEAR,position);
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if(convertView == null) {
            if(day[-Calendar.SUNDAY>)
            view = LayoutInflater.from(mContext).inflate(R.layout.grid_layout, parent, false);
            TextView day_number =(TextView) view.findViewById(R.id.day_number);
            ListView lv=(ListView)view.findViewById(R.id.meeting_list);
            lv.setAdapter(new CalendarListAdapter(mContext,days[position].meetings));
            DateTime day = days[position].day;
            day_number.setText(day.getDayOfMonth()+" "+day.getYear());
        }
        else {
            view = convertView;
        }

        return view;
    }
}
