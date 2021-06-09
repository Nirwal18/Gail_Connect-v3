package com.nirwal.gailconnect.ui.login;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nirwal.gailconnect.Constants;
import com.nirwal.gailconnect.MyApp;
import com.nirwal.gailconnect.R;
import com.nirwal.gailconnect.databinding.FragmentProfileBinding;
import com.nirwal.gailconnect.databinding.FragmentWordBankBinding;
import com.nirwal.gailconnect.tasks.ImageLoader;
import com.nirwal.gailconnect.ui.home.HomeViewModel;

public class ProfileFragment extends Fragment {

    private ProfileViewModel _viewModel;
    private FragmentProfileBinding _viewBinding;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        _viewBinding = FragmentProfileBinding.inflate(inflater, container, false);
        updateUi();
        return _viewBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        _viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        // TODO: Use the ViewModel
    }

    private void updateUi(){
        SharedPreferences sp = getActivity().getSharedPreferences(MyApp.SHEARED_PREF, Context.MODE_PRIVATE);
        String employName = sp.getString(HomeViewModel.EMPLOY_NAME,"Gail employ");
        String cpf = sp.getString(Constants.USER_CPF, "null");
        String dept = sp.getString(Constants.USER_DEPARTMENT,"null");
        String desig = sp.getString(HomeViewModel.EMPLOY_DESIG,"null");
        String grade = sp.getString(HomeViewModel.EMPLOY_GRADE,"null");
        String workLoc = sp.getString(Constants.USER_WORK_LOCATION,"INDIA");
        String imgUrl = sp.getString(HomeViewModel.EMPLOY_IMAGE_URL,"");
        _viewBinding.contactName.setText(employName);
        _viewBinding.contactDesig.setText(desig+"("+grade+")");
        _viewBinding.contactDeptLoc.setText(dept+", "+workLoc);
        _viewBinding.contactCpf.setText("CPF: "+cpf);

        if(imgUrl.isEmpty()){
            _viewBinding.txtNameFirstChar.setText(String.valueOf(employName.charAt(0)));
        }else {
            ImageLoader.with(getActivity().getApplication())
                    .load(MyApp.IMG_URL+imgUrl)
                    .addListener(new ImageLoader.OnLoadListener() {
                        @Override
                        public void onSuccess() {
                            _viewBinding.txtNameFirstChar.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onError(String msg) {

                        }
                    })
                    .into(_viewBinding.profilePic);
        }

        _viewBinding.aboutTxt.setText(Html.fromHtml("<h1>About this app" +
                "<p> <b>Gail connect v3</b> which is base on <b>Gail original</b> " +
                " Gail connect v2 app is recreated by <b>Akshay Kumar Nirwal</b> " +
                "employee of <b>Gail india limited</b>" +
                " for demonstrating various <b>UI and functional</b> improvements.</p>" +
                "<p>Like: " +
                "<ul>"+
                "<li>Better contact search support.</li>" +
                 "<li>Contact export support.</li>"+
                "<li>Birthday list and widget support</li>"+
                "<li>Rajbhasa Hindi improvement.</li>"+
                "<li>Word search and copy support.</li>"+
                "<li>Various UI improvement.</li>"+
                "<li>Dark and Light mode support.</li>"+
                "<li>Gail news now display file without needed to forward request on external app.</li>"+
                "</ul>"+
                "<b>Note:</b> This app is only for demonstrating purpose.</p>"));

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        _viewModel = null;
        _viewBinding = null;
    }
}