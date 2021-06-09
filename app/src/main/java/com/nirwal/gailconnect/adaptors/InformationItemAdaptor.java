package com.nirwal.gailconnect.adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.nirwal.gailconnect.R;
import com.nirwal.gailconnect.modal.Contact;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public abstract class InformationItemAdaptor extends ArrayAdapter<List<HashMap<String, String>>> {

    List<HashMap<String, String>> _dataList;
    public InformationItemAdaptor(@NonNull Context context, List<HashMap<String, String>> data) {
        super(context, R.layout.item_phone);
        this._dataList = data;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        HashMap<String,String> map = _dataList.get(position);

        if(view==null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_phone,parent,false);
        }


        TextView title = view.findViewById(R.id.data_title);
        TextView data = view.findViewById(R.id.data_data);
        title.setText(map.get("title"));
        data.setText(map.get("data"));
        return view;
    }

    @Override
    public int getCount() {
        return _dataList.size();
    }

    public void updateDataSet(List<HashMap<String,String>> data){
        _dataList.clear();
        _dataList.addAll(data);
        notifyDataSetChanged();
        onUpdate();
    }

    abstract void onUpdate();

}
