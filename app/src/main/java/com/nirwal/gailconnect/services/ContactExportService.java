package com.nirwal.gailconnect.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.nirwal.gailconnect.MainActivity;
import com.nirwal.gailconnect.MyApp;
import com.nirwal.gailconnect.R;

public class ContactExportService extends Service {

    private MyApp _app;
    private final IBinder _binder = new MyBinder();
    private NotificationCompat.Builder _notification;
    private NotificationManager _notificationManager;
    private static final int NOTIFICATION_ID =101;



    @Override
    public void onCreate() {
        super.onCreate();
        _app = (MyApp) getApplication();
        _notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return _binder;
    }

    public class MyBinder extends Binder{
        public ContactExportService getService(){
            return ContactExportService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Intent intent1 = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0, intent1, 0);

        _notification = new NotificationCompat
                .Builder(this, MyApp.Channel_Id_Foreground)
                .setOngoing(true)
                .setContentTitle("Database Sync")
                .setContentText("Connecting to server")
                .setSmallIcon(R.drawable.ic_gail_notification)
                .setPriority(NotificationCompat.PRIORITY_MIN)
                .setProgress(100, 0, true)
                .setContentIntent(pendingIntent);



        startForeground(NOTIFICATION_ID, _notification.build());
        return START_NOT_STICKY;
    }
}
