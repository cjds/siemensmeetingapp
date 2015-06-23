package me.cjds.siemensmeetings;

import android.os.AsyncTask;

/**
 * Created by Carl Saldanha on 6/22/2015.
 */
public class DataFetchHandler extends AsyncTask<String,String,String> {

    String json = "[{'hello':'there'},{'hello':'where'}]";

    @Override
    protected String doInBackground(String... params) {
        return null;
    }

    private Meeting[] getAllMeetings(){
        return null;
    }

    private Meeting getMeetingDetails(){
        return null;
    }
}
