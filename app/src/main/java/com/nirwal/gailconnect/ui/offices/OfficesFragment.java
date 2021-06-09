package com.nirwal.gailconnect.ui.offices;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.nirwal.gailconnect.R;
import com.nirwal.gailconnect.databinding.FragmentOfficesBinding;

public class OfficesFragment extends Fragment {

    private OfficesViewModel _viewModel;
    private FragmentOfficesBinding _viewBinding;
    private ViewStateAdapter _adapter;

    public static OfficesFragment newInstance() {
        return new OfficesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        _viewBinding = FragmentOfficesBinding.inflate(inflater, container, false);
        _viewBinding.imgBackBtn.setOnClickListener(v->{
            Navigation.findNavController(requireView())
                    .popBackStack();
        });

        initTabLayout();

        return _viewBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        _viewModel = new ViewModelProvider(this).get(OfficesViewModel.class);
        // TODO: Use the ViewModel
    }

    public void initTabLayout(){
        if(_adapter==null){
            _adapter = new ViewStateAdapter(getChildFragmentManager(), getLifecycle());
        }

        _viewBinding.viewPagerOffice.setAdapter(_adapter);
        _viewBinding.tabLayoutOffice.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                _viewBinding.viewPagerOffice.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        _viewBinding.viewPagerOffice.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                _viewBinding.tabLayoutOffice.selectTab(_viewBinding.tabLayoutOffice.getTabAt(position));
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        _viewModel = null;
        _viewBinding = null;
        _adapter = null;
    }
}