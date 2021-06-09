package com.nirwal.gailconnect.AccountAuth;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.nirwal.gailconnect.BuildConfig;
import com.nirwal.gailconnect.R;

public class MyAccountManager {
    private static final String TAG = "MyAccountManager";

    public static final String user_name = "user_name";
    public static final String user_pass = "user_pass";
    public static final String auth_token_type = "token_full_access";
    public static final String token_full_access = "full_access";


    public static boolean AddAccount(Context context, String userName, String passWord){
        Log.d(TAG, "AddAccount: called");
       Account currentAccount = new Account(userName,context.getResources().getString(R.string.account_type));

        AccountManager am = AccountManager.get(context);

        Bundle userData =  new Bundle();
        userData.putString(user_name, userName);
        userData.putString(user_pass, passWord);

        boolean result = am.addAccountExplicitly(currentAccount,passWord,userData);
        am.setAuthToken(currentAccount,auth_token_type,token_full_access);

        return result;
    }


    public static void removeAccount(Activity context, @Nullable AccountManagerCallback<Bundle> callback){
        AccountManager accountManager = AccountManager.get(context);

        // loop through all accounts to remove them
        Account[] accounts = accountManager.getAccounts();
        for (Account account : accounts) {
            if (account.type.intern().equals(BuildConfig.APPLICATION_ID))
                accountManager.removeAccount(account,context, callback, null);
        }
    }


    public static Account getAccount(Context context){
        return AccountManager.get(context).getAccounts()[0];
    }

    public static AccountManager getManager(Activity activity){
            return AccountManager.get(activity);
    }



}
