package com.nirwal.gailconnect.databse;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.nirwal.gailconnect.modal.Office;

import java.util.List;

@Dao
public interface OfficeDao {
    @Query("SELECT * FROM office_table")
    public LiveData<List<Office>> getAll();

    @Insert
    void insert(Office office);

    @Insert
    void insertAll(Office... offices);

    @Delete
    void delete(Office office);

    @Query("DELETE FROM office_table")
    void deleteAll();

}
