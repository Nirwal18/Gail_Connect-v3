package com.nirwal.gailconnect.ui.offices;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.nirwal.gailconnect.databse.OfficeRepository;
import com.nirwal.gailconnect.modal.Office;

import java.util.List;

public class OfficeListViewModel extends ViewModel {
    // TODO: Implement the
    private LiveData<List<Office>> _officeList;
    private OfficeRepository _repository;

    public OfficeListViewModel(){

    }

    public LiveData<List<Office>> get_officeList(Application app) {

        if(_officeList==null){
            if(_repository==null){
                _repository= new OfficeRepository(app);
            }

           _officeList = _repository.getAll();
        }


        return _officeList;
    }





}