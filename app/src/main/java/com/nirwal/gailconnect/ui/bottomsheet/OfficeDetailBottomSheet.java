package com.nirwal.gailconnect.ui.bottomsheet;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.Snackbar;
import com.nirwal.gailconnect.MyApp;
import com.nirwal.gailconnect.MyPermissionManager;
import com.nirwal.gailconnect.R;
import com.nirwal.gailconnect.databinding.BottomSheetContactDetailsBinding;
import com.nirwal.gailconnect.databinding.BottomSheetInfoBinding;
import com.nirwal.gailconnect.modal.Contact;
import com.nirwal.gailconnect.modal.Office;

import org.jetbrains.annotations.NotNull;

/**
 * <p>A fragment that shows a list of items as a modal bottom sheet.</p>
 * <p>You can show this modal bottom sheet from your activity like this:</p>
 * <pre>
 *     InverterCalculatorListDialogFragment.newInstance(data).show(getSupportFragmentManager(), "dialog");
 * </pre>
 */
public class OfficeDetailBottomSheet extends BottomSheetDialogFragment {

    // TODO: Customize parameter argument names
    private static final String ARG_DATA = "DATA";
    private BottomSheetInfoBinding _viewBinding;

    private Office _contact;

    // TODO: Customize parameters
    public static OfficeDetailBottomSheet newInstance(Office office) {
        OfficeDetailBottomSheet fragment = new OfficeDetailBottomSheet();
        final Bundle args = new Bundle();
        args.putParcelable(ARG_DATA, office);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        _viewBinding = BottomSheetInfoBinding.inflate(inflater,container, false);

        return _viewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        _contact =  getArguments().getParcelable(ARG_DATA);

        updateUi();

        //updateUi2();
    }

    private void updateUi(){
        _viewBinding.txtOfficeLoc.setText(_contact.getLocation());
        //_viewBinding.txtNameFirstChar.setText(String.valueOf(_contact.getLocation().trim().charAt(0)));

        _viewBinding.txtGailNetCode.setText("GailNetCode:" +_contact.getGailnetCode());
        //_viewBinding.contactDeptLoc.setText(_contact.getDepartment()+", "+_contact.getLocation() );
        //_viewBinding.contactCpf.setText("CPF: "+_contact.getEmp_no());

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
    }


//    private void updateUi2(){
//        if(!_contact.getTelNo().trim().isEmpty()) {
//            addItem("Work", _contact.getTelNo());
//        }
//
//        if(!_contact.getMobileNo().trim().isEmpty()) {
//            addItem("Mobile", _contact.getMobileNo());
//        }
//
//        if(!_contact.getMobileNo1().trim().isEmpty()){
//            addItem("Mobile1", _contact.getMobileNo1());
//        }
//
//        if(!_contact.getOfficeTel().trim().isEmpty()){
//            addItem("Office tel", _contact.getOfficeTel());
//        }
//
//        if(!_contact.getLGailTel().trim().isEmpty()){
//            addItem("LGail tel", _contact.getLGailTel());
//        }
//
//        if(!_contact.getLTel().trim().isEmpty()){
//            addItem("L tel", _contact.getLTel());
//        }
//
//        if(!_contact.getEmails().trim().isEmpty()){
//            addEmailItem("Email", _contact.getEmails());
//        }
//
//        if(!_contact.getDateOfBirth().trim().isEmpty()){
//            addTextItem("DOB", _contact.getDateOfBirth());
//        }
//
//        if(!_contact.getHBJExt().trim().isEmpty()){
//            addTextItem("HBJ EXT", _contact.getHBJExt());
//        }
//
//        if(!_contact.getOfficeExt().trim().isEmpty()){
//            addTextItem("OFFICE EXT", _contact.getOfficeExt());
//        }
//
//
//
//    }

    private void addItem(String title, String data){
        View v= getLayoutInflater().inflate(R.layout.item_phone,_viewBinding.infoContainer, false);
        ((TextView)v.findViewById(R.id.data_title)).setText(title);
        ((TextView)v.findViewById(R.id.data_data)).setText(data);
        v.findViewById(R.id.btn_call).setOnClickListener(v1 -> startCall(data));
        v.findViewById(R.id.btn_msg).setOnClickListener(v1 -> composeSmsMessage(data));
        _viewBinding.infoContainer.addView(v);

    }
    private void addEmailItem(String title, String data){
        View v= getLayoutInflater().inflate(R.layout.item_phone,_viewBinding.infoContainer, false);
        ((TextView)v.findViewById(R.id.data_title)).setText(title);
        ((TextView)v.findViewById(R.id.data_data)).setText(data);
        v.findViewById(R.id.btn_call).setVisibility(View.GONE);
        ImageView imageView = v.findViewById(R.id.btn_msg);
        imageView.setImageResource(R.drawable.ic_baseline_markunread_24);
        imageView.setOnClickListener(v1 -> openEmail(data));
        _viewBinding.infoContainer.addView(v);

    }

    private void addTextItem(String title, String data){
        View v= getLayoutInflater().inflate(R.layout.item_phone,_viewBinding.infoContainer, false);
        ((TextView)v.findViewById(R.id.data_title)).setText(title);
        ((TextView)v.findViewById(R.id.data_data)).setText(data);

        v.findViewById(R.id.btn_call).setVisibility(View.GONE);
        v.findViewById(R.id.btn_msg).setVisibility(View.GONE);

        _viewBinding.infoContainer.addView(v);

    }

    private void startCall(String number){

        if(MyPermissionManager.isCallPermissionGranted(requireContext())){
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:"+number));//change the number
            startActivity(callIntent);
        }
        else{
        MyPermissionManager.requestCallPermission(requireActivity());
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
                    Snackbar.make(requireView(), "Permission granted", Snackbar.LENGTH_LONG).show();
                } else {

                    Snackbar.make(requireView(), "Permission denied", Snackbar.LENGTH_LONG).show();
                }
                break;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        _viewBinding = null;
        _contact = null;
    }
}