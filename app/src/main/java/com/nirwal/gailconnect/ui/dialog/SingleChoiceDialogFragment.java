package com.nirwal.gailconnect.ui.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.nirwal.gailconnect.R;

public class SingleChoiceDialogFragment extends DialogFragment {

    private IOnSingleItemChoiceListener _listener;
    private int checkedItem=0;
    private String[] _list;
    private String _title="Dialog";

    public SingleChoiceDialogFragment() {
    }

    public SingleChoiceDialogFragment(String title,int checkedItem) {
        this._title = title;
        this.checkedItem = checkedItem;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        _list = getResources().getStringArray(R.array.contact_filter_choice);

        AlertDialog.Builder builder= new AlertDialog.Builder(requireContext());
        builder.setTitle(_title)
                .setSingleChoiceItems(_list, checkedItem, (dialog, which) -> {
                    checkedItem = which;
                })
                .setPositiveButton("Filter", ((dialog, which) -> {
                    if(_listener!=null){
                        _listener.onPositiveBtnClick(_list,checkedItem);
                    }
                }))
                .setNegativeButton("Cancel",((dialog, which) -> {
                    if(_listener!=null) _listener.onNegativeBtnClick();
                }));



        return builder.create();
    }

    public void setSingleItemChoiceListener(IOnSingleItemChoiceListener listener){
      this._listener = listener;
    }

    public interface IOnSingleItemChoiceListener{
      void onPositiveBtnClick(String[] list, int position);
      void onNegativeBtnClick();
    }

}