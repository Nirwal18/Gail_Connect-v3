package com.nirwal.gailconnect.ui.guesthouselist;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.nirwal.gailconnect.HideShowScrollListener;
import com.nirwal.gailconnect.MyIntentManager;
import com.nirwal.gailconnect.MyPermissionManager;
import com.nirwal.gailconnect.R;
import com.nirwal.gailconnect.adaptors.GuestHouseListAdaptor;
import com.nirwal.gailconnect.adaptors.HospitalListAdaptor;
import com.nirwal.gailconnect.databinding.FragmentGuestHouseListBinding;
import com.nirwal.gailconnect.modal.GuestHouse;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class GuestHouseListFragment extends Fragment implements GuestHouseListAdaptor.IOnClickListener {
    private static final String TAG = "GuestHouseListFragment";
    
    private GuestHouseListViewModel _viewModel;
    private FragmentGuestHouseListBinding _viewBinding;
    private GuestHouseListAdaptor _adaptor;

    public static GuestHouseListFragment newInstance() {
        return new GuestHouseListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        _viewBinding = FragmentGuestHouseListBinding.inflate(inflater,container,false);

        _adaptor = new GuestHouseListAdaptor(new ArrayList<>(),this);
        _viewBinding.guestHouseRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        _viewBinding.guestHouseRecycler.setAdapter(_adaptor);

        _viewBinding.imgBackBtn.setOnClickListener(v -> goBack());

       // initTopBar();

        return _viewBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        _viewModel = new ViewModelProvider(this).get(GuestHouseListViewModel.class);
        _viewModel.get_guestHouseList(requireActivity().getApplication())
                .observe(getViewLifecycleOwner(), new Observer<List<GuestHouse>>() {
                    @Override
                    public void onChanged(List<GuestHouse> guestHouses) {
                        _adaptor.updateDataSet(guestHouses);
                        if(guestHouses.size()>0){
                            hideLoading();
                        }

                    }
                });

    }

    private void hideLoading(){
        _viewBinding.progressBar7.setVisibility(View.GONE);
        _viewBinding.loadingTxt.setVisibility(View.GONE);
    }

    private void goBack(){
        Navigation.findNavController(requireView())
                .popBackStack();
    }

    //animating top bar
    private void initTopBar(){
        float height = getResources().getDimensionPixelSize(R.dimen.top_bar_height);

        _viewBinding.guestHouseRecycler.addOnScrollListener(new HideShowScrollListener() {
            @Override
            public void onHide() {
                Log.d(TAG, "onHide: ");

                if(_viewBinding.topBarLay.getVisibility()==View.VISIBLE){
                    _viewBinding.topBarLay.setVisibility(View.GONE);
                }


            }

            @Override
            public void onShow() {
                Log.d(TAG, "onShow: ");
                if(_viewBinding.topBarLay.getVisibility()==View.GONE) {
                    _viewBinding.topBarLay.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void onScrolled() {

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        _viewModel = null;
        _viewBinding = null;
        _adaptor = null;
    }


    @Override
    public void startCall(String mobileNo) {
        if(MyPermissionManager.isCallPermissionGranted(requireContext())){

            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:"+mobileNo));//change the number
            startActivity(callIntent);
        }
        else{
            requestCallPermission();
        }
    }

    @Override
    public void openMapUsingCordinate(String longitude, String latitude) {
        startActivity(MyIntentManager.openMapWithCoordinates(latitude,longitude));
    }

    @Override
    public void openMayUsingSearchTxt(String searchTxt) {
        startActivity(MyIntentManager.openMapWithSearchIntent(searchTxt));
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
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MyPermissionManager.call_permission_request: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(requireContext(), "Permission granted",Toast.LENGTH_LONG)
                            .show();
                    Snackbar.make(requireView(),"Permission granted", Snackbar.LENGTH_LONG)
                            .setBackgroundTint(getResources().getColor(R.color.success,requireContext().getTheme()))
                            .show();
                } else {
                    showCallPermissionJustification();
                }
                break;
            }

        }
    }

}