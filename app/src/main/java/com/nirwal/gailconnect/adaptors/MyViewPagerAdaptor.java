package com.nirwal.gailconnect.adaptors;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

public class MyViewPagerAdaptor extends FragmentStateAdapter {

        private List<Fragment> _fragmentList;

        public MyViewPagerAdaptor(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, List<Fragment> fragmentList) {
            super(fragmentManager, lifecycle);
            _fragmentList = fragmentList;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            // Hardcoded in this order, you'll want to use lists and make sure the titles match
            return _fragmentList.get(position);
        }

        @Override
        public int getItemCount() {
            // Hardcoded, use lists
            return _fragmentList.size();
        }
}

