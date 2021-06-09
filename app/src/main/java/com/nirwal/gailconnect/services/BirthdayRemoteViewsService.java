package com.nirwal.gailconnect.services;

import android.app.LauncherActivity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.nirwal.gailconnect.MainActivity;
import com.nirwal.gailconnect.MyApp;
import com.nirwal.gailconnect.R;
import com.nirwal.gailconnect.databse.ContactRepository;
import com.nirwal.gailconnect.modal.Contact;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BirthdayRemoteViewsService extends RemoteViewsService {
    private static final String TAG = "BirthdayRemoteViewsServ";

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.d(TAG, "onGetViewFactory: called");
        return new BirthdayWidgetItemFactory(getApplicationContext(), intent);
    }




    static class BirthdayWidgetItemFactory implements RemoteViewsFactory{
        private Context _context;
        private int _appWidgetId;
        private List<Contact> _birthDayList= new ArrayList<>();
        private ContactRepository _repository;


        public BirthdayWidgetItemFactory(Context context, Intent intent){
            this._context = context;
            this._appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,AppWidgetManager.INVALID_APPWIDGET_ID);
        }


        @Override
        public void onCreate() {

            _repository = new ContactRepository((MyApp)_context);

        }

        private String currentDayMonth(){
            Calendar c = Calendar.getInstance();
            int day = c.get(Calendar.DAY_OF_MONTH);
            int month = c.get(Calendar.MONTH)+1;

            DecimalFormat format = new DecimalFormat("00");

            String dob =  format.format(day)+"."+format.format(month)+".";

            return dob;
        }



        @Override
        public void onDataSetChanged() {
            String dob = currentDayMonth();
            _birthDayList = _repository.loadContactByDOB("%"+dob+"%");
            Log.d(TAG, "onDataSetChanged: "+_birthDayList.size());
            Log.d(TAG, "onDataSetChanged: "+dob);
        }

        @Override
        public void onDestroy() {
            // close / disconnect to data source like room
            _repository=null;
        }

        @Override
        public int getCount() {
            return _birthDayList ==null? 0 : _birthDayList.size();
        }


        @Override
        public RemoteViews getViewAt(int position) {
            //Log.d(TAG, "getViewAt: pos:"+position);

            RemoteViews views = new RemoteViews(_context.getPackageName(), R.layout.item_birthday_person_widget);
            Contact contact = _birthDayList.get(position);

            if(_birthDayList.isEmpty())return views;

            views.setTextViewText(R.id.birthday_name,
                    contact.getEmp_Name()+" ("+ contact.getGrade()+")\n"
                            + contact.getDesignation()+", " +contact.getDepartment()+"\n"
                            + contact.getLocation());

            //image lazy load from url
            try {
                Bitmap bitmap = Glide.with(_context.getApplicationContext())
                        .asBitmap()
                        .load(MyApp.IMG_URL+contact.getIMAGE())
                        .submit(512, 512)
                        .get();

                views.setImageViewBitmap(R.id.birthday_photo, bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Bundle bundle= new Bundle();
            bundle.putParcelable("data", contact);
            bundle.putString(MainActivity.ARG_1,"birthday_fragment");
            Intent fillInIntent = new Intent();
            fillInIntent.putExtras(bundle);
            views.setOnClickFillInIntent(R.id.birthday_wish_row, fillInIntent);

            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1; // no of remote view types
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
