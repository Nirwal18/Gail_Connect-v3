package com.nirwal.gailconnect.ui.contacts;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;
import com.nirwal.gailconnect.Constants;
import com.nirwal.gailconnect.MyApp;
import com.nirwal.gailconnect.MyIntentManager;
import com.nirwal.gailconnect.MyPermissionManager;
import com.nirwal.gailconnect.R;
import com.nirwal.gailconnect.adaptors.ContactAdaptor;
import com.nirwal.gailconnect.databinding.BottomSheetContactDetailsBinding;
import com.nirwal.gailconnect.databinding.FragmentContactsBinding;
import com.nirwal.gailconnect.modal.Contact;
import com.nirwal.gailconnect.tasks.Result;
import com.nirwal.gailconnect.tasks.TaskCallback;
import com.nirwal.gailconnect.ui.bottomsheet.ContactDetailBottomSheet;
import com.nirwal.gailconnect.ui.dialog.SingleChoiceDialogFragment;


import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.Permission;
import java.util.ArrayList;
import java.util.List;

public class ContactsFragment extends Fragment implements ContactAdaptor.IOnClickListener,
        View.OnClickListener {
    private static final String TAG = "ContactsFragment";

    private FragmentContactsBinding _viewBinding;

    private ContactsViewModel _contactsViewModel;
    private ContactAdaptor _adaptor;
    private PopupMenu _menu;
    private int _filterOption;
    private InputMethodManager _inputMethodManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        _contactsViewModel =
                new ViewModelProvider(this).get(ContactsViewModel.class);

        _filterOption = getActivity().getSharedPreferences(MyApp.SHEARED_PREF, Context.MODE_PRIVATE)
                .getInt(Constants.FILTER_BY,Constants.FILTER_NO_FILTER);

        _viewBinding = FragmentContactsBinding.inflate(inflater,container,false);

        _inputMethodManager = (InputMethodManager) requireContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        _adaptor = new ContactAdaptor(new ArrayList<>(),this);
        _viewBinding.contactRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        _viewBinding.contactRecycler.setAdapter(_adaptor);
        _viewBinding.fastscroll.setRecyclerView(_viewBinding.contactRecycler);

        _viewBinding.imgSearchBtn.setOnClickListener(this);
        _viewBinding.imgBackBtn.setOnClickListener(this);
        _viewBinding.imgMenuBtn.setOnClickListener(this);

        _viewBinding.txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
              _contactsViewModel.performSearch(s.toString().toUpperCase());
            }
        });



        initPopupMenu();

        return _viewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        _contactsViewModel.setFilter(_filterOption);
        _contactsViewModel.loadContact(requireActivity().getApplication());
        _contactsViewModel.getContactListLiveData().observe(getViewLifecycleOwner(),contacts -> {
            _adaptor.updateDataSet(contacts);
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

        if(v==_viewBinding.imgMenuBtn){
            _menu.show();
            return;
        }

    }

    private void initPopupMenu() {
        _menu =  new PopupMenu(requireContext(), _viewBinding.imgMenuBtn);

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P){
            _menu.setForceShowIcon(true);
        }else {
            Method menuMethod = null;
            try {
                menuMethod = _menu.getMenu().getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                menuMethod.setAccessible(true);
                menuMethod.invoke(_menu.getMenu(), true);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        _menu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.export_contact: {
                    showExportDialog();
                    return true;
                }
                case R.id.filter_all:{
                    item.setChecked(true);
                    filterContact(Constants.FILTER_NO_FILTER);
                    return true;
                }
                case R.id.filter_by_loc:{
                    item.setChecked(true);
                    filterContact(Constants.FILTER_BY_LOCATION);
                    return true;
                }
                case R.id.filter_by_loc_dep:{
                    item.setChecked(true);
                    filterContact(Constants.FILTER_BY_LOCATION_AND_DEPARTMENT);
                    return true;
                }

                default:
                    return false;
            }
        });
        _menu.inflate(R.menu.contact_more_menu);

        Menu menu = _menu.getMenu();


        switch (_filterOption){
            case Constants.FILTER_NO_FILTER:{
                menu.findItem(R.id.filter_all).setChecked(true);
                break;
            }
            case Constants.FILTER_BY_LOCATION:{
                menu.findItem(R.id.filter_by_loc).setChecked(true);
                break;
            }
            case Constants.FILTER_BY_LOCATION_AND_DEPARTMENT:{
                menu.findItem(R.id.filter_by_loc_dep).setChecked(true);
            }
        }

    }

    public void showSnakebar(String msg, int color){
        Snackbar.make(requireView(), msg,Snackbar.LENGTH_LONG).setBackgroundTint(color).show();
    }

    private void filterContact(int option){
        this._filterOption = option;

        requireActivity().getSharedPreferences(MyApp.SHEARED_PREF, Context.MODE_PRIVATE)
                .edit()
                .putInt(Constants.FILTER_BY,_filterOption)
                .apply();

        _contactsViewModel.setFilter(_filterOption);
        _contactsViewModel.loadContact(requireActivity().getApplication());
    }

    private void showExportDialog(){
        boolean isPermission = ActivityCompat
                .checkSelfPermission(requireContext(),Manifest.permission.WRITE_CONTACTS)
                == PackageManager.PERMISSION_GRANTED;
        if(isPermission){
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Confirmation")
                    .setMessage("Do you want to export current contact list.")
                    .setPositiveButton("Export", (dialog, which) -> {
                        exportContact();
                    })
                    .setNegativeButton("Cancel",null)
                    .show();
        }
        else {
            requestWriteContactPermission();
        }

    }

    private void requestWriteContactPermission(){

        if(shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CONTACTS)){
            new AlertDialog.Builder(requireContext())
                    .setTitle("Permission needed")
                    .setMessage("Write contact permission required to perform this action")
                    .setCancelable(false)
                    .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestPermissions(new String[]{Manifest.permission.WRITE_CONTACTS},
                                    MyPermissionManager.contact_permission_request );
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .create()
                    .show();
        } else {
            requestPermissions(new String[]{Manifest.permission.WRITE_CONTACTS},
                    MyPermissionManager.contact_permission_request);
        }
    }

    public void showWriteContactPermissionJustification(){

        if(shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CONTACTS)){
            requestWriteContactPermission();
        }else {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Permission denied")
                    .setMessage("Without this permission the app is unable to export Contacts. \n" +
                            "If you choose re-try then: Permissions> Phone> Allow")
                    .setPositiveButton("RE-TRY", (dialog, which) -> {

                        startActivity(MyIntentManager.openSettingForSelf(requireContext()));
                    })
                    .setNegativeButton("Exit", null)
                    .create()
                    .show();
        }

    }

    private void exportContact(){
        ProgressDialog progressDialog = new ProgressDialog(requireContext());
        progressDialog.setTitle("Process");
        progressDialog.setMessage("Exporting please wait.");
        progressDialog.setCancelable(false);
        progressDialog.show();
        _contactsViewModel.exportContacts(requireActivity().getApplication(), new ContactsViewModel.IonExportListener() {
            @Override
            public void onProgressUpdate(String progTxt) {
                progressDialog.setMessage(progTxt);
            }

            @Override
            public void onComplete() {
                progressDialog.dismiss();
                showSnakebar(
                        "Contact Export success",
                        getResources().getColor(R.color.success,requireContext().getTheme())
                        );
            }
        });
    }

    private void toggleSearch(boolean show) {

        if(show){
            _viewBinding.txtTitle.setVisibility(View.GONE);
            _viewBinding.txtSearch.setVisibility(View.VISIBLE);
            _viewBinding.txtSearch.requestFocus();
            _viewBinding.imgSearchBtn.setImageResource(R.drawable.ic_baseline_close_24);
            _inputMethodManager.showSoftInput(_viewBinding.txtSearch,0);

        }else {
            _viewBinding.txtTitle.setVisibility(View.VISIBLE);
            _viewBinding.txtSearch.setText("");
            _viewBinding.txtSearch.setVisibility(View.GONE);
            _viewBinding.imgSearchBtn.setImageResource(R.drawable.ic_baseline_search_24);
            //_inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY,0);
            _inputMethodManager.hideSoftInputFromWindow(_viewBinding.txtSearch.getWindowToken(),0);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==MyPermissionManager.contact_permission_request){
            if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                showExportDialog();
                showSnakebar("Permission granted",getResources().getColor(R.color.success, getContext().getTheme()));
            }
            else {
                showSnakebar("Permission denied",getResources().getColor(R.color.danger, getContext().getTheme()));
                showWriteContactPermissionJustification();
            }
        }
    }

    @Override
    public void onRowClick(int position) {
        // hide keyboard
        _inputMethodManager.hideSoftInputFromWindow(_viewBinding.txtSearch.getWindowToken(),0);

        Contact contact = _contactsViewModel.getContactListLiveData().getValue().get(position);
        ContactDetailBottomSheet bottomSheet = ContactDetailBottomSheet.newInstance(contact);
        bottomSheet.show(getChildFragmentManager(),"Contact details");
    }


    @Override
    public void onPause() {
        super.onPause();
        _inputMethodManager.hideSoftInputFromWindow(_viewBinding.txtSearch.getWindowToken(),0);
        Log.d(TAG, "onPause: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        _inputMethodManager=null;
        _menu = null;
        _contactsViewModel = null;
        _viewBinding = null;
        _adaptor = null;
        Log.d(TAG, "onDestroy: ");
    }
}