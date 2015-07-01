package me.cjds.siemensmeetings;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Carl Saldanha on 6/27/2015.
 */
public class MeetingService extends Service {

    Runnable runnable;
    long timeBeforeNextRun=1000*10;
    private final IBinder mBinder = new LocalBinder();

    @Override
    public void onCreate() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public class LocalBinder extends Binder {
        MeetingService getService() {
            return MeetingService.this;
        }
    }

}
