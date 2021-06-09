package com.nirwal.gailconnect.ui.birthdayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.nirwal.gailconnect.Constants;
import com.nirwal.gailconnect.MyApp;
import com.nirwal.gailconnect.MyIntentManager;
import com.nirwal.gailconnect.R;
import com.nirwal.gailconnect.databinding.FragmentBirthdayWishBinding;
import com.nirwal.gailconnect.modal.Contact;
import com.nirwal.gailconnect.tasks.ImageLoader;
import com.nirwal.gailconnect.ui.home.HomeViewModel;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class BirthdayWishFragment extends Fragment implements View.OnClickListener {


    public static final String ARG_CONTACT = "person";

    // TODO: Rename and change types of parameters

    private FragmentBirthdayWishBinding _viewBinding;
    private Contact person;
    private String _msg;
    private PopupMenu _menu;

    public BirthdayWishFragment() {
        // Required empty public constructor
    }

    public static BirthdayWishFragment newInstance(Contact arg1) {
        BirthdayWishFragment fragment = new BirthdayWishFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_CONTACT, arg1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            person = getArguments().getParcelable(ARG_CONTACT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        _viewBinding = FragmentBirthdayWishBinding.inflate(inflater, container, false);
        _viewBinding.messageBtn.setOnClickListener(this);
        _viewBinding.emailBtn.setOnClickListener(this);
        updateUI();
        initPopupMenu();
        return _viewBinding.getRoot();
    }


    private void updateUI(){
        if(person==null) return;
        _viewBinding.txtPersionName.setText(person.getEmp_Name());

        if(!person.getIMAGE().trim().isEmpty()){
            ImageLoader.with(getActivity().getApplication())
            .load(MyApp.IMG_URL+person.getIMAGE())
            .into(_viewBinding.imageView3);
        }

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(requireContext());
        String msg= sp.getString("default_birthday_wish_msg",getString(R.string.birthday_wish_default_msg));
        _viewBinding.birthdayMsg.setText(msg);

    }


    @Override
    public void onClick(View v) {
        SharedPreferences sp = requireContext().getSharedPreferences(MyApp.SHEARED_PREF,Context.MODE_PRIVATE);
        String senderName = sp.getString(HomeViewModel.EMPLOY_NAME,"Your well wisher");
        String desig = sp.getString(HomeViewModel.EMPLOY_DESIG,"");
        String dep = sp.getString(Constants.USER_DEPARTMENT,"India");
        String loc = sp.getString(Constants.USER_WORK_LOCATION,"");
        _msg = "Dear "+person.getEmp_Name()+",\n\n"+
                _viewBinding.birthdayMsg.getText().toString()+
                "\n\n Regards,\n\n" +
                senderName+"\n"+
                desig+"("+dep+")\n"+
                loc;


     if(v == _viewBinding.emailBtn){
         MyIntentManager.openEmail(
                 requireContext(),
                 person.getEmails(),
                 "Birthday wish",
                 _msg
         );
         return;
     }

     if(v == _viewBinding.messageBtn){
         if(_menu.getMenu().size()<=0){
             Toast.makeText(requireContext(),"No mobile number found!",Toast.LENGTH_LONG)
                     .show();
         }
        _menu.show();
         return;
     }

    }


    void initPopupMenu(){
        _menu =  new PopupMenu(requireContext(), _viewBinding.messageBtn);
        Menu subMenu =_menu.getMenu();

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

        if(!person.getTelNo().trim().isEmpty()){
            subMenu.add(person.getTelNo()).setIcon(R.drawable.ic_baseline_send_24);
        }

        if(!person.getMobileNo().trim().isEmpty()){
            subMenu.add(person.getMobileNo()).setIcon(R.drawable.ic_baseline_send_24);
        }
        if(!person.getMobileNo1().trim().isEmpty()){
            subMenu.add(person.getMobileNo1()).setIcon(R.drawable.ic_baseline_send_24);
        }



//            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P){
//                menu.setForceShowIcon(true);
//            }else {
//                Method menuMethod = null;
//                try {
//                    menuMethod = _menu.getMenu().getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
//                    menuMethod.setAccessible(true);
//                    menuMethod.invoke(_menu.getMenu(), true);
//                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
//                    e.printStackTrace();
//                }
//            }
            _menu.setOnMenuItemClickListener(item -> {

                startActivity(MyIntentManager.sendSMS(item.getTitle().toString(),_msg));
                return true;
            });


    }
}