package com.nirwal.gailconnect.tasks;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.nirwal.gailconnect.MyApp;
import com.nirwal.gailconnect.services.WebApiSyncService;
import com.nirwal.gailconnect.services.WebServiceApi;
import com.nirwal.gailconnect.databse.ContactRepository;
import com.nirwal.gailconnect.databse.LocationRepository;
import com.nirwal.gailconnect.databse.OfficeRepository;
import com.nirwal.gailconnect.modal.Contact;
import com.nirwal.gailconnect.modal.Location;
import com.nirwal.gailconnect.modal.Office;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyWorker extends Worker {

    private static final String TAG = "MyWorker";

    private Result work_result = Result.success();


    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

    }

    @NonNull
    @Override
    public Result doWork() {
        // Indicate whether the work finished successfully with the Result
        Log.d(TAG, "doWork: ");
        syncDataFromWebService();
        return work_result;
    }

    public void syncDataFromWebService(){
        Intent intent = new Intent(this.getApplicationContext(), WebApiSyncService.class);
       getApplicationContext().startService(intent);
    }



}
