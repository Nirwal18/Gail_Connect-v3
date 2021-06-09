package com.nirwal.gailconnect;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import androidx.core.os.HandlerCompat;
import androidx.room.Room;

import com.nirwal.gailconnect.databse.AppDatabase;
import com.nirwal.gailconnect.services.WebServiceApi;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyApp extends Application {
    private static final String TAG = "MyApp";
    public static final String SHEARED_PREF=TAG+"_SETTING";
    public static final String URL = "https://gailebank.gail.co.in/webservices/contactinfo/pipelineinfoservice.svc/";
    public static final String IMG_URL = "https://gailebank.gail.co.in/WebServices/Consolidated/";
    public static final String PDF_URL = "https://gailebank.gail.co.in/Gail_Connect_News_Holiday/UploadedPDF/";
    public static final String Channel_Id_Foreground = "Data update channel";


    public ExecutorService executorService = Executors.newFixedThreadPool(4);
    public Handler mainThreadHandler = HandlerCompat.createAsync(Looper.getMainLooper());

    private WebServiceApi _serviceApi;


    @Override
    public void onCreate() {
        super.onCreate();

        createNotiFiactionChannel();

    }

    public WebServiceApi get_serviceApi() {
        if(_serviceApi==null){

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(MyApp.URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            _serviceApi = retrofit.create(WebServiceApi.class);

        }

        return _serviceApi;
    }


    private void createNotiFiactionChannel(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel forgroundServiceChannel = new NotificationChannel(
                    Channel_Id_Foreground,
                    "ForeGround Service channel",
                    NotificationManager.IMPORTANCE_MIN
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(forgroundServiceChannel);
        }
    }

}
