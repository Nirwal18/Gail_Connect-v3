package com.nirwal.gailconnect.ui.search;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.nirwal.gailconnect.adaptors.ContactAdaptor;
import com.nirwal.gailconnect.databinding.FragmentSearchContactBinding;
import com.nirwal.gailconnect.modal.Contact;

import java.util.ArrayList;
import java.util.List;

public class SearchContactFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "SearchContactFragment";

    private SearchContactViewModel mViewModel;
    private FragmentSearchContactBinding _viewBinding;

    private ContactAdaptor _contactAdaptor;

    public static SearchContactFragment newInstance() {
        return new SearchContactFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        _viewBinding = FragmentSearchContactBinding.inflate(inflater,container,false);
        _contactAdaptor =  new ContactAdaptor(new ArrayList<>(), new ContactAdaptor.IOnClickListener() {
            @Override
            public void onRowClick(int position) {

            }
        });

        _viewBinding.imgBackBtn.setOnClickListener(this);
        _viewBinding.imgCloseBtn.setOnClickListener(this);

        _viewBinding.searchRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        _viewBinding.searchRecycler.setAdapter(_contactAdaptor);


        _viewBinding.editSearchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mViewModel.loadContactByName(requireContext(),_viewBinding.editSearchBox.getText().toString().trim());
                _viewBinding.progSearch.setVisibility(View.VISIBLE);
                if(_viewBinding.editSearchBox.getText().toString().trim().isEmpty()){
                    _contactAdaptor.updateDataSet(new ArrayList<>());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return _viewBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SearchContactViewModel.class);
        mViewModel.getContactList().observe(getViewLifecycleOwner(), new Observer<List<Contact>>() {
            @Override
            public void onChanged(List<Contact> contacts) {
                // update contact list adaptor
                _contactAdaptor.updateDataSet(contacts);
                _viewBinding.progSearch.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public void onClick(View v) {
       if(_viewBinding.imgBackBtn.getId() == v.getId()){
           // go one step back in view hirachy
           Navigation.findNavController(requireView()).popBackStack();
       }
        else if(_viewBinding.imgCloseBtn.getId() == v.getId()){
            _viewBinding.editSearchBox.setText("");
       }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        _viewBinding = null;
        _contactAdaptor = null;
    }
}