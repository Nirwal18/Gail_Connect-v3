package com.nirwal.gailconnect.ui.hindi;

import android.content.ActivityNotFoundException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.nirwal.gailconnect.MyIntentManager;
import com.nirwal.gailconnect.R;
import com.nirwal.gailconnect.adaptors.IOnListItemClickListener;
import com.nirwal.gailconnect.adaptors.LinkAdaptor;
import com.nirwal.gailconnect.databinding.FragmentHindiBinding;
import com.nirwal.gailconnect.modal.Link;
import com.nirwal.gailconnect.ui.pdf.PdfReaderFragment;

import java.util.ArrayList;

public class HindiFragment extends Fragment implements IOnListItemClickListener {

    private HindiViewModel _viewModel;
    private FragmentHindiBinding _viewBinding;
    private LinkAdaptor _adaptor;

    public static HindiFragment newInstance() {
        return new HindiFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        _viewBinding = FragmentHindiBinding.inflate(inflater, container, false);
        _viewBinding.imgBackBtn.setOnClickListener(v -> goBack());
        return _viewBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        _viewModel = new ViewModelProvider(this).get(HindiViewModel.class);
        _adaptor =  new LinkAdaptor(new ArrayList<>(), this);
        _viewBinding.hindiRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        _viewBinding.hindiRecycler.setAdapter(_adaptor);

        _viewModel.get_linkList().observe(getViewLifecycleOwner(), links -> {
            _adaptor.updateDataSet(links);
            hideLoading();
        });

    }


    private void hideLoading(){
        _viewBinding.progressBar10.setVisibility(View.GONE);
        _viewBinding.txtLoading.setVisibility(View.GONE);
    }

    private void goBack(){
        Navigation.findNavController(requireView())
                .popBackStack();
    }

    @Override
    public void onListItemClicked(int position) {
        Link link = _viewModel.get_linkList().getValue().get(position);
        switch (link.getMimeType()){
            case Link.MIME_PDF:{
                openPDF(link.getUrl());
                break;
            }
            case Link.MIME_URL:{
                openWebBrowser(link.getUrl());
                break;
            }

            case "hindi_word_bank":{
                openWordBank(link.getUrl());
                break;
            }


            default:{
                Toast.makeText(requireContext(),"Error: Nothing found to handle it.", Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }

    private void openPDF(String url){
        String[] splitStrgs = url.split("/");
        String pdfFileName = splitStrgs[splitStrgs.length-1];

        Bundle bundle =  new Bundle();
        bundle.putString(PdfReaderFragment.ARG_FILE_URL,url);
        bundle.putString(PdfReaderFragment.ARG_FILE_NAME, pdfFileName);

        Navigation.findNavController(requireView())
                .navigate(R.id.action_hindiFragment_to_pdfReaderFragment,bundle);
    }

    private void openWebBrowser(String url){
        try {
            startActivity(MyIntentManager.openBrowser(url));
        } catch (ActivityNotFoundException e) {
            _viewBinding.txtLoading.setText(
                    requireContext()
                            .getResources()
                            .getText(R.string.error_plz_install_browser)
            );

            e.printStackTrace();
        }
    }

    private void openWordBank(String path){
        Bundle bundle = new Bundle();
        bundle.putString(WordBankFragment.ARG_PATH,path);
        Navigation.findNavController(requireView())
                .navigate(R.id.action_hindiFragment_to_wordBankFragment,bundle);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        _viewModel = null;
        _viewBinding = null;
    }
}