package me.cjds.siemensmeetings;

import android.os.Parcel;
import android.os.Parcelable;

import org.joda.time.DateTime;
import org.joda.time.Interval;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by Carl Saldanha on 6/13/2015.
 */
public class Meeting implements Parcelable{

    String meeting_name;
    Interval meeting_time;
    ArrayList<Person> attendees;

    public static final Parcelable.Creator<Meeting> CREATOR = new Parcelable.Creator<Meeting>() {
        public Meeting createFromParcel(Parcel in) {
            return new Meeting(in);
        }

        public Meeting[] newArray(int size) {
            return new Meeting[size];
        }
    };

    Meeting(String meeting_name,Interval meeting_time,ArrayList<Person> attendees){
        this.meeting_name=meeting_name;
        this.meeting_time=meeting_time;
        this.attendees=attendees;
    }

    Meeting (Parcel in){
        meeting_time=new Interval(in.readLong(),in.readLong());
        meeting_name=in.readString();
        Person[] person=(Person[])in.createTypedArray(Person.CREATOR);
        attendees=new ArrayList<Person>(Arrays.asList(person));

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(meeting_time.getStart().getMillis());
        dest.writeLong(meeting_time.getEnd().getMillis());
        dest.writeString(meeting_name);
        Person[] persons=new Person[attendees.size()];
        for(int i=0;i<persons.length;i++)
            persons[i]=attendees.get(i);
        dest.writeTypedArray(persons, 0);

    }
}
