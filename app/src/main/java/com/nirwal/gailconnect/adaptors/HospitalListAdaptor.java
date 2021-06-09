package com.nirwal.gailconnect.adaptors;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import com.nirwal.gailconnect.R;
import com.nirwal.gailconnect.modal.Hospital;

import java.util.List;

public class HospitalListAdaptor extends RecyclerView.Adapter<HospitalListAdaptor.HospitalViewHolder> {
    private static final String TAG = "HospitalListAdaptor";
        
        private final List<Hospital> _hospitalList;
        private final IOnClickListener _listener;


        public HospitalListAdaptor(List<Hospital> listData, IOnClickListener listener) {
            this._hospitalList =  listData;
            this._listener = listener;
        }

        @NonNull
        @Override
        public HospitalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            return new HospitalViewHolder(layoutInflater,parent,_listener);
        }

        @Override
        public void onBindViewHolder(@NonNull HospitalViewHolder holder, int position) {
            Hospital hospital = _hospitalList.get(position);
            holder.hospital = hospital;
            holder.title.setText(hospital.HOSPITAL_NAME);
            holder.initial.setText(String.valueOf(hospital.HOSPITAL_NAME.trim().charAt(0)));
            holder.gailnetCode.setText("Location: "+ hospital.HOSPITAL_LOC);
            holder.container.setHasTransientState(true);

        }

        @Override
        public int getItemCount() {
            return _hospitalList.size();
        }

        public void updateDataSet(List<Hospital> data){
            _hospitalList.clear();
            _hospitalList.addAll(data);
            notifyDataSetChanged();
        }

        public interface IOnClickListener{
            void openMapUsingCordinate(String longitude, String latitude);
            void openMayUsingSearchTxt(String searchTxt);
        }



    public static class HospitalViewHolder extends RecyclerView.ViewHolder {
        public TextView title, initial, gailnetCode;
        public LinearLayout container;
        public Button dropdownBtn;
        public Hospital hospital;
        private final IOnClickListener _listener;
        private final LayoutInflater _layoutInflater;

        public HospitalViewHolder(@NonNull LayoutInflater layoutInflater, ViewGroup parent, IOnClickListener listener) {
            super(layoutInflater.inflate(R.layout.item_expandable_card,parent,false));
            _layoutInflater = layoutInflater;
            _listener = listener;
            container = itemView.findViewById(R.id.list_container);
            title = itemView.findViewById(R.id.txt_office_title);
            initial = itemView.findViewById(R.id.txt_office_title_initial);
            gailnetCode = itemView.findViewById(R.id.txt_gail_net_code);
            dropdownBtn = itemView.findViewById(R.id.btn_dropdown);

            dropdownBtn.setOnClickListener(v -> {
                animateDropDown();
                if(container.getChildCount()<=0){
                    updateUi();
                }
            });
            itemView.setOnClickListener(v -> {
                animateDropDown();
            });
        }

        private void animateDropDown(){
            if(container.getVisibility()==View.GONE){
                TransitionManager.beginDelayedTransition((ViewGroup) itemView,new AutoTransition());
                container.setVisibility(View.VISIBLE);
                dropdownBtn.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
            }else {
                TransitionManager.beginDelayedTransition((ViewGroup) itemView,new AutoTransition());
                container.setVisibility(View.GONE);
                dropdownBtn.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
            }
        }

        private void updateUi(){

                        if(!hospital.HOSPITAL_ADD.trim().isEmpty()){
                addMapItem("Address",hospital.HOSPITAL_ADD,"","");
            }

            if(!hospital.Longitude.trim().isEmpty() && !hospital.Latitude.trim().isEmpty()){
                addMapItem(
                        "Coordinates",
                        "Log: "+hospital.Longitude +", Lat: "+hospital.Latitude,
                        hospital.Longitude, hospital.Latitude);
            }


        }

        private void addMapItem( String title, String address, String longi, String lati){
            View v = _layoutInflater.inflate(R.layout.item_phone, container, false);



            ((TextView)v.findViewById(R.id.data_title)).setText(title);
            ((TextView)v.findViewById(R.id.data_data)).setText(address);
            v.findViewById(R.id.btn_call).setVisibility(View.GONE);
            ImageView imageView = v.findViewById(R.id.btn_msg);
            imageView.setImageResource(R.drawable.map_24dp);
            imageView.setOnClickListener(v1 -> {

                if(!longi.isEmpty() && !lati.isEmpty()){
                    _listener.openMapUsingCordinate(longi,lati);
                } else{
                    _listener.openMayUsingSearchTxt(address);
                }
            });

            container.addView(v);
        }

    }


}
