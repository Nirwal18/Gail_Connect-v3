package com.nirwal.gailconnect.services;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import java.util.HashSet;
import java.util.Set;


public class ConnectionLiveData extends LiveData<Boolean> {
    private static final String TAG = "ConnectionLiveData";

    private Set<Network> _validNetworks = new HashSet<>();
    private ConnectivityManager _connectivityManager;

    public ConnectionLiveData(Context _context) {
        super(false);
        _connectivityManager = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    private final ConnectivityManager.NetworkCallback _networkCallback = new ConnectivityManager.NetworkCallback(){
        @Override
        public void onAvailable(@NonNull Network network) {
           NetworkCapabilities networkCapabilities = _connectivityManager.getNetworkCapabilities(network);
            boolean isInternet = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
            if(isInternet){
                _validNetworks.add(network);
            }
            checkValidNetwork();
        }

        @Override
        public void onLost(@NonNull Network network) {
            _validNetworks.remove(network);
            checkValidNetwork();
        }


    };


    private void checkValidNetwork(){
        postValue(_validNetworks.size() > 0);
    }

    @Override
    protected void onActive() {
        NetworkRequest networkRequest = new NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build();
        _connectivityManager.registerNetworkCallback(networkRequest,_networkCallback);
    }

    @Override
    protected void onInactive() {
        _connectivityManager.unregisterNetworkCallback(_networkCallback);
    }

}
