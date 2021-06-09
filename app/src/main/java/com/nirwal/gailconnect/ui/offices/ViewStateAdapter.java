package com.nirwal.gailconnect.ui.offices;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewStateAdapter extends FragmentStateAdapter {

    private List<Fragment> _fragmentList;

    public ViewStateAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);

        _fragmentList = new ArrayList<>();
        _fragmentList.add(new OfficeListFragment());
        _fragmentList.add(new ControlRoomListFragment());
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