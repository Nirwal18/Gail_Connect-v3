package com.nirwal.gailconnect.ui.birthdayList;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.nirwal.gailconnect.databse.ContactRepository;
import com.nirwal.gailconnect.modal.Contact;
import com.nirwal.gailconnect.tasks.Result;
import com.nirwal.gailconnect.tasks.TaskCallback;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BirthdayViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private ContactRepository _contactRepository;
    private MutableLiveData<List<Contact>> _birthdayList;
    private List<Contact> _tempContactList;


    public BirthdayViewModel(){
        _birthdayList = new MutableLiveData<>();
        _tempContactList = new ArrayList<>();

    }


    public LiveData<List<Contact>> get_birthdayList(Application application) {
        if(_birthdayList.getValue()==null || _birthdayList.getValue().size() <= 0){
            getBirthdayList(application);
        }
        return _birthdayList;
    }

    public void set_birthdayList(List<Contact> birthdayList) {
        this._birthdayList.setValue(birthdayList);
    }

    public void getBirthdayList(Application app){
        if(_contactRepository==null){
        _contactRepository =  new ContactRepository(app);
        }

        _contactRepository.loadContactByDOB("%" + currentDayMonth() + "%", new TaskCallback<List<Contact>>() {
            @Override
            public void onComplete(Result result) {
                if(result instanceof Result.Success) {
                    if(_tempContactList.size()>0) _tempContactList.clear();
                    _tempContactList = ((Result.Success<List<Contact>>) result).data;
                    _birthdayList.setValue(_tempContactList);
                }
            }
        });

    }





    private String currentDayMonth(){
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH)+1;

        DecimalFormat format = new DecimalFormat("00");

        return format.format(day)+"."+format.format(month)+".";
    }

}