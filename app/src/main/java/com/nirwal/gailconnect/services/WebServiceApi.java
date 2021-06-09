package com.nirwal.gailconnect.services;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import com.nirwal.gailconnect.modal.AuthResponse;
import com.nirwal.gailconnect.modal.Contact;
import com.nirwal.gailconnect.modal.Hospital;
import com.nirwal.gailconnect.modal.Location;
import com.nirwal.gailconnect.modal.News;
import com.nirwal.gailconnect.modal.Office;
import com.nirwal.gailconnect.modal.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface WebServiceApi {

    // Url : https://gailebank.gail.co.in/webservices/contactinfo/pipelineinfoservice.svc/

    @GET("GetTelInfoUsingOracle")
    Call<List<Contact>> getContacts();

    @GET("GetTEL_SUBLOCATION_DETAIL")
    Call<List<Location>> getLocations();

    @GET("GetGailCodesUsingOracle")
    Call<List<Office>> getOffices();


    @Headers({ "Content-Type: application/json; charset=utf-8"})
    @POST("IsValidUserData")
    Call<List<AuthResponse>> isValidUserData(@Body User user);


    @GET("https://gailebank.gail.co.in/WebServices/MYGAIL/Service1.svc/Get_MOBILE_GAIL_HOSPITAL_MASTER")
    Call<JsonArray> getHospitals();

    @GET("https://gailebank.gail.co.in/GailconnectLiveEvents/api/liveevents/getgailnewsbycategory/G")
    Call<JsonObject> getNewsGailList();

    @GET("https://gailebank.gail.co.in/GailconnectLiveEvents/api/liveevents/getgailnewsbycategory/I")
    Call<JsonObject> getNewsIndustryList();

    @GET("https://gailebank.gail.co.in/GailconnectLiveEvents/api/liveevents/GuestHouseDetails")
    Call<JsonObject> getGuestHouseList();
}
