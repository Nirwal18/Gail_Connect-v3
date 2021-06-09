package com.nirwal.gailconnect.databse;

import android.app.Application;
import android.os.Handler;
import android.util.Log;

import com.nirwal.gailconnect.MyApp;
import com.nirwal.gailconnect.modal.Contact;
import com.nirwal.gailconnect.modal.Office;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public abstract class Repository<T> {
    private static final String TAG = "Repository";
    private final MyApp _application;


    public Repository(@NotNull Application _app) {
        this._application = (MyApp) _app;
    }

    public MyApp getApplication() {
        return _application;
    }

    public abstract void insert(T obj);
    public abstract void insertAll(T... objs);
    public abstract void delete(T obj);
    public abstract void deleteAll();

    public abstract Call<List<T>> getWebRepository();
    public abstract void performSync(Handler handler, ProgressUpdater callback, String tag);

    public static abstract class ProgressUpdater{

        public abstract void onSyncStatus(SyncStatus status, String tag);
        public abstract void onError(String error, String tag);
        public abstract void getMaxProgress(int max_progress, String tag);
        public abstract void onProgressUpdate(int progress, String tag);
    }

    public void sync(Call<List<T>> webRepository,Handler handler, ProgressUpdater callback,String tag) {

        updateSyncStatus(SyncStatus.SYNC_STARTED, callback, tag);


        webRepository.enqueue(new Callback<List<T>>() {
            @Override
            public void onResponse(@NotNull Call<List<T>> call, @NotNull Response<List<T>> response) {

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        List<T> list = response.body();

                        if (response.code() > 300) {
                            updateError("Web Response code :" + response.code(), callback, tag);

                            return;
                        }

                        updateSyncStatus(SyncStatus.DOWNLOADED, callback, tag);

                        Log.d(TAG, "onResponse: SIZE:" + list.size());

                        deleteAll();
                        updateSyncStatus(SyncStatus.OLD_DATA_DELETED, callback, tag);
                        updateMaxProgress(list.size(), callback, tag);
                        updateSyncStatus(SyncStatus.SYNCING, callback, tag);

                        int i = 0;
                        for (T item : list) {
                            insert(item);
                           // updateProgress(i, callback, tag);
                            i++;
                        }

                        updateSyncStatus(SyncStatus.SYNC_COMPLETED, callback,tag);
                    }
                });


            }

            @Override
            public void onFailure(@NotNull Call<List<T>> call, @NotNull Throwable t) {
                updateError(t.getMessage(), callback, tag);
            }
        });


    }

    private void updateProgress(int progress, ProgressUpdater callback, String tag){
        getApplication().mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onProgressUpdate(progress,tag);
            }
        });
    }

    private void updateMaxProgress(int max_progress, ProgressUpdater callback, String tag){
        getApplication().mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.getMaxProgress(max_progress,tag);
            }
        });
    }

    private void updateSyncStatus(SyncStatus status, ProgressUpdater callback, String tag){
        getApplication().mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onSyncStatus(status, tag);
            }
        });
    }

    private void updateError(String error, ProgressUpdater callback,String tag){
        getApplication().mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onError(error, tag);
            }
        });
    }


    public enum SyncStatus{
        SYNC_STARTED,
        DOWNLOADED,
        OLD_DATA_DELETED,
        SYNCING,
        SYNC_COMPLETED
    }

}
