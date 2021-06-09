package com.nirwal.gailconnect.ui.news;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.nirwal.gailconnect.R;
import com.nirwal.gailconnect.adaptors.MyViewPagerAdaptor;
import com.nirwal.gailconnect.databinding.FragmentNewsBinding;
import com.nirwal.gailconnect.ui.offices.ViewStateAdapter;

import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends Fragment {

    private NewsViewModel _newsViewModel;
    private FragmentNewsBinding _viewBinding;
    private MyViewPagerAdaptor _adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        _newsViewModel =
                new ViewModelProvider(this).get(NewsViewModel.class);
        _viewBinding = FragmentNewsBinding.inflate(inflater, container, false);

        initTopBar();

        return _viewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initTabLayout();
    }

    public void initTabLayout(){

        List<Fragment> fragmentList = new ArrayList<>();

        fragmentList.add(NewsListFragment.newInstance(NewsListFragment.MODE_GAIL_NEWS));
        fragmentList.add(NewsListFragment.newInstance(NewsListFragment.MODE_INDUSTRY_NEWS));

        _adapter = new MyViewPagerAdaptor(getChildFragmentManager(), getLifecycle(),fragmentList);

        _viewBinding.viewPagerNews.setAdapter(_adapter);

        _viewBinding.tabLayoutNews.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                _viewBinding.viewPagerNews.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        _viewBinding.viewPagerNews.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                _viewBinding.tabLayoutNews.selectTab(_viewBinding.tabLayoutNews.getTabAt(position));
            }
        });
    }


    private void initTopBar(){

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        _newsViewModel = null;
        _viewBinding = null;
        _adapter = null;
    }
}


