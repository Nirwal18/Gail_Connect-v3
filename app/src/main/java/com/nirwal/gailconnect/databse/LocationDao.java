package com.nirwal.gailconnect.databse;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.nirwal.gailconnect.modal.Location;

import java.util.List;

@Dao
public interface LocationDao {
    @Query("SELECT * FROM location_table")
    public LiveData<List<Location>> getAll();

    @Insert
    void insertAll(Location... locations);

    @Delete
    void delete(Location location);

    @Query("DELETE FROM location_table")
    void deleteAll();

}
