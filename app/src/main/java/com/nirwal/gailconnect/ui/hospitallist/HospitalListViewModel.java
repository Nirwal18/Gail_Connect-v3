package com.nirwal.gailconnect.ui.hospitallist;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.nirwal.gailconnect.MyApp;
import com.nirwal.gailconnect.modal.Hospital;
import com.nirwal.gailconnect.modal.News;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HospitalListViewModel extends ViewModel {
    private static final String TAG = "HospitalListViewModel";

    private MutableLiveData<List<Hospital>> _hospitalList;
    private MutableLiveData<List<String>> _cityList;

    public HospitalListViewModel(){
        _cityList = new MutableLiveData<>();
    }

    public MutableLiveData<List<Hospital>> get_hospitalList(Application app) {
        if(_hospitalList==null){
            _hospitalList = new MutableLiveData<>();
            fetchHospitalList((MyApp) app);
        }
        return _hospitalList;
    }

    private void fetchHospitalList(MyApp app){
        app.get_serviceApi().getHospitals().enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

                JsonObject jsonObject =  response.body().get(0)
                        .getAsJsonObject();

                JsonArray hospitalJsonArray = jsonObject
                        .getAsJsonArray("lst_Hospital_list");

                JsonArray cityJsonArray = jsonObject
                        .getAsJsonArray("lst_State_all");

                Gson gson = new Gson();

                List<Hospital> hospitalsList = new ArrayList<>();
                List<String> cityList = new ArrayList<>();

                for (JsonElement element :hospitalJsonArray){
                    hospitalsList.add(gson.fromJson(element, Hospital.class));
                }



                for (JsonElement element : cityJsonArray){
                    cityList.add(element.getAsJsonObject().get("STATE_DESCRIPTION").toString());
                   // Log.d(TAG, "onResponse: city: "+element.getAsJsonObject().get("STATE_DESCRIPTION").toString());
                }
                _hospitalList.setValue(hospitalsList);
                _cityList.setValue(cityList);

            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
    }



}