package com.nirwal.gailconnect.ui.usefullink;

import androidx.lifecycle.ViewModelProvider;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nirwal.gailconnect.MyIntentManager;
import com.nirwal.gailconnect.R;
import com.nirwal.gailconnect.adaptors.IOnListItemClickListener;
import com.nirwal.gailconnect.adaptors.LinkAdaptor;
import com.nirwal.gailconnect.databinding.FragmentUsefulLinkBinding;
import com.nirwal.gailconnect.modal.Link;

import java.util.ArrayList;

public class UsefulLinkFragment extends Fragment implements IOnListItemClickListener {

    private FragmentUsefulLinkBinding _viewBinding;
    private UsefulLinkViewModel _viewModel;
    private LinkAdaptor _linkAdaptor;

    public static UsefulLinkFragment newInstance() {
        return new UsefulLinkFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        _viewBinding = FragmentUsefulLinkBinding.inflate(inflater,container, false);
        _viewBinding.imgBackBtn.setOnClickListener(v-> Navigation.findNavController(requireView()).popBackStack());

        return _viewBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        _viewModel = new ViewModelProvider(this).get(UsefulLinkViewModel.class);
        // TODO: Use the ViewModel

        _linkAdaptor = new LinkAdaptor(new ArrayList<>(),this);

        _viewBinding.usefullLinkList.setLayoutManager(new LinearLayoutManager(requireContext()));
        _viewBinding.usefullLinkList.setAdapter(_linkAdaptor);
        _viewModel.get_linkList().observe(getViewLifecycleOwner(), myListItems -> {
            _linkAdaptor.updateDataSet(myListItems);
            hideLoading();
        });

    }

    @Override
    public void onListItemClicked(int position) {
        Link link = _viewModel.get_linkList().getValue().get(position);
        try {
            startActivity(MyIntentManager.openBrowser(link.getUrl()));
        } catch (ActivityNotFoundException e) {
            _viewBinding.txtLoading.setText(
                    requireContext()
                            .getResources()
                            .getText(R.string.error_plz_install_browser)
            );

            e.printStackTrace();
        }
    }

    private void hideLoading(){
        _viewBinding.progressBar8.setVisibility(View.GONE);
        _viewBinding.txtLoading.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        _viewModel = null;
        _viewBinding = null;
        _linkAdaptor = null;
    }
}