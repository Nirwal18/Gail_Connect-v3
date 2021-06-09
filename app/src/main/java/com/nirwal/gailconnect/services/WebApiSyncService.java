package com.nirwal.gailconnect.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.nirwal.gailconnect.MainActivity;
import com.nirwal.gailconnect.MyApp;
import com.nirwal.gailconnect.R;
import com.nirwal.gailconnect.databse.ContactRepository;
import com.nirwal.gailconnect.databse.LocationRepository;
import com.nirwal.gailconnect.databse.OfficeRepository;
import com.nirwal.gailconnect.databse.Repository;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WebApiSyncService extends Service {
    private static final String TAG = "WebApiSyncService";
    private MyApp _app;
    private final IBinder _binder = new MyBinder();
    private NotificationCompat.Builder _notification;
    private NotificationManager _notificationManager;
    private static final int NOTIFICATION_ID =101;

    private String _taskStatus;

    private final Set<String> _taskTagList =new HashSet<>();
    private final Map<String, Integer> _max_Prog_data = new HashMap<>();


    @Override
    public void onCreate() {
        super.onCreate();
        _app = (MyApp) getApplication();
        _notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Intent intent1 = new Intent(this,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0, intent1, 0);

        _notification = new NotificationCompat
                .Builder(this, MyApp.Channel_Id_Foreground)
                .setOngoing(true)
                .setContentTitle("Database Sync")
                .setContentText("Connecting to server")
                .setSmallIcon(R.drawable.ic_gail_logo_transparent)
                .setPriority(NotificationCompat.PRIORITY_MIN)
                .setProgress(100, 0, true)
                .setContentIntent(pendingIntent);



        startForeground(NOTIFICATION_ID, _notification.build());

        startSync();

        return START_NOT_STICKY;

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return _binder;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        _thread.quitSafely();
        _thread=null;
    }

    HandlerThread _thread;
    private void startSync(){

         _thread = new HandlerThread("Sync_thread");
        _thread.start();

        Handler handler = new Handler(_thread.getLooper());

        new ContactRepository(_app).performSync(handler,callback, "Contact");
        new OfficeRepository(_app).performSync(handler,callback, "Office");
        new LocationRepository(_app).performSync(handler,callback, "ControlRoom");


    }



    private boolean _isWorkFinished =false;

    public boolean isWorkFinished(){
        return _isWorkFinished;
    }



    public String getTaskStatus(){
        return _taskStatus;
    }

    public void updateStatus(int prog,String msg) {
        _taskStatus = msg + prog+"%";
        _notificationManager.notify(
                NOTIFICATION_ID,
                _notification.setProgress(100,prog, false)
                        .setContentText(_taskStatus)
                        .build()
        );

    }


    private final Repository.ProgressUpdater callback = new Repository.ProgressUpdater(){



        @Override
        public void onSyncStatus(Repository.SyncStatus status, String tag) {

                switch (status){
                    case SYNC_STARTED:{
                        updateStatus(1, tag+": added to queue");
                        _taskTagList.add(tag);
                        break;
                    }
                    case DOWNLOADED:{
                        updateStatus(1, tag+": data downloaded");
                        break;
                    }
                    case OLD_DATA_DELETED:{
                        updateStatus(1,tag+" :Old data cleared");
                        break;
                    }
                    case SYNCING:{
                        updateStatus(1,tag+" :Syncing started");
                        break;
                    }
                    case SYNC_COMPLETED:{
                        updateStatus(100,tag+" :Sync Complete");
                        _taskTagList.remove(tag);
                        checkAndStopSelf();
                        break;
                    }
                }

        }

        @Override
        public void onError(String error, String tag) {
            Log.d(TAG, "onError: "+error);
            _taskStatus = error;
            updateStatus(1,tag+"failed");
        }

        @Override
        public void getMaxProgress(int max_progress, String tag) {
             _max_Prog_data.put(tag,max_progress);
        }

        @Override
        public void onProgressUpdate(int progress, String tag) {
            int max = _max_Prog_data.get(tag);
            progress = (100/max) * progress ;

            updateStatus(progress,tag+" syncing");
        }
    };


    private void checkAndStopSelf(){

        if(_taskTagList.isEmpty()){
            _isWorkFinished = true;
                    _app.mainThreadHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            stopForeground(true);
                        }
                    },2000);
        }
    }

    public class MyBinder extends Binder{
        public WebApiSyncService getService(){
            return WebApiSyncService.this;
        }
    }
}
