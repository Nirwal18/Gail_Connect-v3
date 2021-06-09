package com.nirwal.gailconnect.AccountAuth;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.nirwal.gailconnect.MyApp;
import com.nirwal.gailconnect.modal.Contact;
import com.nirwal.gailconnect.tasks.MyWorker;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class SyncAdapter extends AbstractThreadedSyncAdapter {
    private static final String TAG = "SyncAdapter";
    private AccountManager mAccountManager;
    private WeakReference<MyApp>  _myAppWeakRef;

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mAccountManager = AccountManager.get(context);
        _myAppWeakRef = new WeakReference<>((MyApp)context.getApplicationContext());
    }

    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        mAccountManager = AccountManager.get(context);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras,
                              String authority, ContentProviderClient provider,
                              SyncResult syncResult) {

        Log.d(TAG, "onPerformSync: " +"called");
       // syncDataFromWebRepository();

//        try {
//            String authToken = mAccountManager.blockingGetAuthToken(account,
//                    AccountConstant.AUTH_TOKEN_TYPE, true);
//            // Use the authToken and write your sync logic. Skip the previous call if authToken is not required
//
//            Log.d(TAG, "onPerformSync: " +"called");
//
//        } catch (OperationCanceledException | AuthenticatorException | IOException e) {
//            e.printStackTrace();
//        }
        //syncContacts();
    }


    @Override
    public void onSyncCanceled() {
        super.onSyncCanceled();
        MyApp app= _myAppWeakRef.get();
     //   app.executorService.shutdown();
        _myAppWeakRef.clear();
        _myAppWeakRef=null;
    }


    private void syncContacts(){
        MyApp app = _myAppWeakRef.get();
//        new ContactSyncToAccountTask(app.executorService, app.mainThreadHandler)
//                .syncContacts(getContext(), getContactList(), new TaskCallback() {
//                    @Override
//                    public void onComplete(Result result) {
//                        Log.d(TAG, "onComplete: Contact Sync Completed");
//                    }
//                });
    }

    private List<Contact> getContactList(){
        //ContactRepository contactRepository =  new ContactRepository(_myAppWeakRef.get());
        //if(contactRepository.getAll())
        Toast.makeText(getContext(),"not implemented yet",Toast.LENGTH_LONG).show();
        return new ArrayList<>();
    }

    private void syncDataFromWebRepository(){
        Log.d(TAG, "syncDataFromWebRepository: called");
        WorkRequest workRequest = OneTimeWorkRequest.from(MyWorker.class);
        WorkManager.getInstance(getContext()).enqueue(workRequest);
    }
}