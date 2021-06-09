package com.nirwal.gailconnect.ui.search;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.nirwal.gailconnect.MyApp;
import com.nirwal.gailconnect.databse.ContactRepository;
import com.nirwal.gailconnect.modal.Contact;
import com.nirwal.gailconnect.tasks.Result;
import com.nirwal.gailconnect.tasks.TaskCallback;

import java.util.List;

public class SearchContactViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private static final String TAG = "SearchContactViewModel";

    private MutableLiveData<List<Contact>> _contactList;
    private ContactRepository _contactRepository;
    public SearchContactViewModel() {
        _contactList = new MutableLiveData<>();


    }


    public LiveData<List<Contact>> getContactList() {
        return _contactList;
    }

    public void setContactList(List<Contact> contactList) {
        this._contactList.setValue(contactList);
    }

    public void loadContactByName(Context context, String name){
        MyApp app = (MyApp)context.getApplicationContext();
        Log.d(TAG, "loadContactByName: "+name);
        if(_contactRepository==null){
            _contactRepository = new ContactRepository(app);
        }


        _contactRepository.loadContactByName("%"+name+"%", new TaskCallback<List<Contact>>() {
            @Override
            public void onComplete(Result result) {
                if(result instanceof Result.Success){
                    setContactList(((List<Contact>) ((Result.Success) result).data));
                }
            }
        });

    }



}