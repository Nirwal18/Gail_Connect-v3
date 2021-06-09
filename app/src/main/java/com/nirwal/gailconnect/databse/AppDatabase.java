package com.nirwal.gailconnect.databse;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.nirwal.gailconnect.modal.Contact;
import com.nirwal.gailconnect.modal.Location;
import com.nirwal.gailconnect.modal.Office;

@Database(entities = {Contact.class, Location.class, Office.class}, version = 2 ,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract ContactDao contactDao();
    public abstract LocationDao locationDao();
    public abstract OfficeDao officeDao();


    public static synchronized AppDatabase getInstance(Context context){
        if(instance==null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "database-gailconnect")
                    .fallbackToDestructiveMigration() // delete database in case version changes
                    .build();

        }
        return instance;
    }

}
