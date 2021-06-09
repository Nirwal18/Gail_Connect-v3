package com.nirwal.gailconnect.ui.offices;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.nirwal.gailconnect.databse.LocationRepository;
import com.nirwal.gailconnect.databse.OfficeRepository;
import com.nirwal.gailconnect.modal.Location;
import com.nirwal.gailconnect.modal.Office;

import java.util.List;

public class ControlRoomListViewModel extends ViewModel {
    // TODO: Implement the ViewModel


    private LiveData<List<Location>> _locationList;
    private LocationRepository _repository;


    public LiveData<List<Location>> get_locationList(Application app) {

        if(_locationList==null){
            if(_repository==null){
                _repository= new LocationRepository(app);
            }

            _locationList = _repository.getAll();
        }


        return _locationList;
    }


}