package com.nirwal.gailconnect;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MyPermissionManager {
    public static final int contact_permission_request=123;
    public static final int call_permission_request=122;
    public static boolean isWriteContactPermissionGranted(Context context){

        boolean b = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_CONTACTS) == (PackageManager.PERMISSION_GRANTED);
        return b;
    }

    public static void requestWriteContactPermission(Activity context){
        ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.WRITE_CONTACTS}, contact_permission_request);
    }

    public static boolean isCallPermissionGranted(Context context){

        boolean b = ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == (PackageManager.PERMISSION_GRANTED);
        return b;
    }

    public static void requestCallPermission(Activity context){
        ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.CALL_PHONE}, call_permission_request);
    }
}
