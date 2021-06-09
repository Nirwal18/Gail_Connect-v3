package com.nirwal.gailconnect.databse;

import android.app.Application;
import android.os.Handler;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.nirwal.gailconnect.MyApp;
import com.nirwal.gailconnect.modal.Contact;
import com.nirwal.gailconnect.modal.Office;
import com.nirwal.gailconnect.tasks.Result;
import com.nirwal.gailconnect.tasks.TaskCallback;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class OfficeRepository extends Repository<Office>{
    private static final String TAG = "OfficeRepository";

    private final OfficeDao _officeDao;
    private final MyApp _app = getApplication();

    public OfficeRepository(Application application){
        super(application);
        AppDatabase database = AppDatabase.getInstance(application);
        _officeDao = database.officeDao();
    }

    public LiveData<List<Office>> getAll(){
    return _officeDao.getAll();
    }



    public void insertAllOffice(TaskCallback<Result> callback, Office... offices){
        _app.executorService.execute(() -> {
            _officeDao.insertAll(offices);
            Result result = new Result.Success<Boolean>(true);
            postResult(callback,result);
        });
    }


    public void deleteAllOffice(){
        _app.executorService.execute(_officeDao::deleteAll);
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
    public void insert(Office office) {
        _officeDao.insert(office);
    }

    @Override
    public void insertAll(Office... offices) {
        _officeDao.insertAll(offices);
    }

    @Override
    public void delete(Office obj) {
        _officeDao.delete(obj);
    }

    @Override
    public void deleteAll() {
        _officeDao.deleteAll();
    }

    @Override
    public Call<List<Office>> getWebRepository() {
        return  _app.get_serviceApi().getOffices();
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
