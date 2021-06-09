package com.nirwal.gailconnect.ui.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.bumptech.glide.request.target.AppWidgetTarget;
import com.nirwal.gailconnect.MainActivity;
import com.nirwal.gailconnect.R;
import com.nirwal.gailconnect.modal.Contact;
import com.nirwal.gailconnect.services.BirthdayRemoteViewsService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Implementation of App Widget functionality.
 */
public class HappyBirthdayWidget extends AppWidgetProvider {
    public static final String EXTRA_ITEM = "EXTRA_ITEM";


    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {


        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_happy_birthday);

        // remote view class need differnt adaptor RemoteViewsService and RemoteViewsFactory
        Intent serviceIntent = new Intent(context, BirthdayRemoteViewsService.class);
        serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,appWidgetId);
        serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));

        views.setRemoteAdapter(R.id.widget_birthday_list,serviceIntent);
        views.setEmptyView(R.id.widget_birthday_list, R.id.birthday_empty_txt);

        views.setTextViewText(R.id.data_update_status_txt,context.getString(R.string.last_updated_on,getCurrentDateTime()));

        Intent openAppIntent = new Intent(context, MainActivity.class);
        openAppIntent.setAction(Intent.ACTION_VIEW);
        views.setPendingIntentTemplate(R.id.widget_birthday_list,
                PendingIntent.getActivity(context,0,openAppIntent,PendingIntent.FLAG_UPDATE_CURRENT)
        );

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private static String getCurrentDateTime(){
        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm aaa");
        return df.format(Calendar.getInstance().getTime());
    }





    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }


}