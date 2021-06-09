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
import com.nirwal.gailconnect.modal.Location;
import com.nirwal.gailconnect.modal.Office;

import java.util.List;

public class LocationAdaptor extends RecyclerView.Adapter<LocationAdaptor.LocationViewHolder> {
    private static final String TAG = "OfficeListAdaptor";
    public static final int ACTION_CALL = 1;
    public static final int ACTION_OPEN_MAP = 2;
    private List<Location> _locationList;
    private LayoutInflater _layoutInflater;
    private IOnClickListener _listener;


    public LocationAdaptor(List<Location> listData,IOnClickListener _listener) {
        this._locationList =  listData;
        this._listener = _listener;
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        _layoutInflater = LayoutInflater.from(parent.getContext());
        return new LocationViewHolder(_layoutInflater,parent,_listener);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        Location location =  _locationList.get(position);

        holder.location = location;
        holder.title.setText(location.getLOCATION());
        holder.initial.setText(String.valueOf(location.getLOCATION().trim().charAt(0)));
        holder.gailnetCode.setText(location.getSUBLOCATION());
        holder.container.setHasTransientState(true);

    }

    @Override
    public int getItemCount() {
       return _locationList.size();
    }

    public void updateDataSet(List<Location> data){
        _locationList.clear();
        _locationList.addAll(data);
        notifyDataSetChanged();
    }



    public interface IOnClickListener{
        void startCall(String mobileNo);
        void openMapUsingCordinate(String longitude, String latitude);
        void openMayUsingSearchTxt(String searchTxt);
    }


    public static class LocationViewHolder extends RecyclerView.ViewHolder {
        public TextView title, initial, gailnetCode;
        public LinearLayout container;
        public Button dropdownBtn;
        public Location location;
        private final IOnClickListener _listener;
        private final LayoutInflater _layoutInflater;

        public LocationViewHolder(@NonNull LayoutInflater layoutInflater, ViewGroup parent, IOnClickListener listener) {
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

            if(location.getTEL1()!=null && !location.getTEL1().trim().isEmpty()){
                addPhoneItemView( "Tel 1",location.getTEL1() );
            }

            if(location.getTEL2()!=null && !location.getTEL2().trim().isEmpty()){
                addPhoneItemView( "Tel 2",location.getTEL2() );
            }

            if(location.getTEL3()!=null && !location.getTEL3().trim().isEmpty()){
                addPhoneItemView( "Tel 3",location.getTEL3() );
            }

            if(location.getTEL4() !=null && !location.getTEL4().trim().isEmpty()){
                addPhoneItemView( "Tel 4",location.getTEL4() );
            }

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


    }

}
