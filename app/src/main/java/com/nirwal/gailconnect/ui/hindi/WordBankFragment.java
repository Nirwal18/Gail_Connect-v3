package com.nirwal.gailconnect.ui.hindi;

import androidx.lifecycle.ViewModelProvider;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.nirwal.gailconnect.MyApp;
import com.nirwal.gailconnect.R;
import com.nirwal.gailconnect.adaptors.WordListAdaptor;
import com.nirwal.gailconnect.databinding.FragmentWordBankBinding;

import java.util.ArrayList;

import static android.content.Context.CLIPBOARD_SERVICE;

public class WordBankFragment extends Fragment implements View.OnClickListener,
        WordListAdaptor.IonRowClick {
    private static final String TAG = "WordBankFragment";
    public static final String ARG_PATH = "ARG_PATH";

    private WordBankViewModel _viewModel;
    private FragmentWordBankBinding _viewBinding;
    private WordListAdaptor _adaptor;
    private String _path;

    public static WordBankFragment newInstance(String path) {
        WordBankFragment fragment = new WordBankFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_PATH,path);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        _viewBinding = FragmentWordBankBinding.inflate(inflater,container,false);
        _viewBinding.wordListRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        _adaptor = new WordListAdaptor(new ArrayList<>(), this);
        _viewBinding.wordListRecycler.setAdapter(_adaptor);

        _viewBinding.imgSearchBtn.setOnClickListener(this);
        _viewBinding.imgBackBtn.setOnClickListener(this);


        _viewBinding.txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                _viewModel.performSearch(s.toString().toLowerCase());
            }
        });

        if(getArguments()!=null){
            _path = getArguments().getString(ARG_PATH);
        }



        return _viewBinding.getRoot();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        _viewModel = new ViewModelProvider(this).get(WordBankViewModel.class);

        _viewModel.get_errorTxt().observe(getViewLifecycleOwner(),error ->{
            if(error.isEmpty()){
                _viewBinding.txtLoading2.setVisibility(View.GONE);
            }
            else {
                _viewBinding.txtLoading2.setVisibility(View.VISIBLE);
                _viewBinding.txtLoading2.setText(error);
            }
        });

        _viewModel.get_wordListLiveData((MyApp) requireActivity().getApplication(),_path)
                .observe(getViewLifecycleOwner(),words -> {
            _adaptor.updateDataSet(words);
            //Log.d(TAG, "onActivityCreated: size:"+words.size());
            hideProgress();
        });
    }


    @Override
    public void onClick(View v) {
        if(v==_viewBinding.imgBackBtn){
            backBtnClick();
            return;
        }

        if(v==_viewBinding.imgSearchBtn){
            toggleSearch(_viewBinding.txtSearch.getVisibility()==View.GONE);
            return;
        }


    }


    private void backBtnClick(){
        if(_viewBinding.txtSearch.getVisibility()==View.VISIBLE){
            _viewBinding.txtSearch.setText("");
            toggleSearch(false);
            return;
        }

        Navigation.findNavController(requireView())
                .popBackStack();
    }


    private void hideProgress(){
        _viewBinding.progressBar9.setVisibility(View.GONE);
        _viewBinding.txtLoading2.setVisibility(View.GONE);
    }

    private void toggleSearch(boolean show) {
        if(show){
            _viewBinding.txtTitle.setVisibility(View.GONE);
            _viewBinding.txtSearch.setVisibility(View.VISIBLE);
            _viewBinding.txtSearch.requestFocus();
            InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }else {
            _viewBinding.txtTitle.setVisibility(View.VISIBLE);
            _viewBinding.txtSearch.setVisibility(View.GONE);
        }
    }


    @Override
    public void onRowClick(int position) {

    }

    @Override
    public void onCopyBtnClick(int position) {
        String txt =_viewModel.get_wordListLiveData((MyApp)requireActivity().getApplication(),_path)
                .getValue().get(position).getHindi();
        ClipboardManager clipboard = (ClipboardManager) requireContext().getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label",txt );
        clipboard.setPrimaryClip(clip);
        Toast.makeText(requireContext(),"Coppied: "+txt,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        _viewModel = null;
        _viewBinding = null;
    }
}