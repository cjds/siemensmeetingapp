package me.cjds.siemensmeetings;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Carl Saldanha on 6/22/2015.
 */

public class DataFetchHandler extends AsyncTask<String,String,String> {

    AsyncResponse delegate;
    String json = " [ {'start':'07/09/2015 11:30:00 AM', 'end':'07/09/2015 2:30:00 PM', 'attendees': ['Underwood-Gess, Donna (CT US)',' Joiner, Carolyn (CT US)', ' Azar, Natasha (ext) (CT US)'], 'subject': 'Dr Gaus Breakfast'}]";

   SharedPreferences settings;


    DataFetchHandler(AsyncResponse context){

        this.delegate=context;
        settings= PreferenceManager.getDefaultSharedPreferences((Activity)context);
    }

    @Override
    protected String doInBackground(String... params) {
        //params[0] will be the room_name
        //params[1] will be th day
        URL url = null;
        String out="";
        if(settings.getString("ipaddressurl", "http://www.example.com").equals("null"))
            return json;
        try {
        /*    url = new URL("google.com");
            HttpURLConnection urlConnection=(HttpURLConnection)url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setChunkedStreamingMode(0);
            ContentValues values=new ContentValues();
            values.put("room",settings.getString("roomname",""));
            OutputStream os=urlConnection.getOutputStream();
            BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
            InputStream in=urlConnection.getInputStream();
            BufferedReader br=new BufferedReader(new InputStreamReader(in));
            writer.write("room="+values.get("roomname"));
            writer.close();
            writer.flush();
            os.close();
            out=br.readLine();
            urlConnection.connect();*/
            HttpClient httpClient=new DefaultHttpClient();
            HttpPost httpPost=new HttpPost(settings.getString("ipaddressurl", "http://www.example.com"));
            List<NameValuePair> pairs=new ArrayList<NameValuePair>();
            DateTimeFormatter formatter= DateTimeFormat.forPattern(settings.getString("dateTimeFormat","dd/mm/yyyy"));
            DateTime dt= DateTime.now();
            pairs.add(new BasicNameValuePair(settings.getString("roomnameparameter","roomname"), settings.getString("roomname", "")));
            pairs.add(new BasicNameValuePair(settings.getString("datetime", "date"),dt.toString(formatter)));
            httpPost.setEntity(new UrlEncodedFormEntity(pairs));
            HttpResponse response=httpClient.execute(httpPost);
            out= EntityUtils.toString(response.getEntity());
            return out;
        } catch (MalformedURLException e) {
           out=    e.getMessage();
            e.printStackTrace();
        } catch (IOException e) {
            out=    e.getMessage();
            e.printStackTrace();
        }
        Log.e("out",out);
        return "[ERROR] "+out;
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
