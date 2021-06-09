package com.nirwal.gailconnect.ui.login;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.MultiAutoCompleteTextView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.nirwal.gailconnect.AccountAuth.MyAccountManager;
import com.nirwal.gailconnect.MyApp;
import com.nirwal.gailconnect.services.WebServiceApi;
import com.nirwal.gailconnect.modal.AuthResponse;
import com.nirwal.gailconnect.modal.DeviceProperties;
import com.nirwal.gailconnect.modal.User;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginViewModel extends ViewModel {
    private static final String TAG = "LoginViewModel";

    private final WebServiceApi _webServiceApi = new Retrofit.Builder()
                    .baseUrl(MyApp.URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(WebServiceApi.class);

    private final MutableLiveData<AuthResponse> _authResponse = new MutableLiveData<>();
    private final MutableLiveData<String> _errorMsg = new MutableLiveData<>();
    private String _userName, _password;

    public LiveData<AuthResponse> get_authResponse(){
        return _authResponse;
    }

    public LiveData<String> get_errorMsg(){
        return _errorMsg;
    }

    public void loginUsingWebService(String username, String pass){

        User user = new User(
                username,
                pass,
                "",
                "",
                new DeviceProperties(
                        "",
                        "",
                        "",
                        "",
                        "",
                        ""
                ));

//        Gson gson =  new Gson();
//        Log.d(TAG, "GSON: "+ gson.toJson(user));
//        Log.d(TAG, "loginUsingWebService: "+user.getUserid()+":"+user.getPassword());

        Call<List<AuthResponse>> repo = _webServiceApi.isValidUserData(user);
        repo.enqueue(new Callback<List<AuthResponse>>() {
            @Override
            public void onResponse(@NotNull Call<List<AuthResponse>> call, @NotNull Response<List<AuthResponse>> response) {


                    if (response.code() > 199 && response.code() < 300) {

                        AuthResponse authResponse = response.body().get(0);

                        //true i case of success
                        if(authResponse.isResponse()){
                            _authResponse.postValue(authResponse);
                            Log.d(TAG, "onResponse: Login: Success");
                        }else {
                            _authResponse.postValue(null);
                            _errorMsg.postValue("Invalid credential");
                            Log.d(TAG, "onResponse: Login:Invalid credential");
                        }
                        return;
                    }
                    Log.d(TAG, "onResponse: LoginError: "+response.toString());
                    _errorMsg.postValue("LoginError:"+response.toString());

            }

            @Override
            public void onFailure(Call<List<AuthResponse> >call, Throwable t) {
                    _errorMsg.postValue("onFailure: LoginError: "+t.getMessage());
                    Log.d(TAG, "onFailure: LoginError: "+t.getMessage());

            }
        });
    }


    public String getUserName() {
        return _userName;
    }

    public void setUserName(String userName) {
        this._userName= userName;
    }

    public String getPassword() {
        return _password;
    }

    public void setPassword(String password) {
        this._password = password;
    }


}