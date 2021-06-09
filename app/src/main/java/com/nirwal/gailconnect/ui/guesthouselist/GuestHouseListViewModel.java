package com.nirwal.gailconnect.ui.guesthouselist;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.nirwal.gailconnect.MyApp;
import com.nirwal.gailconnect.modal.GuestHouse;
import com.nirwal.gailconnect.modal.News;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GuestHouseListViewModel extends ViewModel {
    private static final String TAG = "GuestHouseListViewModel";
    private MutableLiveData<List<GuestHouse>> _guestHouseList;


    public MutableLiveData<List<GuestHouse>> get_guestHouseList(Application app) {
        if(_guestHouseList==null){
            _guestHouseList= new MutableLiveData<>(new ArrayList<>());
            fetchGailGuestHouse((MyApp) app);

        }
        return _guestHouseList;
    }


    private void fetchGailGuestHouse(MyApp app){
        app.get_serviceApi().getGuestHouseList().enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonArray jsonArray =  response.body().getAsJsonArray("data");
                Gson gson = new Gson();

                List<GuestHouse> GuestHouseList = new ArrayList<>();

                for (JsonElement element :jsonArray){
                    GuestHouseList.add(gson.fromJson(element, GuestHouse.class));
                }


                _guestHouseList.setValue(GuestHouseList);
                Log.d(TAG, "onResponse: "+response.code()+" "+response.message());

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
                //_errorMsg = t.getMessage();
                //_isError.setValue(true);
            }
        });
    }
}