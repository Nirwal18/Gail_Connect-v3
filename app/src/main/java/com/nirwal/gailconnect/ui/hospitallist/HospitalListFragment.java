package com.nirwal.gailconnect.ui.hospitallist;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nirwal.gailconnect.MyIntentManager;
import com.nirwal.gailconnect.R;
import com.nirwal.gailconnect.adaptors.HospitalListAdaptor;
import com.nirwal.gailconnect.databinding.FragmentHospitalListBinding;
import com.nirwal.gailconnect.modal.Hospital;

import java.util.ArrayList;
import java.util.List;

public class HospitalListFragment extends Fragment  implements HospitalListAdaptor.IOnClickListener{
    private static final String TAG = "HospitalListFragment";

    private HospitalListViewModel _viewModel;
    private FragmentHospitalListBinding _viewBinding;
    private HospitalListAdaptor _adaptor;

    public static HospitalListFragment newInstance() {
        return new HospitalListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        _viewBinding = FragmentHospitalListBinding.inflate(inflater,container,false);
        _viewBinding.imgBackBtn.setOnClickListener(v->{
            Navigation.findNavController(requireView())
                    .popBackStack();
        });

        return _viewBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        _viewModel = new ViewModelProvider(this).get(HospitalListViewModel.class);

        _adaptor = new HospitalListAdaptor(new ArrayList<>(), this);

        _viewBinding.hospitalList.setLayoutManager(new LinearLayoutManager(requireContext()));
        _viewBinding.hospitalList.setAdapter(_adaptor);

        _viewModel.get_hospitalList(requireActivity().getApplication())
                .observe(getViewLifecycleOwner(), new Observer<List<Hospital>>() {
                    @Override
                    public void onChanged(List<Hospital> hospitals) {
                        _adaptor.updateDataSet(hospitals);
                        hideLoading();
                    }
                });

    }

    private void hideLoading(){
        _viewBinding.progressBar6.setVisibility(View.GONE);
        _viewBinding.txtLoading1.setVisibility(View.GONE);
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