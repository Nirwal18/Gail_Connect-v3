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
import com.nirwal.gailconnect.modal.Office;

import java.util.List;

public class OfficeListAdaptor extends RecyclerView.Adapter<OfficeListAdaptor.OfficeViewHolder> {
    private static final String TAG = "OfficeListAdaptor";
    public static final int ACTION_CALL = 1;
    public static final int ACTION_OPEN_MAP = 2;
    private final List<Office> _officeList;
    private final IOnClickListener _listener;


    public OfficeListAdaptor(List<Office> listData,IOnClickListener listener) {
    this._officeList =  listData;
    this._listener = listener;
    }

    @NonNull
    @Override
    public OfficeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new OfficeViewHolder(layoutInflater,parent, _listener);
    }

    @Override
    public void onBindViewHolder(@NonNull OfficeViewHolder holder, int position) {
        Office office =  _officeList.get(position);

        holder.office = office;
        holder.title.setText(office.getLocation());
        holder.initial.setText(String.valueOf(office.getLocation().trim().charAt(0)));
        holder.gailnetCode.setText("Gailnet code: "+office.getGailnetCode());

        holder.container.setHasTransientState(true);

    }

    @Override
    public int getItemCount() {
        return _officeList.size();
    }

    public void updateDataSet(List<Office> data){
        _officeList.clear();
        _officeList.addAll(data);
        notifyDataSetChanged();
    }
    

    public interface IOnClickListener{
        void startCall(String mobileNo);
        void openMapUsingCordinate(String longitude, String latitude);
        void openMayUsingSearchTxt(String searchTxt);
    }



    public static class OfficeViewHolder extends RecyclerView.ViewHolder {
        public TextView title, initial, gailnetCode;
        public LinearLayout container;
        public Button dropdownBtn;
        public Office office;
        private final IOnClickListener _listener;
        private final LayoutInflater _layoutInflater;

        public OfficeViewHolder(@NonNull LayoutInflater layoutInflater, ViewGroup parent, IOnClickListener listener) {
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

            if(!office.getAddress().trim().isEmpty()){
                addMapItem("Address",office.getAddress(),"","");
            }

            if(!office.getLongitude().trim().isEmpty() && !office.getLatitude().trim().isEmpty()){
                addMapItem(
                        "Coordinates",
                        "Log: "+office.getLongitude() +", Lat: "+office.getLatitude(),
                        office.getLongitude(), office.getLatitude());
            }

            if(!office.getEPABX().trim().isEmpty()){
                String[] phones = office.getEPABX().split(",");
                for(int i=0; i<phones.length; i++){
                    addPhoneItemView( "EPABX"+(i+1),phones[i] );
                }
            }

            if(!office.getFax().trim().isEmpty()){
                addTextItem("FAX",office.getFax());
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
