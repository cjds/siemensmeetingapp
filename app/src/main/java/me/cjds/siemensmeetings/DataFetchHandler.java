package me.cjds.siemensmeetings;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Carl Saldanha on 6/22/2015.
 */

public class DataFetchHandler extends AsyncTask<String,String,String> {

    AsyncResponse delegate;


    String json = "[{'hello':'there'},{'hello':'where'}]";
    DataFetchHandler(AsyncResponse context){
        this.delegate=context;
    }


    @Override
    protected String doInBackground(String... params) {
        return json;
    }

    private Meeting[] getAllMeetings(){
        return null;
    }

    private Meeting getMeetingDetails(){
        return null;
    }

    private void PostExecuteAllMeetings(){}
    private void PostExecuteGetMeetingDetails(){}
    @Override
    protected void onPostExecute(String result) {
        delegate.doSomething(result);
        //TextView txt = (TextView)((Activity) context).findViewById(R.id.meeting_list);
        //txt.setText("Executed"); // txt.setText(result);
        // might want to change "executed" for the returned string passed
        // into onPostExecute() but that is upto you

    }

}
