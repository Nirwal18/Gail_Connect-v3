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
import com.nirwal.gailconnect.modal.GuestHouse;
import com.nirwal.gailconnect.modal.Office;

import java.util.List;

public class GuestHouseListAdaptor extends RecyclerView.Adapter<GuestHouseListAdaptor.GuestHouseViewHolder> {
    private static final String TAG = "GuestHouseListAdaptor";
    private List<GuestHouse> _guestHouseList;
    private LayoutInflater _layoutInflater;
    private IOnClickListener _listener;


        public GuestHouseListAdaptor(List<GuestHouse> listData, IOnClickListener listener) {
            this._guestHouseList =  listData;
            this._listener = listener;

        }

        @NonNull
        @Override
        public GuestHouseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            _layoutInflater = LayoutInflater.from(parent.getContext());
            return new GuestHouseViewHolder(_layoutInflater,parent,_listener);
        }

        @Override
        public void onBindViewHolder(@NonNull GuestHouseViewHolder holder, int position) {
            GuestHouse guestHouse =  _guestHouseList.get(position);

            holder.guestHouse = guestHouse;
            holder.title.setText(guestHouse.location);
            holder.initial.setText(String.valueOf(guestHouse.location.trim().charAt(0)));
            holder.gailnetCode.setText("HVJ :"+guestHouse.hvj);

            holder.container.setHasTransientState(true);

        }

        @Override
        public int getItemCount() {
            return _guestHouseList.size();
        }

        public void updateDataSet(List<GuestHouse> data){
            _guestHouseList.clear();
            _guestHouseList.addAll(data);
            notifyDataSetChanged();
        }


    public interface IOnClickListener{
        void startCall(String mobileNo);
        void openMapUsingCordinate(String longitude, String latitude);
        void openMayUsingSearchTxt(String searchTxt);
    }



    public static class GuestHouseViewHolder extends RecyclerView.ViewHolder {
        public TextView title, initial, gailnetCode;
        public LinearLayout container;
        public Button dropdownBtn;
        public GuestHouse guestHouse;
        private final IOnClickListener _listener;
        private final LayoutInflater _layoutInflater;

        public GuestHouseViewHolder(@NonNull LayoutInflater layoutInflater, ViewGroup parent,
                                    IOnClickListener listener) {
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

            if(!guestHouse.address.trim().isEmpty()){
                addMapItem("Address",guestHouse.address, "","");
            }

            if(!guestHouse.address.trim().isEmpty()){
                addMapItem("Coordinate","Lat:"+guestHouse.latitude+ ", Log:"+guestHouse.longitude,
                        guestHouse.latitude, guestHouse.longitude);
            }


            if(guestHouse.telephone!=null && !guestHouse.telephone.trim().isEmpty()){
                addPhoneItemView( "Telephone",guestHouse.telephone );
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

        private void addPhoneItemView(String title, String data){
            View v= _layoutInflater.inflate(R.layout.item_phone,container, false);
            ((TextView)v.findViewById(R.id.data_title)).setText(title);
            ((TextView)v.findViewById(R.id.data_data)).setText(data);
            v.findViewById(R.id.btn_call).setVisibility(View.GONE);
            ImageView imageView = v.findViewById(R.id.btn_msg);
            imageView.setImageResource(R.drawable.ic_baseline_phone_24);

            imageView.setOnClickListener(v1 -> _listener.startCall(data));

            container.addView(v);
        }

        private void addTextItem(String title, String data){
            View v= _layoutInflater.inflate(R.layout.item_phone,container, false);

            ((TextView)v.findViewById(R.id.data_title)).setText(title);
            ((TextView)v.findViewById(R.id.data_data)).setText(data);

            v.findViewById(R.id.btn_call).setVisibility(View.GONE);
            v.findViewById(R.id.btn_msg).setVisibility(View.GONE);

            container.addView(v);
        }


    }



}


