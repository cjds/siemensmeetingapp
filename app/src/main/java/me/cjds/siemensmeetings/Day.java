package me.cjds.siemensmeetings;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Carl Saldanha on 6/13/2015.
 */
public class Day {

    ArrayList<Meeting> meetings;
    DateTime day;

    Day(ArrayList<Meeting> meetings,DateTime day){
        this.meetings=meetings;
        this.day=day;

    }

}
