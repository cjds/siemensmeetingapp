package me.cjds.siemensmeetings;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Carl Saldanha on 6/15/2015.
 */
public class Person implements Parcelable{
    String first_name;
    String last_name;
    String role;
    String person_reply;

    public static final Parcelable.Creator<Person> CREATOR = new Parcelable.Creator<Person>() {
        public Person createFromParcel(Parcel in) {
            return new Person(in);
        }

        public Person[] newArray(int size) {
            return new Person[size];
        }
    };

    Person(String first_name,String last_name,String role,String person_reply){
        this.first_name=first_name;
        this.last_name=last_name;
        this.role=role;
        this.person_reply=person_reply;
    }

    Person(Parcel in){
        first_name=in.readString();
        last_name=in.readString();
        role=in.readString();
        person_reply=in.readString();
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(first_name);
        dest.writeString(last_name);
        dest.writeString(role);
        dest.writeString(person_reply);
    }
}
