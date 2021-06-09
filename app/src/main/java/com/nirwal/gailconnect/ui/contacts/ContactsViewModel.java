package com.nirwal.gailconnect.ui.contacts;

import android.accounts.Account;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.TextView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.nirwal.gailconnect.AccountAuth.MyAccountManager;
import com.nirwal.gailconnect.Constants;
import com.nirwal.gailconnect.ContactsManager;
import com.nirwal.gailconnect.MyApp;
import com.nirwal.gailconnect.databse.ContactRepository;
import com.nirwal.gailconnect.modal.Contact;
import com.nirwal.gailconnect.tasks.ImageLoader;
import com.nirwal.gailconnect.tasks.Result;
import com.nirwal.gailconnect.tasks.TaskCallback;

import java.util.ArrayList;
import java.util.List;

public class ContactsViewModel extends ViewModel {
    private static final String TAG = "ContactsViewModel";

    private MyApp _app;
    private ContactRepository _contactRepository;
    public MutableLiveData<List<Contact>> _contactListLiveData =new MutableLiveData<>();
    private final List<Contact> _tempContactList = new ArrayList<>();

    private int _filterOption;
    private String _searchQuery = "";
    private final TaskCallback<List<Contact>> _callback=  new TaskCallback<List<Contact>>() {
        @Override
        public void onComplete(Result result) {
            Log.d(TAG, "onComplete: "+result.toString());
            if(result instanceof Result.Success){

                _tempContactList.clear();
                _tempContactList.addAll(((Result.Success<List<Contact>>)result).data);

                updateContacts();

            }else {
                // show error on ui
                //showSnakebar("Error loading contact");
            }
        }
    };

    public LiveData<List<Contact>> getContactListLiveData() {
        return _contactListLiveData;
    }

    public void performSearch(String query){
        //Log.d(TAG, "performSearch: "+query);
        this._searchQuery=query;
        updateContacts();
    }

    public void setFilter(int filterOption){
        _searchQuery = "";
        _filterOption = filterOption;
      //  Log.d(TAG, "setFilter: "+filterOption);
    }
    private final List<Contact> _temList = new ArrayList<>();
    private void updateContacts(){
        //Log.d(TAG, "updateContact: "+_searchQuery);
        //Log.d(TAG, "updateContact: _tempContList.size "+_tempContactList.size());
        if(_searchQuery.trim().isEmpty()){
            _contactListLiveData.postValue(_tempContactList);
            return;
        }

        _app.executorService.execute(new Runnable() {
            @Override
            public void run() {
                _temList.clear();
                for (Contact contact : _tempContactList){
                    if(contact.getEmp_Name().toUpperCase().contains(_searchQuery))
                        _temList.add(contact);
                }
                _app.mainThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        _contactListLiveData.postValue(_temList);
                    }
                });
            }
        });

        //  Log.d(TAG, "updateContact: "+temList.size());


    }

    public void loadContact(Application application){

        if(_contactRepository==null){
            _app = (MyApp)application;
            _contactRepository =  new ContactRepository(_app);
        }

        SharedPreferences spref= application.getSharedPreferences(MyApp.SHEARED_PREF,Context.MODE_PRIVATE);

        String location = spref.getString(Constants.USER_WORK_LOCATION,"*");
        String department = spref.getString(Constants.USER_DEPARTMENT,"*");

        //Log.d(TAG, "loadContact: loc: "+location+" dep:"+department);

        if(location.equals("*") || department.equals("*")){
            fetchEmployLocAndDept(spref);
          //  Log.d(TAG, "loadContact: loc: "+location+" dep:"+department);
        }

        switch (_filterOption){
            case Constants.FILTER_BY_LOCATION_AND_DEPARTMENT:{
                _contactRepository.loadContactByLocationAndDept(location,department,_callback);
                break;
            }

            case Constants.FILTER_BY_LOCATION:{

                _contactRepository.loadContactByLocation(location,_callback);
                break;
            }

            case Constants.FILTER_NO_FILTER:{
                _contactRepository.getAll(_callback);
                break;
            }
        }
    }

    private void fetchEmployLocAndDept(SharedPreferences shredPref){
        String cpf = shredPref.getString(Constants.USER_CPF,"");
        _contactRepository.getContactByCPF(cpf, new TaskCallback<Contact>() {
            @Override
            public void onComplete(Result result) {
                if(result instanceof Result.Success){
                    Contact contact =  ((Result.Success<Contact>) result).data;

                    if(contact==null)return;

                    shredPref.edit().putString(Constants.USER_DEPARTMENT,contact.getDepartment())
                            .putString(Constants.USER_WORK_LOCATION,contact.getLocation())
                            .apply();
                }
            }
        });
    }

    public void  exportContacts(Application application, IonExportListener listener){
        MyApp app = (MyApp) application;
        List<Contact> contacts = _contactListLiveData.getValue();
        Account account = MyAccountManager.getAccount(app);
        if(contacts.size()>0){
            app.executorService.execute(()->{
                ImageLoader imageLoader = new ImageLoader(app);

                postUpdateOnUi(app, listener, "Preparing contacts");
                for(Contact contact : contacts) {
                    String url = MyApp.IMG_URL + contact.getIMAGE();
                    if (!imageLoader.isImageCached(url)){
                        Bitmap bitmap = imageLoader.getImage(url);
                        imageLoader.putImageInCache(imageLoader.getFileNameFromUrl(url),bitmap);
                    }
                }

                ContactsManager.deleteAllContacts(app,account);

                for (Contact contact : contacts){
                    ContactsManager.addContact(app, account,contact);
                    postUpdateOnUi(app, listener, "Exporting: "+contact.getEmp_Name());
                }

                app.mainThreadHandler.post(listener::onComplete);

            });


        }

    }

    private void postUpdateOnUi(MyApp app, IonExportListener listener, String msg){
        app.mainThreadHandler.post(()-> listener.onProgressUpdate(msg));
    }

    interface IonExportListener{
        void onProgressUpdate(String progTxt);
        void onComplete();
    }

}