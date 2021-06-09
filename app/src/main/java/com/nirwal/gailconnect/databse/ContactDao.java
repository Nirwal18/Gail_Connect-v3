package com.nirwal.gailconnect.databse;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.nirwal.gailconnect.modal.Contact;

import java.util.List;

@Dao
public interface ContactDao {

    @Query("SELECT * FROM contact_table ORDER BY Emp_Name ASC")
    public List<Contact> getAll();


//    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
//    List<User> loadAllByIds(int[] userIds);
//
//    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
//            "last_name LIKE :last LIMIT 1")
//    User findByName(String first, String last);

    @Query("SELECT * FROM contact_table WHERE Location LIKE :location ORDER BY Emp_Name ASC")
    public List<Contact> loadContactByLocation(String location);


    @Query("SELECT * FROM contact_table WHERE Location LIKE :location AND Department LIKE :department ORDER BY Emp_Name ASC")
    public List<Contact> loadContactByLocationAndDept(String location, String department);

    @Query("SELECT * FROM contact_table WHERE Emp_no LIKE :cpf LIMIT 1 ")
    public Contact getContactByCPF(String cpf);

    @Query("SELECT * FROM contact_table WHERE Emp_Name LIKE :name ORDER BY Emp_Name ASC")
    public List<Contact> getContactByName(String name);

    @Query("SELECT * FROM contact_table WHERE DateOfBirth LIKE :dob ORDER BY Emp_Name ASC")
    public List<Contact> getContactByDOB(String dob);

    @Insert
    void insert(Contact contact);

    @Insert
    void insertAll(Contact... contacts);

    @Delete
    void delete(Contact contact);

    @Query("DELETE FROM contact_table")
    void deleteAll();

}

