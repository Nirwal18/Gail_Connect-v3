package com.nirwal.gailconnect.ui.news;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.room.PrimaryKey;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nirwal.gailconnect.MyApp;
import com.nirwal.gailconnect.R;
import com.nirwal.gailconnect.adaptors.NewsListAdaptor;
import com.nirwal.gailconnect.databinding.FragmentNewsListBinding;
import com.nirwal.gailconnect.modal.Contact;
import com.nirwal.gailconnect.ui.bottomsheet.ContactDetailBottomSheet;
import com.nirwal.gailconnect.ui.pdf.PdfReaderFragment;
import com.nirwal.gailconnect.ui.web.WebFragment;

import java.util.ArrayList;

public class NewsListFragment extends Fragment implements NewsListAdaptor.IOnClickListener {
    private static final String TAG = "NewsListFragment";
    public static final int MODE_GAIL_NEWS = 0;
    public static final int MODE_INDUSTRY_NEWS = 1;


    private static final String ARG_DATA ="ARG_DATA";
    private NewsListViewModel _viewModel;
    private FragmentNewsListBinding _viewBinding;
    private NewsListAdaptor _adaptor;

    private int _mode;

    public static NewsListFragment newInstance(int mode) {
        NewsListFragment fragment =new NewsListFragment();
        final Bundle args = new Bundle();
        args.putInt(ARG_DATA, mode);
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        _viewBinding = FragmentNewsListBinding.inflate(inflater, container, false);

        _mode = getArguments().getInt(ARG_DATA);

        return _viewBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        _viewModel = new ViewModelProvider(this).get(NewsListViewModel.class);


        _adaptor =  new NewsListAdaptor(new ArrayList<>());
        _adaptor.set_IOnClickListener(this);

        _viewBinding.newsRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        _viewBinding.newsRecycler.setAdapter(_adaptor);



        _viewModel.get_newsList(requireActivity().getApplication(),_mode )
                .observe(getViewLifecycleOwner(), news -> {
                    _adaptor.updateDataSet(news);
                    hideLoading();

                });

        _viewModel.get_isError().observe(getViewLifecycleOwner(),isError->{
            if(isError){
                showError(_viewModel.get_errorMsg());
            }
        });
    }


    @Override
    public void onNewsItemClick(String pdfFileName) {
        openPDFReader(pdfFileName);
    }

    public void openPDFReader(String pdfFileName){
//        String pdfServerUrl = "https://docs.google.com/gview?embedded=true&url="+MyApp.PDF_URL+pdfFileName;
//        Bundle bundle =  new Bundle();
//        bundle.putString(WebFragment.ARG_URL, pdfServerUrl);
//        Navigation.findNavController(requireView())
//                .navigate(R.id.action_navigation_news_to_webFragment, bundle);

        Bundle bundle =  new Bundle();
        bundle.putString(PdfReaderFragment.ARG_FILE_URL, MyApp.PDF_URL+pdfFileName);
        bundle.putString(PdfReaderFragment.ARG_FILE_NAME, pdfFileName);
        Navigation.findNavController(requireView())
                .navigate(R.id.action_navigation_news_to_pdfReaderFragment, bundle);

    }


    private void hideLoading(){
    _viewBinding.progressBar4.setVisibility(View.GONE);
    _viewBinding.txtLoading.setVisibility(View.GONE);
    }

    private void showError(String errorMsg){
        _viewBinding.txtLoading.setVisibility(View.VISIBLE);
        _viewBinding.txtLoading.setText(errorMsg);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        _viewModel = null;
        _viewBinding = null;
        _adaptor = null;
    }
}