package com.nirwal.gailconnect.ui.hollydaylist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.nirwal.gailconnect.databinding.FragmentHollydayListBinding;

public class HollydayListFragment extends Fragment {

    private HollydayListViewModel _viewModel;
    private FragmentHollydayListBinding _viewBinding;

    public static HollydayListFragment newInstance() {
        return new HollydayListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        _viewBinding = FragmentHollydayListBinding.inflate(inflater, container,false);
        _viewBinding.imgBackBtn.setOnClickListener(v -> {
            Navigation.findNavController(requireView())
                    .popBackStack();
        });

        return _viewBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        _viewModel = new ViewModelProvider(this).get(HollydayListViewModel.class);
        // TODO: Use the ViewModel

    }

}