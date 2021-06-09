package com.nirwal.gailconnect.ui.setting;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceFragmentCompat;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.nirwal.gailconnect.MainActivity;
import com.nirwal.gailconnect.R;
import com.nirwal.gailconnect.tasks.MyWorker;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;


public class SettingsFragment extends PreferenceFragmentCompat implements
        SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String TAG = "SettingsFragment";

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.d(TAG, "onSharedPreferenceChanged: ");
        MainActivity activity = null;
        if(getActivity() instanceof MainActivity){
            activity = ((MainActivity)getActivity());
        }

        if(activity == null) return;

        if(key.equals("theme")){
          activity.initLightDarkMode();
        }
        else if(key.equals("syncPeriodically")){
            boolean result = sharedPreferences.getBoolean("syncPeriodically", false);
            if(result) postWorker();
        }


    }

    private void postWorker(){
        PeriodicWorkRequest uploadWorkRequest =
                new PeriodicWorkRequest.Builder(MyWorker.class,7, TimeUnit.DAYS)
                        .addTag("GailConnectSyncWorker")
                        .build();
        WorkManager
                .getInstance(requireContext())
                .enqueueUniquePeriodicWork("GailConnectSyncWorker", ExistingPeriodicWorkPolicy.KEEP,uploadWorkRequest);

    }
}