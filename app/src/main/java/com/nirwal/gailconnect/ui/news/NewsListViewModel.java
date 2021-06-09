package com.nirwal.gailconnect.ui.news;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.nirwal.gailconnect.MyApp;
import com.nirwal.gailconnect.modal.News;

import java.util.ArrayList;
import java.util.FormatFlagsConversionMismatchException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsListViewModel extends ViewModel {
    private static final String TAG = "NewsListViewModel";


    // TODO: Implement the ViewModel
    private final MutableLiveData<List<News>> _newsList = new MutableLiveData<>();
    private final MutableLiveData<Boolean> _isError = new MutableLiveData<>();
    private String _errorMsg;


    public MutableLiveData<List<News>> get_newsList(Application app, int mode) {
        if(_newsList.getValue()==null){
            fetchNewsFromWebApi((MyApp) app, mode);
        }
        return _newsList;
    }

    public MutableLiveData<Boolean> get_isError() {
        return _isError;
    }

    public String get_errorMsg() {
        return _errorMsg;
    }

    private void fetchNewsFromWebApi(MyApp app, int mode){

        switch (mode){
            case NewsListFragment.MODE_GAIL_NEWS:{
                fetchGailNews(app);
                break;
            }
            case NewsListFragment.MODE_INDUSTRY_NEWS:{
                fetchIndustry(app);
                break;
            }
        }

    }



    private void fetchGailNews(MyApp app){
        app.get_serviceApi().getNewsGailList().enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonArray jsonArray =  response.body().getAsJsonArray("data");
                Gson gson = new Gson();

                List<News> newsList = new ArrayList<>();

                for (JsonElement element :jsonArray){
                    newsList.add(gson.fromJson(element, News.class));
                }


                _newsList.postValue(newsList);
                Log.d(TAG, "onResponse: "+response.code()+" "+response.message());

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
                _errorMsg = t.getMessage();
                _isError.setValue(true);
            }
        });
    }

    private void fetchIndustry(MyApp app){
        app.get_serviceApi().getNewsIndustryList().enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonArray jsonArray =  response.body().getAsJsonArray("data");
                Gson gson = new Gson();

                List<News> newsList = new ArrayList<>();

                for (JsonElement element :jsonArray){
                    newsList.add(gson.fromJson(element, News.class));
                }
                _newsList.postValue(newsList);


            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
                _errorMsg = t.getMessage();
                _isError.setValue(true);

            }
        });
    }
}