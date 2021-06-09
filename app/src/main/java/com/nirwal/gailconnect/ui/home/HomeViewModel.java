package com.nirwal.gailconnect.ui.home;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.nirwal.gailconnect.Constants;
import com.nirwal.gailconnect.MyApp;
import com.nirwal.gailconnect.databse.ContactRepository;
import com.nirwal.gailconnect.modal.Contact;
import com.nirwal.gailconnect.tasks.Result;
import com.nirwal.gailconnect.tasks.TaskCallback;

public class HomeViewModel extends ViewModel {
    public static final String EMPLOY_NAME = "EMPLOY_NAME";
    public static final String EMPLOY_DESIG = "EMPLOY_DESIG";
    public static final String EMPLOY_GRADE = "EMPLOY_GRADE";
    public static final String EMPLOY_IMAGE_URL = "EMPLOY_IMAGE_URL";

    public void fetchEmployLocAndDept(Application app, SharedPreferences shredPref, OnEmployNameUpdate listner){
        String cpf = shredPref.getString(Constants.USER_CPF,"");

        new ContactRepository(app).getContactByCPF(cpf, new TaskCallback<Contact>() {
            @Override
            public void onComplete(Result result) {
                if(result instanceof Result.Success){
                    Contact contact =  ((Result.Success<Contact>) result).data;

                    if(contact==null)return;

                    shredPref.edit().putString(Constants.USER_DEPARTMENT,contact.getDepartment())
                            .putString(Constants.USER_WORK_LOCATION,contact.getLocation())
                            .putString(EMPLOY_NAME,contact.getEmp_Name())
                            .putString(EMPLOY_DESIG,contact.getDesignation())
                            .putString(EMPLOY_GRADE,contact.getGrade())
                            .putString(EMPLOY_IMAGE_URL, contact.getIMAGE())
                            .apply();

                    listner.onSuccess(contact.getEmp_Name());
                }
            }
        });
    }

    interface OnEmployNameUpdate{
        void onSuccess(String name);
    }


}