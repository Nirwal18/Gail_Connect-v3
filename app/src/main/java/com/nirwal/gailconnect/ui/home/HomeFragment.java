package com.nirwal.gailconnect.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.snackbar.Snackbar;
import com.nirwal.gailconnect.Constants;
import com.nirwal.gailconnect.MainActivity;
import com.nirwal.gailconnect.MyApp;
import com.nirwal.gailconnect.R;
import com.nirwal.gailconnect.databinding.FragmentHomeBinding;


public class HomeFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "HomeFragment";
    private HomeViewModel _homeViewModel;
    private FragmentHomeBinding _viewBinding;
    private NavController _navController;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        _viewBinding = FragmentHomeBinding.inflate(inflater, container, false);

        _viewBinding.cardViewEmploy.setOnClickListener(this);
        _viewBinding.cardViewOffices.setOnClickListener(this);
        _viewBinding.cardViewHospitalList.setOnClickListener(this);
        _viewBinding.cardViewGuesthouseList.setOnClickListener(this);
        _viewBinding.cardViewHollydayList.setOnClickListener(this);
        _viewBinding.cardViewBirthdayList.setOnClickListener(this);
        _viewBinding.cardViewHindi.setOnClickListener(this);
        _viewBinding.cardViewGstin.setOnClickListener(this);
        _viewBinding.cardViewUsefullink.setOnClickListener(this);

        _viewBinding.settingBtn.setOnClickListener(this);
        _viewBinding.profileBtn.setOnClickListener(this);
        _viewBinding.syncBtn.setOnClickListener(this);


        firstTimeSetup();
        initBannerAnimation();

        return _viewBinding.getRoot();
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        _navController =  Navigation.findNavController(_viewBinding.getRoot());

        boolean isAuth = requireContext().getSharedPreferences(MyApp.SHEARED_PREF,Context.MODE_PRIVATE)
                .getBoolean(Constants.USER_IS_AUTH, false);
        if(!isAuth){
            _navController.navigate(R.id.loginFragment);
        }

        _homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        initWelcomeMessage();
    }



    @Override
    public void onClick(View v) {
       if(v == _viewBinding.cardViewEmploy){
           _navController.navigate(R.id.navigation_contacts);
           return;
       }

        if(v == _viewBinding.cardViewOffices){
            _navController.navigate(R.id.action_navigation_home_to_officesFragment);
            return;
        }

        if(v == _viewBinding.cardViewHospitalList){
            _navController.navigate(R.id.action_navigation_home_to_hospitalListFragment);
            return;
        }

        if(v == _viewBinding.cardViewGuesthouseList){
            _navController.navigate(R.id.action_navigation_home_to_gusestHouseListFragment);
            return;
        }

        if(v == _viewBinding.cardViewHollydayList){
            _navController.navigate(R.id.action_navigation_home_to_hollydayListFragment);
            return;
        }


        if(v == _viewBinding.cardViewBirthdayList){
            _navController.navigate(R.id.action_navigation_home_to_birthdayFragment);
            return;
        }


        if(v == _viewBinding.cardViewHindi){
            _navController.navigate(R.id.action_navigation_home_to_hindiFragment);
            return;
        }


        if(v == _viewBinding.cardViewGstin){
            _navController.navigate(R.id.action_navigation_home_to_GSTINDialog);
            return;
        }


        if(v == _viewBinding.cardViewUsefullink){
            _navController.navigate(R.id.action_navigation_home_to_usefulLinkFragment);
            return;
        }

        if(v== _viewBinding.settingBtn){
             Navigation.findNavController(requireView())
                     .navigate(R.id.action_navigation_home_to_settingsFragment);
             return;
        }

        if(v == _viewBinding.profileBtn){
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_navigation_home_to_profileFragment);
            return;
        }

        if(v == _viewBinding.syncBtn){
            if(getActivity() instanceof MainActivity)
            {
                ((MainActivity)getActivity()).startSyncService();
            }
            return;
        }



    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        _navController = null;
        _homeViewModel = null;
        _viewBinding = null;
    }


    public void showSnakeBar(String msg){
        Snackbar.make(requireView(), msg,Snackbar.LENGTH_LONG).setBackgroundTint(Color.GRAY).show();
    }


    private void firstTimeSetup(){
        if(requireActivity() instanceof MainActivity){
            ((MainActivity)requireActivity()).firstTimeSetup();
        }
    }

    private void initBannerAnimation(){
        AnimationDrawable animationDrawable = (AnimationDrawable) _viewBinding.bannerView.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();
    }

    private void initWelcomeMessage(){
        SharedPreferences sp =requireContext().getSharedPreferences(MyApp.SHEARED_PREF,Context.MODE_PRIVATE);
       String name = sp.getString(HomeViewModel.EMPLOY_NAME,"");
       if(name.isEmpty()){
           _homeViewModel.fetchEmployLocAndDept(requireActivity().getApplication(), sp, name1 -> {
               _viewBinding.welcomeTxt.setText(
                       getString(R.string.welcome_txt,name1)
               );
           });
       }else {
           _viewBinding.welcomeTxt.setText(
                   getString(R.string.welcome_txt,name)
           );
       }

    }


}