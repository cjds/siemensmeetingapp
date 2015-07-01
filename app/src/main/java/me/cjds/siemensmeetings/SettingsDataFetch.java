package me.cjds.siemensmeetings;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Carl Saldanha on 6/30/2015.
 */
public class SettingsDataFetch  extends AsyncTask<String,String,String> {

    AsyncResponse delegate;
    String json = "['Room 1','Room 2','Room 3']";
    SharedPreferences settings;
    SettingsDataFetch(AsyncResponse context){
        this.delegate=context;
        settings= PreferenceManager.getDefaultSharedPreferences((Activity) context);

    }


    @Override
    protected String doInBackground(String... params) {
        //params[0] will be the room_name
        //params[1] will be th day
        URL url = null;
        String out="";
        if(settings.getString("settingurl", "http://www.example.com").equals("null"))
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
            HttpGet httpPost=new HttpGet(settings.getString("settingurl", "http://www.example.com"));
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
        Log.e("out", out);
        return "[ERROR] "+out;
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

