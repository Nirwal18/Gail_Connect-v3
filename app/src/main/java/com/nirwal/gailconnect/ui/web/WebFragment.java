package com.nirwal.gailconnect.ui.web;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.nirwal.gailconnect.R;
import com.nirwal.gailconnect.databinding.FragmentWebBinding;
import com.nirwal.gailconnect.ui.news.NewsListFragment;

public class WebFragment extends Fragment {
    private static final String TAG = "WebFragment";
    public static final String ARG_URL = "ARG_URL";

    private WebViewModel _viewModel;
    private FragmentWebBinding _viewBinding;
    private String _url;

    public static WebFragment newInstance(String url) {
        WebFragment fragment = new WebFragment();
        final Bundle args = new Bundle();
        args.putString(ARG_URL, url);
        fragment.setArguments(args);


        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _url = getArguments().getString(ARG_URL);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        _viewBinding = FragmentWebBinding.inflate(inflater, container, false);

        return _viewBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        _viewModel = new ViewModelProvider(this).get(WebViewModel.class);
        // TODO: Use the ViewModel
        if(_url!=null && !_url.isEmpty()){
            _viewModel.set_url(_url);
        }


        _viewModel.get_url().observe(getViewLifecycleOwner(), s -> {
            _url = s;
        });

        initWebView();
    }


    public void initWebView(){
        WebSettings ws = _viewBinding.webView.getSettings();
        ws.setJavaScriptEnabled(true);
        _viewBinding.webView.loadUrl(_viewModel.get_url().getValue());
        _viewBinding.webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
               // _viewBinding.progressBar.setVisibility(View.GONE);
            }
        });

//        _viewBinding.webView.setWebChromeClient(new WebChromeClient(){
//            @Override
//            public void onProgressChanged(WebView view, int newProgress) {
//                super.onProgressChanged(view, newProgress);
//                //_viewBinding.progressBar.setProgress(newProgress);
//                if(newProgress>=100){
//                    _viewBinding.progressBar.setVisibility(View.GONE);
//                }
//            }
//        });

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        _viewBinding = null;
    }
}