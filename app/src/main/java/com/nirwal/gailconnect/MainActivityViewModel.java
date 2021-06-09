package com.nirwal.gailconnect;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.IBinder;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.nirwal.gailconnect.services.ConnectionLiveData;
import com.nirwal.gailconnect.services.WebApiSyncService;

public class MainActivityViewModel extends ViewModel {
    private final MutableLiveData<WebApiSyncService.MyBinder> _binder = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isProgressBarUpdating = new MutableLiveData<>();

    public ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            _binder.postValue((WebApiSyncService.MyBinder) service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            _binder.postValue(null);
        }
    };


    public ServiceConnection getServiceConnection() {
        return serviceConnection;
    }

    public LiveData<WebApiSyncService.MyBinder> getBinder() {
        return _binder;
    }

    public MutableLiveData<Boolean> getIsProgressBarUpdating() {
        return isProgressBarUpdating;
    }

    public void setIsProgressBarUpdating(boolean isUpdating){
        isProgressBarUpdating.postValue(isUpdating);
    }
}
