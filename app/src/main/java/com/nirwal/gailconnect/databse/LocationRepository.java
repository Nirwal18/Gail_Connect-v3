package com.nirwal.gailconnect.databse;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.nirwal.gailconnect.MyApp;
import com.nirwal.gailconnect.modal.Contact;
import com.nirwal.gailconnect.modal.Location;
import com.nirwal.gailconnect.modal.Office;
import com.nirwal.gailconnect.tasks.Result;
import com.nirwal.gailconnect.tasks.TaskCallback;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationRepository extends Repository<Location> {
    private static final String TAG = "LocationRepository";
    private MyApp _app = getApplication();
    private final LocationDao _locationDao;

    public LocationRepository(Application application){
        super(application);
        _app = (MyApp) application;
        AppDatabase database = AppDatabase.getInstance(application);
        _locationDao = database.locationDao();
    }


    public LiveData<List<Location>> getAll(){
        return _locationDao.getAll();
    }



    public void insertAllLocation(TaskCallback<Result> callback, Location... locations){
        _app.executorService.execute(() -> {
            _locationDao.insertAll(locations);
            Result result = new Result.Success<Boolean>(true);
            postResult(callback,result);
        });
    }


    public void deleteAllLocations(){
        _app.executorService.execute(() -> {
            _locationDao.deleteAll();
        });
    }





    private void postResult(TaskCallback<?> callback, Result data){
        _app.mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onComplete(data);
            }
        });
    }


    @Override
    public void insert(Location obj) {
        _locationDao.insertAll(obj);
    }

    @Override
    public void insertAll(Location... objs) {
        _locationDao.insertAll(objs);
    }

    @Override
    public void delete(Location obj) {
        _locationDao.delete(obj);
    }

    @Override
    public void deleteAll() {
        _locationDao.deleteAll();
    }

    @Override
    public Call<List<Location>> getWebRepository() {
        return getApplication().get_serviceApi().getLocations();
    }

    @Override
    public void performSync(Handler handler, ProgressUpdater callback, String tag) {
        sync(
                getWebRepository(),
                handler,
                callback,
                tag
        );
    }
}
