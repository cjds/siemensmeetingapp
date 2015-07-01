package me.cjds.siemensmeetings;

import android.os.Parcel;
import android.os.Parcelable;

import org.joda.time.DateTime;
import org.joda.time.Interval;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

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
    public boolean equals(Object other){
        //@Todo write a better equals method
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof Meeting))return false;
        Meeting otherMyClass = (Meeting)other;
        if(otherMyClass.meeting_time.toDuration().equals(this.meeting_time.toDuration())){
            if(otherMyClass.meeting_name.equals(this.meeting_name))
                return true;
            else
                return false;
        }
        else{
            return false;
        }
    }

    public static boolean compareTo(ArrayList<Meeting> m1, ArrayList<Meeting> m2){
        if(m1.size()!=m2.size())return false;
        int count=0;
        for(int i=0;i<m1.size();i++){
            Meeting m=m1.get(i);
            for(int j=0;j<m2.size();j++){
                if(m.equals(m2.get(j))){
                    count++;
                }
            }
        }

        if(count==m1.size())return true;
        else return false;
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
