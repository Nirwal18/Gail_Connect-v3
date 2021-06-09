package com.nirwal.gailconnect;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

import com.nirwal.gailconnect.modal.Link;

public class MyIntentManager {
    public static Intent openMapWithSearchIntent(String searchTxt){
        String query = searchTxt.replace(" ","+");
        Uri uri = Uri.parse("geo:0,0?q="+query);
        Intent intent = new Intent(Intent.ACTION_VIEW,uri);;
        intent.setPackage("com.google.android.apps.maps");
        return intent;
    }

    public static Intent openMapWithCoordinates(String latitude, String longitude){
        Uri uri = Uri.parse("geo:"+latitude+","+longitude);
        Intent intent = new Intent(Intent.ACTION_VIEW,uri);;
        intent.setPackage("com.google.android.apps.maps");
        return intent;
    }

    public static Intent openBrowser(String url){
        return new Intent(Intent.ACTION_VIEW, Uri.parse(url));
    }

    public static Intent openSettingForSelf(Context context){
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        return intent.setData(uri);
        //startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
    }

    public static void openEmail(Context context, String emailAddress, String subject, String msgBody ){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] { emailAddress });
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, msgBody);
        context.startActivity(Intent.createChooser(intent, "Birthday Wish"));
    }

    public static Intent sendSMS(String srcNumber, String message){
        Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( "sms:" + srcNumber));
        intent.putExtra( "sms_body", message );
        return intent;
    }
}
