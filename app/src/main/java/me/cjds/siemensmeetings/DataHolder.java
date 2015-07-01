package me.cjds.siemensmeetings;

import android.util.Log;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Carl Saldanha on 6/29/2015.
 */
public class DataHolder {
    private ArrayList<Meeting> data;
    public ArrayList<Meeting> getData() {return data;}
    public void setData(ArrayList<Meeting> data) {this.data = data;}


    private static final DataHolder holder = new DataHolder();
    public static DataHolder getInstance() {return holder;}

    public void arrangeData(String jsonData){
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            if (this.data == null)
                this.data=(new ArrayList<Meeting>());
            ArrayList<Meeting> meetings = new ArrayList<Meeting>();
            for (int j = 0; j < jsonArray.length(); j++) {

                JSONObject json = jsonArray.getJSONObject(j);
//              if (json.getString("hello").equals("there"))
                DateTimeFormatter formatter= DateTimeFormat.forPattern("dd/mm/yyyy hh:mm:ss a");
                DateTime start=DateTime.parse(json.getString("start"), formatter);
                DateTime end=DateTime.parse(json.getString("end"),formatter);
                JSONArray peopleJSON= json.getJSONArray("attendees");
                ArrayList<Person> persons = new ArrayList<Person>();
                for (int i = 0; i < peopleJSON.length(); i++) {

                    String name=(String)peopleJSON.getString(i);
                    name=name.trim();
                    name=name.substring(0, name.length() - 7);
                    name=name.trim();
                    if(name.contains("(ext)"))
                        name.substring(0,name.length()-5);
                    name.trim();
                    String[] arr=name.split(",");
                    persons.add(new Person(arr[1],arr[0],"","Attending"));
                }
                long currentTime =Calendar.getInstance().getTimeInMillis()+j*60*1000;
                meetings.add(new Meeting(json.getString("subject"), new Interval(start,end), persons));
            }
            if (!Meeting.compareTo(this.data, meetings)) {
                this.data = meetings;
            }

        }
        catch (Exception e){
            Log.e("Error","ERRROR");
            e.printStackTrace();
        }
    }

}
