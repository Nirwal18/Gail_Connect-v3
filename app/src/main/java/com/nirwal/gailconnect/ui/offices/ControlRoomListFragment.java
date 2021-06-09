package com.nirwal.gailconnect.ui.offices;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.nirwal.gailconnect.MyIntentManager;
import com.nirwal.gailconnect.MyPermissionManager;
import com.nirwal.gailconnect.R;
import com.nirwal.gailconnect.adaptors.LocationAdaptor;
import com.nirwal.gailconnect.adaptors.OfficeListAdaptor;
import com.nirwal.gailconnect.databinding.FragmentControlRoomListBinding;
import com.nirwal.gailconnect.modal.Location;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ControlRoomListFragment extends Fragment implements LocationAdaptor.IOnClickListener {

    private ControlRoomListViewModel _viewModel;
    private FragmentControlRoomListBinding _viewBinding;
    private LocationAdaptor _adaptor;

    public static ControlRoomListFragment newInstance() {
        return new ControlRoomListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        _viewBinding = FragmentControlRoomListBinding.inflate(inflater, container, false);



        return _viewBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        _viewModel = new ViewModelProvider(this).get(ControlRoomListViewModel.class);
        _adaptor = new LocationAdaptor(new ArrayList<>(), this);

        _viewBinding.recyclerViewControlRoom.setLayoutManager(new LinearLayoutManager(requireContext()));
        _viewBinding.recyclerViewControlRoom.setAdapter(_adaptor);
        _viewModel.get_locationList(requireActivity().getApplication())
                .observe(getViewLifecycleOwner(), new Observer<List<Location>>() {
                    @Override
                    public void onChanged(List<Location> locations) {
                        _viewBinding.progressBar3.setVisibility(View.GONE);
                        if(locations.size()>0){
                            _viewBinding.textView15.setVisibility(View.GONE);
                        }else {
                            _viewBinding.textView15.setVisibility(View.VISIBLE);
                        }
                        _adaptor.updateDataSet(locations);
                    }
                });
    }




    private String _mobileNumber;
    @Override
    public void startCall(String number){
        _mobileNumber = number;
        if(MyPermissionManager.isCallPermissionGranted(requireContext())){
          performCall(number);
        }
        else{
            requestCallPermission();
        }
    }

    public void performCall(String number){
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+number));//change the number
        startActivity(callIntent);
    }

    public void showCallPermissionJustification(){

        if(shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)){
            requestCallPermission();
        }else {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Permission denied")
                    .setMessage("Without this permission the app is unable to process 'CALL'. \n" +
                            "If you choose re-try then: Permissions> Phone> Allow")
                    .setPositiveButton("RE-TRY", (dialog, which) -> {

                        startActivity(MyIntentManager.openSettingForSelf(requireContext()));
                    })
                    .setNegativeButton("Exit", null)
                    .create()
                    .show();
        }

    }

    public void requestCallPermission(){

        if(shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)){
            new AlertDialog.Builder(requireContext())
                    .setTitle("Permission needed")
                    .setMessage("Call permission required to perform this action")
                    .setCancelable(false)
                    .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestPermissions(new String[]{Manifest.permission.CALL_PHONE},
                                    MyPermissionManager.call_permission_request );
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .create()
                    .show();
        } else {
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE},
                    MyPermissionManager.call_permission_request );
        }
    }

    @Override
    public void openMapUsingCordinate(String longitude, String latitude) {

    }

    @Override
    public void openMayUsingSearchTxt(String searchTxt) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MyPermissionManager.call_permission_request: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Snackbar.make(requireView(),"Permission granted", Snackbar.LENGTH_LONG)
                            .setBackgroundTint(getResources().getColor(R.color.success,requireContext().getTheme()))
                            .show();
                    performCall(_mobileNumber);
                } else {
                    showCallPermissionJustification();
                }
                break;
            }

        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        _viewModel = null;
        _viewBinding = null;
        _adaptor = null;
    }
}