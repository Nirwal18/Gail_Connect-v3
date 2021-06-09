package com.nirwal.gailconnect.databse;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.nirwal.gailconnect.MyApp;
import com.nirwal.gailconnect.R;
import com.nirwal.gailconnect.modal.Contact;
import com.nirwal.gailconnect.tasks.Result;
import com.nirwal.gailconnect.tasks.TaskCallback;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class  ContactRepository extends Repository<Contact>{
    private static final String TAG = "ContactRepository";
    private final ContactDao _contactDao;
    private final MyApp _app = getApplication();

    public ContactRepository(Application application){
        super(application);
        AppDatabase database = AppDatabase.getInstance(_app);
        _contactDao = database.contactDao();
    }


    public List<Contact> getAll(){
        return _contactDao.getAll();
    }


    public void insertAllContact(TaskCallback<Result> callback, Contact... contacts){
        _app.executorService.execute(() -> {
            _contactDao.insertAll(contacts);
            Result result = new Result.Success<Boolean>(true);
            //callback.onComplete(result);
            postResult(callback, result);
        });
    }

    public void deleteAllContact(){
        _app.executorService.execute(_contactDao::deleteAll);
    }

    public void getAll(TaskCallback<List<Contact>> callback){
        Log.d(TAG, "getAll(): ");
        _app.executorService.execute(new Runnable() {
            @Override
            public void run() {
                List<Contact> contacts = _contactDao.getAll();
                Log.d(TAG, "getAll(): size:"+contacts.size());
                postResult(callback,new Result.Success<>(contacts));
            }
        });
    }

    public void loadContactByLocation(String location, TaskCallback<List<Contact>> callback){
        _app.executorService.execute(new Runnable() {
            @Override
            public void run() {
                postResult(callback, new Result.Success<>(_contactDao.loadContactByLocation(location)));
            }
        });
    }

    public void loadContactByLocationAndDept(String location, String dept, TaskCallback<List<Contact>> callback){
        _app.executorService.execute(new Runnable() {
            @Override
            public void run() {
                postResult(callback, new Result.Success<>(_contactDao.loadContactByLocationAndDept(location,dept)));
            }
        });
    }

    public void getContactByCPF(String cpf, TaskCallback<Contact> callback){
        _app.executorService.execute(new Runnable() {
            @Override
            public void run() {
                postResult(callback, new Result.Success<>(_contactDao.getContactByCPF(cpf)));
            }
        });
    }



    public void loadContactByName(String name, TaskCallback<List<Contact>> callback){
        _app.executorService.execute(new Runnable() {
            @Override
            public void run() {
                postResult(callback, new Result.Success<>(_contactDao.getContactByName(name)));
            }
        });
    }


    public void loadContactByDOB(String dob, TaskCallback<List<Contact>> callback){
        _app.executorService.execute(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: "+dob);
                postResult(callback, new Result.Success<>(_contactDao.getContactByDOB(dob)));
            }
        });
    }


    public List<Contact> loadContactByDOB(String dob){
        return _contactDao.getContactByDOB(dob);
    }


    private void postResult(TaskCallback<?> callback, Result data){
        _app.mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onComplete(data);
            }
        });
    }



    @Override
    public void insert(Contact contact) {
        _contactDao.insert(contact);
    }

    @Override
    public void insertAll(Contact... contacts) {
        _contactDao.insertAll(contacts);
    }

    @Override
    public void delete(Contact contact) {
        _contactDao.delete(contact);
    }

    @Override
    public void deleteAll() {
        _contactDao.deleteAll();
    }

    @Override
    public Call<List<Contact>> getWebRepository() {
        return _app.get_serviceApi().getContacts();
    }

    @Override
    public void performSync(Handler handler, ProgressUpdater callback, String tag) {
    sync(
            getWebRepository(),
            handler,
            callback,
            tag
    );
    }


}

