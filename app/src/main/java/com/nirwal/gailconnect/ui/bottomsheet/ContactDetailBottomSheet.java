package com.nirwal.gailconnect.ui.bottomsheet;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.Snackbar;
import com.nirwal.gailconnect.MyApp;
import com.nirwal.gailconnect.MyIntentManager;
import com.nirwal.gailconnect.MyPermissionManager;
import com.nirwal.gailconnect.R;
import com.nirwal.gailconnect.databinding.BottomSheetContactDetailsBinding;
import com.nirwal.gailconnect.modal.Contact;
import com.nirwal.gailconnect.tasks.ImageLoader;

/**
 * <p>A fragment that shows a list of items as a modal bottom sheet.</p>
 * <p>You can show this modal bottom sheet from your activity like this:</p>
 * <pre>
 *     InverterCalculatorListDialogFragment.newInstance(data).show(getSupportFragmentManager(), "dialog");
 * </pre>
 */
public class ContactDetailBottomSheet extends BottomSheetDialogFragment {

    // TODO: Customize parameter argument names
    private static final String ARG_DATA = "DATA";
    private BottomSheetContactDetailsBinding _viewBinding;
    private Contact _contact;
    private String _mobileNumber;

    // TODO: Customize parameters
    public static ContactDetailBottomSheet newInstance(Contact contact) {
        ContactDetailBottomSheet fragment = new ContactDetailBottomSheet();
        final Bundle args = new Bundle();
        args.putParcelable(ARG_DATA, contact);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        _viewBinding = BottomSheetContactDetailsBinding.inflate(inflater,container, false);

        return _viewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        _contact =  getArguments().getParcelable(ARG_DATA);

        updateUi();

        updateUi2();
    }

    private void updateUi(){
        _viewBinding.contactName.setText(_contact.getEmp_Name());
        _viewBinding.contactDesig.setText(_contact.getDesignation());
        _viewBinding.contactDeptLoc.setText(_contact.getDepartment()+", "+_contact.getLocation() );
        _viewBinding.contactCpf.setText("CPF: "+_contact.getEmp_no());
        _viewBinding.txtNameFirstChar.setText(String.valueOf(_contact.getEmp_Name().trim().charAt(0)));
//        Glide.with(requireContext())
//                .load(MyApp.IMG_URL+_contact.getIMAGE())
//                .fitCenter()
//                .listener(new RequestListener<Drawable>() {
//                    @Override
//                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                        _viewBinding.txtNameFirstChar.setVisibility(View.INVISIBLE);
//                        return false;
//                    }
//                })
//                .placeholder(R.drawable.color_1_bg)
//                .into(_viewBinding.profilePic);

        ImageLoader.with(getActivity().getApplication())
                .load(MyApp.IMG_URL+_contact.getIMAGE())
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


    private void updateUi2(){
        if(!_contact.getTelNo().trim().isEmpty()) {
            addItem("Work", _contact.getTelNo());
        }

        if(!_contact.getMobileNo().trim().isEmpty()) {
            addItem("Mobile", _contact.getMobileNo());
        }

        if(!_contact.getMobileNo1().trim().isEmpty()){
            addItem("Mobile1", _contact.getMobileNo1());
        }

        if(!_contact.getOfficeTel().trim().isEmpty()){
            addItem("Office tel", _contact.getOfficeTel());
        }

        if(!_contact.getLGailTel().trim().isEmpty()){
            addItem("LGail tel", _contact.getLGailTel());
        }

        if(!_contact.getLTel().trim().isEmpty()){
            addItem("L tel", _contact.getLTel());
        }

        if(!_contact.getEmails().trim().isEmpty()){
            addEmailItem("Email", _contact.getEmails());
        }

        if(!_contact.getDateOfBirth().trim().isEmpty()){
            addTextItem("DOB", _contact.getDateOfBirth());
        }

        if(!_contact.getHBJExt().trim().isEmpty()){
            addTextItem("HBJ EXT", _contact.getHBJExt());
        }

        if(!_contact.getOfficeExt().trim().isEmpty()){
            addTextItem("OFFICE EXT", _contact.getOfficeExt());
        }



    }

    private void addItem(String title, String data){
        View v= getLayoutInflater().inflate(R.layout.item_phone,_viewBinding.phoneContainer, false);
        ((TextView)v.findViewById(R.id.data_title)).setText(title);
        ((TextView)v.findViewById(R.id.data_data)).setText(data);
        v.findViewById(R.id.btn_call).setOnClickListener(v1 -> startCall(data));
        v.findViewById(R.id.btn_msg).setOnClickListener(v1 -> composeSmsMessage(data));
        _viewBinding.phoneContainer.addView(v);

    }
    private void addEmailItem(String title, String data){
        View v= getLayoutInflater().inflate(R.layout.item_phone,_viewBinding.phoneContainer, false);
        ((TextView)v.findViewById(R.id.data_title)).setText(title);
        ((TextView)v.findViewById(R.id.data_data)).setText(data);
        v.findViewById(R.id.btn_call).setVisibility(View.GONE);
        ImageView imageView = v.findViewById(R.id.btn_msg);
        imageView.setImageResource(R.drawable.ic_baseline_markunread_24);
        imageView.setOnClickListener(v1 -> openEmail(data));
        _viewBinding.phoneContainer.addView(v);

    }

    private void addTextItem(String title, String data){
        View v= getLayoutInflater().inflate(R.layout.item_phone,_viewBinding.phoneContainer, false);
        ((TextView)v.findViewById(R.id.data_title)).setText(title);
        ((TextView)v.findViewById(R.id.data_data)).setText(data);

        v.findViewById(R.id.btn_call).setVisibility(View.GONE);
        v.findViewById(R.id.btn_msg).setVisibility(View.GONE);

        _viewBinding.phoneContainer.addView(v);

    }

    private void startCall(String number){
        _mobileNumber = number;
        if(MyPermissionManager.isCallPermissionGranted(requireContext())){
            performCall(number);

        }
        else{
             requestCallPermission();
        }
    }

    private void performCall(String number){
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+number));//change the number
        startActivity(callIntent);
    }

    public void requestCallPermission(){

        if(shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)){
            new AlertDialog.Builder(requireContext())
                    .setTitle("Permission needed")
                    .setMessage("Call permission required to perform this action")
                    .setCancelable(false)
                    .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestPermissions(new String[]{Manifest.permission.CALL_PHONE},
                                    MyPermissionManager.call_permission_request );
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .create()
                    .show();
        } else {
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE},
                    MyPermissionManager.call_permission_request );
        }
    }

    public void composeSmsMessage(String phoneNumber) {

        Intent intent = new Intent(Intent.ACTION_SENDTO,Uri.parse("smsto:"+phoneNumber));
        startActivity(intent);

    }

    public void openEmail(String address){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] { address });
        intent.putExtra(Intent.EXTRA_SUBJECT, "subject");
        intent.putExtra(Intent.EXTRA_TEXT, "Gail connect v3 mail");
        startActivity(Intent.createChooser(intent, ""));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MyPermissionManager.call_permission_request: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(requireContext(), "Permission granted",Toast.LENGTH_LONG)
                            .show();
                    performCall(_mobileNumber);
                } else {
                    showCallPermissionJustification();
                }
                break;
            }

        }
    }



    public void showCallPermissionJustification(){

        if(shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)){
            requestCallPermission();
        }else {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Permission denied")
                    .setMessage("Without this permission the app is unable to process 'CALL'. \n" +
                            "If you choose re-try then: Permissions> Phone> Allow")
                    .setPositiveButton("RE-TRY", (dialog, which) -> {

                        startActivity(MyIntentManager.openSettingForSelf(requireContext()));
                    })
                    .setNegativeButton("Exit", null)
                    .create()
                    .show();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        _viewBinding = null;
        _contact = null;
    }
}