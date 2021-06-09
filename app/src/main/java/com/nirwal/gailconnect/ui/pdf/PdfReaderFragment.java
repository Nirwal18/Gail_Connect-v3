package com.nirwal.gailconnect.ui.pdf;

import androidx.annotation.ColorInt;
import androidx.lifecycle.ViewModelProvider;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.listener.OnPageScrollListener;
import com.google.android.material.snackbar.Snackbar;
import com.nirwal.gailconnect.MyApp;
import com.nirwal.gailconnect.R;
import com.nirwal.gailconnect.databinding.FragmentPdfReaderBinding;
import com.nirwal.gailconnect.tasks.DownloadFile;
import com.nirwal.gailconnect.tasks.FileDownloader;

import java.io.File;

public class PdfReaderFragment extends Fragment {

    public static final String ARG_FILE_NAME = "ARG_FILE_NAME";
    public static final String ARG_FILE_URL = "ARG_FILE_URL";
    private PdfReaderViewModel mViewModel;
    private FragmentPdfReaderBinding _viewBinding;

    public static PdfReaderFragment newInstance(String url,String fileName) {
        PdfReaderFragment fragment = new PdfReaderFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_FILE_URL,url);
        bundle.putString(ARG_FILE_NAME, fileName);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        _viewBinding = FragmentPdfReaderBinding.inflate(inflater,container,false);




        return _viewBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PdfReaderViewModel.class);
        if(getArguments()!=null){
            String filename = getArguments().getString(ARG_FILE_NAME);
            String url = getArguments().getString(ARG_FILE_URL);
            getFile(url,filename);
        }
    }

    private void initPdfView(File pdfFile){

        _viewBinding.pdfView.fromFile(pdfFile)
                //.pages(0, 2, 1, 3, 3, 3) // all pages are displayed by default
                .enableSwipe(true) // allows to block changing pages using swipe
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .defaultPage(0)
                .onLoad(new OnLoadCompleteListener() {
                    @Override
                    public void loadComplete(int nbPages) {
                        _viewBinding.progressBar5.setVisibility(View.GONE);
                        _viewBinding.txtLoadPdf.setVisibility(View.GONE);
                    }
                }) // called after document is loaded and starts to be rendered
                .onPageChange(new OnPageChangeListener() {
                    @Override
                    public void onPageChanged(int page, int pageCount) {
                        _viewBinding.txtPageNo.setText(page+1+"/"+pageCount);
                        _viewBinding.txtPageNo.animate()
                                .withStartAction(() -> {
                                    if(_viewBinding==null)return;
                                    _viewBinding.txtPageNo.setVisibility(View.VISIBLE);
                                })
                                .withEndAction(() -> {
                                   if(_viewBinding==null)return;
                                    _viewBinding.txtPageNo.setVisibility(View.GONE);
                                })
                                .setDuration(1000)
                                .start();

                    }
                })
                .onError(new OnErrorListener() {
                    @Override
                    public void onError(Throwable t) {
                        showErrorTxt(t.getMessage());
                    }
                })
                .onPageError(new OnPageErrorListener() {
                    @Override
                    public void onPageError(int page, Throwable t) {
                        showErrorTxt("Page no:"+page+ "\nError:"+t.getMessage());

                    }
                })
                .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
                .password(null)
                .scrollHandle(null)
                .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                // spacing between pages in dp. To define spacing color, set view background
                .spacing(16)
                .load();
    }

    public void showSnackbar(String msg, @ColorInt int color){
        Snackbar.make(_viewBinding.getRoot(), msg,Snackbar.LENGTH_LONG).setBackgroundTint(color).show();
    }


    private void getFile(String url, String fileName){

        File pdfFile = readCacheFile(fileName);

        if(!pdfFile.exists()){
        DownloadFile task = new DownloadFile(requireContext());
        task.addOnCompleteListener(new DownloadFile.IOnCompleteListener() {
            @Override
            public void onComplete() {
                     initPdfView(readCacheFile(fileName));
            }
        });
        task.execute(url, fileName);

        return;
        }

        initPdfView(pdfFile);

    }

    private File readCacheFile(String fileName){
       String extdir = requireContext().getCacheDir().toString();
        return new File( extdir+"/PDF/" + fileName);
    }

    private void showErrorTxt(String msg){
        _viewBinding.progressBar5.setVisibility(View.GONE);
        _viewBinding.txtLoadPdf.setText(msg);
        _viewBinding.txtLoadPdf.setTextColor(Color.RED);
        _viewBinding.txtLoadPdf.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mViewModel = null;
        _viewBinding = null;
    }
}