package com.nirwal.gailconnect.ui.offices;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.nirwal.gailconnect.MyIntentManager;
import com.nirwal.gailconnect.MyPermissionManager;
import com.nirwal.gailconnect.adaptors.OfficeListAdaptor;
import com.nirwal.gailconnect.databinding.FragmentOfficeListBinding;

import java.util.ArrayList;

public class OfficeListFragment extends Fragment implements OfficeListAdaptor.IOnClickListener {
    private static final String TAG = "OfficeListFragment";

    private OfficeListViewModel _viewModel;
    private FragmentOfficeListBinding _viewBinding;
    private OfficeListAdaptor _adaptor;

    public static OfficeListFragment newInstance() {
        return new OfficeListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        _viewBinding = FragmentOfficeListBinding.inflate(inflater,container,false);
        _viewBinding.progressBar2.setVisibility(View.VISIBLE);

        return _viewBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        _viewModel = new ViewModelProvider(this).get(OfficeListViewModel.class);
        // TODO: Use the ViewModel


        _adaptor = new OfficeListAdaptor(new ArrayList<>(), this);
        _viewBinding.recyclerViewOffice.setLayoutManager(new LinearLayoutManager(requireContext()));
        _viewBinding.recyclerViewOffice.setAdapter(_adaptor);

        _viewModel.get_officeList(requireActivity().getApplication())
                .observe(getViewLifecycleOwner(), offices -> {

                    _viewBinding.progressBar2.setVisibility(View.GONE);

                    if(offices.size()>0){
                        _viewBinding.textView16.setVisibility(View.GONE);
                    }else {
                        _viewBinding.textView16.setVisibility(View.VISIBLE);
                    }
                    _adaptor.updateDataSet(offices);
        });
    }




    @Override
    public void startCall(String mobileNo) {
        if(MyPermissionManager.isCallPermissionGranted(requireContext())){
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:"+mobileNo));//change the number
            startActivity(callIntent);
        }
        else{
            MyPermissionManager.requestCallPermission(requireActivity());
        }
    }



    private void openMap(String coordinate){
        String[] cord = coordinate.split(":");
        try {
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse("geo:" + cord[0]
                            + "," + cord[1]
                            + "?q=" + cord[0]
                            + "," + cord[1]
                            + "(" + "GAIL OFFICE" + ")"));
            intent.setComponent(new ComponentName(
                    "com.google.android.apps.maps",
                    "com.google.android.maps.MapsActivity"));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {

            try {

                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?saddr="+cord[0]+","+cord[1]+"&daddr="+cord[0]+","+cord[1]));
                startActivity(intent);
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=com.google.android.apps.maps")));
            }

            e.printStackTrace();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        _viewModel = null;
        _viewBinding = null;
        _adaptor = null;
    }


    @Override
    public void openMapUsingCordinate(String longitude, String latitude) {
        Intent intent = MyIntentManager.openMapWithCoordinates(latitude,longitude);
        startActivity(intent);
    }

    @Override
    public void openMayUsingSearchTxt(String searchTxt) {
        Intent intent = MyIntentManager.openMapWithSearchIntent(searchTxt);
        startActivity(intent);
    }
}

