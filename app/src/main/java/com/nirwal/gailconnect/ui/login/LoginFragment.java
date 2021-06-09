package com.nirwal.gailconnect.ui.login;

import androidx.annotation.ColorInt;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.nirwal.gailconnect.AccountAuth.MyAccountManager;
import com.nirwal.gailconnect.Constants;
import com.nirwal.gailconnect.MainActivity;
import com.nirwal.gailconnect.MyApp;
import com.nirwal.gailconnect.R;
import com.nirwal.gailconnect.databinding.FragmentLoginBinding;
import com.nirwal.gailconnect.modal.AuthResponse;

import java.nio.channels.spi.AbstractInterruptibleChannel;
import java.util.ArrayList;

public class LoginFragment extends Fragment{

    private LoginViewModel _viewModel;
    private FragmentLoginBinding _viewBinding;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        _viewBinding = FragmentLoginBinding.inflate(inflater,container,false);
        _viewBinding.btnLogin.setOnClickListener(v -> login());
        return _viewBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        _viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        _viewModel.get_authResponse().observe(getViewLifecycleOwner(),authResponse -> {
            if(authResponse!=null){
                saveUserDetails(authResponse);

                showSnackBar("Login Success!",
                        getResources().getColor(R.color.success, requireActivity().getTheme())
                );

                addAccountToAndroidSystem();

                //return to home
                Navigation.findNavController(requireView())
                        .popBackStack();
            }
        });
        _viewModel.get_errorMsg().observe(getViewLifecycleOwner(), this::showErrorAlertDialog);

    }


    private void login(){
        _viewBinding.btnLogin.setEnabled(false);

        String user =_viewBinding.editUserName.getText().toString().trim();
        String pass = _viewBinding.editPass.getText().toString().trim();
        if(user.isEmpty()){
            _viewBinding.txtInputLoginlayout.setError("User name can't be empty");
            return;
        }

        if(pass.isEmpty()) {
            _viewBinding.txtInputPasslayout.setError("Password can't be empty");
            return;
        }

        showProgress(true);

        if(user.contains("@")){
            user = user.split("@")[0];
        }
        _viewModel.setUserName(user);
        _viewModel.setPassword(pass);
        _viewModel.loginUsingWebService(user, pass);
    }

    public void saveUserDetails(AuthResponse response) {
        SharedPreferences.Editor editor =requireContext()
                .getSharedPreferences(MyApp.SHEARED_PREF, Context.MODE_PRIVATE)
                .edit();

       // editor.putBoolean(Constants.USER_IS_AUTH,response.isResponse()); //in completeLogin()
        editor.putString(Constants.USER_NAME,_viewModel.getUserName() );
        editor.putString(Constants.USER_PASS,_viewModel.getPassword());
        editor.putString(Constants.USER_CPF, response.getCpf_Number());
        editor.putString(Constants.USER_EMAIL, response.getEmail());
        editor.putString(Constants.USER_BUSINESS_AREA,response.getBusiness_Area());
        editor.putString(Constants.USER_BA_NAME, response.getBa_Name());
        editor.putString(Constants.USER_GSTIN,response.getGstin());
        editor.putString(Constants.USER_GSTN_LOC,response.getGstn_Location());
        editor.putString(Constants.USER_APK_VERSION_NO,response.getAPK_Version_No());
        editor.putString(Constants.USER_ACCESS,response.getUser_Acess());
        editor.apply();
    }

    private void completeLogin(boolean result){
        requireContext()
                .getSharedPreferences(MyApp.SHEARED_PREF, Context.MODE_PRIVATE)
                .edit()
                .putBoolean(Constants.USER_IS_AUTH,result)
                .apply();
    }

    private void addAccountToAndroidSystem(){
        SharedPreferences sp =getContext().getSharedPreferences(MyApp.SHEARED_PREF,Context.MODE_PRIVATE);
        String username = sp.getString(Constants.USER_NAME,"");
        String passWord = sp.getString(Constants.USER_PASS,"");
        if(username.isEmpty() || passWord.isEmpty()){
            showErrorAlertDialog("Account detail required to register in system");
            return;
        }

        boolean result = MyAccountManager.AddAccount(requireActivity(), username,passWord);

//        if(!result){
//            MyAccountManager.getManager(requireActivity()) for future update
//        }


        completeLogin(true);
    }


    private void showSnackBar(String msg, @ColorInt int color){
        Snackbar.make(requireView(), msg, Snackbar.LENGTH_LONG)
                .setBackgroundTint(color)
                .show();
    }

    private void showProgress(boolean show){
        _viewBinding.transBackground.setVisibility(show ? View.VISIBLE : View.GONE);
        _viewBinding.progressBar11.setVisibility(show ? View.VISIBLE : View.GONE);
        _viewBinding.loadingTxt3.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    public void showErrorAlertDialog(String errorMsg) {
        showProgress(false);
        _viewBinding.btnLogin.setEnabled(true);

        new AlertDialog.Builder(requireContext())
                .setTitle("Login failed")
                .setIcon(R.drawable.ic_outline_dangerous_red_24)
                .setMessage(errorMsg)
                .setPositiveButton("OK",null)
                .setCancelable(false)
                .show();

    }






    @Override
    public void onDestroy() {
        super.onDestroy();
        _viewModel = null;
        _viewBinding = null;
    }
}