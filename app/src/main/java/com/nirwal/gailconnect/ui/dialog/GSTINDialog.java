package com.nirwal.gailconnect.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.nirwal.gailconnect.Constants;
import com.nirwal.gailconnect.MyApp;
import com.nirwal.gailconnect.databinding.DialogGstinBinding;

public class GSTINDialog extends DialogFragment {

    private DialogGstinBinding _viewBinding;
    private IOnClickListener _listener;

    public GSTINDialog() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        SharedPreferences shpf= getActivity().getSharedPreferences(MyApp.SHEARED_PREF, Context.MODE_PRIVATE);
        String state = shpf.getString(Constants.USER_GSTN_LOC,"Sync required");
        String ba = shpf.getString(Constants.USER_BA_NAME,"Sync required");
        String GSTIN = shpf.getString(Constants.USER_GSTIN,"Sync required");

        _viewBinding = DialogGstinBinding.inflate(getLayoutInflater());

        _viewBinding.gstinDialogTitle.setText("My GSTIN");
        _viewBinding.gstinState.setText(state);
        _viewBinding.gstinBa.setText(ba);
        _viewBinding.gstinGstin.setText(GSTIN);

        AlertDialog.Builder builder= new AlertDialog.Builder(requireContext());
        builder//.setTitle("GSTIN")
                .setView(_viewBinding.getRoot())
                .setPositiveButton("Ok", null);

        return builder.create();
    }

    public void addOnClickListener(IOnClickListener listener){
        this._listener = listener;
    }

    public interface IOnClickListener{
        void onShareClick();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        _viewBinding = null;
    }
}
