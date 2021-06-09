package com.nirwal.gailconnect;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.nirwal.gailconnect.databinding.ActivityMainBinding;
import com.nirwal.gailconnect.databse.ContactRepository;
import com.nirwal.gailconnect.modal.Contact;
import com.nirwal.gailconnect.services.ConnectionLiveData;
import com.nirwal.gailconnect.services.WebApiSyncService;
import com.nirwal.gailconnect.tasks.MyWorker;
import com.nirwal.gailconnect.tasks.Result;
import com.nirwal.gailconnect.tasks.TaskCallback;
import com.nirwal.gailconnect.ui.birthdayList.BirthdayWishFragment;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static final String ARG_1 = "ARG_1";

    private ActivityMainBinding _viewBinding;
    private BottomNavigationView _navView;
    private MainActivityViewModel _viewModel;
    private NavController _navController;
    private WebApiSyncService _syncService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_GailConnectV3);
        initLightDarkMode();


        _viewBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(_viewBinding.getRoot());
         _viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

         //used for animation show hide
        _navView = _viewBinding.navView;


        _navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_contacts, R.id.navigation_news)
                .build();

        //NavigationUI.setupActionBarWithNavController(this, _navController, appBarConfiguration);
        NavigationUI.setupWithNavController(_navView, _navController);
        _navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                animateBottomNavigation(destination.getId());


            }
        });

        checkAndUpdateNetworkStatus();
        checkIntent();

    }



    @Override
    public void onConfigurationChanged(@NonNull @NotNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int currentNightMode = newConfig.uiMode & Configuration.UI_MODE_NIGHT_MASK;

        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                // Night mode is not active, we're using the light theme
                AppCompatDelegate
                        .setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case Configuration.UI_MODE_NIGHT_YES:
                // Night mode is active, we're using dark theme
                AppCompatDelegate
                        .setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
        }
        this.recreate();
    }

    public void initLightDarkMode(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String theme = sp.getString("theme", "default");

        String[] values = getResources().getStringArray(R.array.theme_values);


       if(theme.equals(values[0])){
           //default
           AppCompatDelegate
                   .setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
       }else if(theme.equals(values[1])){
           AppCompatDelegate
                   .setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
       }
       else if(theme.equals(values[2])){
           AppCompatDelegate
                   .setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
       }
    }

    public void firstTimeSetup(){
        SharedPreferences sp = getSharedPreferences(MyApp.SHEARED_PREF, MODE_PRIVATE);
        boolean isFirstTime = sp.getBoolean(Constants.IS_FIRST_TIME, true);
        boolean isAuth = sp.getBoolean(Constants.USER_IS_AUTH, false);

        if(isFirstTime && isAuth){
            performSync();
            // should be called after sync complete
            sp.edit().putBoolean(Constants.IS_FIRST_TIME, false)
            .apply();
        }

    }


    private void animateBottomNavigation(int destId){
        if(destId== R.id.navigation_home ||
                destId==R.id.navigation_contacts ||
                destId==R.id.navigation_news){
            if(barDown){
               slideUp(_navView);
            }
            return;
        }
        slideDown(_navView);

    }

    boolean barDown = false;
    private void slideUp(BottomNavigationView child) {
        child.clearAnimation();
        child.animate().translationY(0).setDuration(100);
        child.setVisibility(View.VISIBLE);

    barDown = false;
    }

    private void slideDown(BottomNavigationView child) {
        child.clearAnimation();
        child.animate().translationY(child.getHeight()).setDuration(200);
        child.postDelayed(new Runnable() {
            @Override
            public void run() {
                child.setVisibility(View.GONE);
            }
        },200 );

        barDown= true;
    }

    private void initSync(){
   //  startSyncService();
        _viewModel.getBinder().observe(this, new Observer<WebApiSyncService.MyBinder>() {
            @Override
            public void onChanged(WebApiSyncService.MyBinder myBinder) {
                if(myBinder!=null){
                    _syncService = myBinder.getService();
                    _viewModel.setIsProgressBarUpdating(true);
                }
            }
        });
        _viewModel.getIsProgressBarUpdating().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                final Handler handler = new Handler();
                final Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        if(_viewModel.getIsProgressBarUpdating().getValue()){
                            if(_viewModel.getBinder().getValue() != null){ // meaning the service is bound
                                if(_syncService.isWorkFinished()){
                                    _viewModel.setIsProgressBarUpdating(false);
                                    _viewBinding.progressCont.setVisibility(View.GONE);
                                }

                                _viewBinding.progText.setText(_syncService.getTaskStatus());
                            }
                            handler.postDelayed(this, 100);
                        }
                        else{
                            handler.removeCallbacks(this);
                        }
                    }
            };

                if(aBoolean){
                    handler.postDelayed(runnable, 100);
                    _viewBinding.progressCont.setVisibility(View.VISIBLE);

                }
        }
    });
    }

    //called from home
    public void performSync(){
        initSync();
        startSyncService();
    }


    private void checkIntent(){
        String action = getIntent().getAction();
        if(getIntent().getExtras()==null)return;
        String farg = getIntent().getExtras().getString(ARG_1,"");
        if(action == Intent.ACTION_VIEW && farg.contains("birthday_fragment"))
        {
            Bundle bundle = new Bundle();
            Contact c =getIntent().getExtras().getParcelable("data");
            bundle.putParcelable(BirthdayWishFragment.ARG_CONTACT, c);
            _navController.navigate(R.id.birthdayWishFragment,bundle);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void checkAndUpdateNetworkStatus(){
       new ConnectionLiveData(this)
               .observe(this, isAvailable -> {



                  if(isAvailable){

                      showBackOnline();
                  }else {
                      showNoConnection();
                  }
               });
    };

    // network status no connection
    private void showNoConnection(){
        int color  = getResources().getColor(R.color.black);
        _viewBinding.txtNetworkStatus.setText(getString(R.string.no_connection));
        _viewBinding.txtNetworkStatus.setBackgroundColor(color);
        _viewBinding.txtNetworkStatus.animate()
                .translationY(0)
                .setDuration(200)
                .withStartAction(() -> {
                    getWindow().setStatusBarColor(color);
                });

    }

    // network status connected
    private void showBackOnline(){
        float height = getResources().getDimensionPixelSize(R.dimen.connectionTxtHeight);
        int color  = getResources().getColor(R.color.success);



        _viewBinding.txtNetworkStatus.setText(getString(R.string.back_online));
        _viewBinding.txtNetworkStatus.setBackgroundColor(color);
        _viewBinding.txtNetworkStatus.animate()
                .translationY(0)
                .setDuration(200)
                .withStartAction(() -> getWindow().setStatusBarColor(color));

        _viewBinding.txtNetworkStatus.postDelayed(() -> {
            if(_viewBinding==null) return;
            _viewBinding.txtNetworkStatus.animate()
                    .translationY(-height)
                    .setDuration(200)
                    .withEndAction(
                            () -> getWindow().setStatusBarColor(getResources().getColor(R.color.primary_dark))
                    );
        }
        ,2000);

    }

    @Override
    public void onBackPressed() {

        if(_navController.getCurrentDestination()!=null &&
                _navController.getCurrentDestination().getId()==R.id.loginFragment)
        {
         finish();
        }
        super.onBackPressed();
    }



    public void startSyncService(){
        _viewModel.setIsProgressBarUpdating(false);
        Intent serviceIntent = new Intent(this, WebApiSyncService.class);
        startService(serviceIntent);
        bindService();
    }

    private void bindService(){
        Intent serviceBindIntent =  new Intent(this, WebApiSyncService.class);
        bindService(serviceBindIntent, _viewModel.getServiceConnection(), Context.BIND_AUTO_CREATE);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        _viewModel = null;
        _viewBinding = null;
        _navController = null;
        _navView = null;
    }
}