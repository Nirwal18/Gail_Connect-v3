package com.nirwal.gailconnect.ui.web;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class WebViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private MutableLiveData<String> _url;

    public WebViewModel() {
        _url = new MutableLiveData<>();
    }

    public MutableLiveData<String> get_url() {
        return _url;
    }

    public void set_url(String url) {
        this._url.setValue(url);
    }
}