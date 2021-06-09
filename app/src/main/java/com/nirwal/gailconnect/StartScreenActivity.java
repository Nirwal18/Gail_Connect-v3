package com.nirwal.gailconnect;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.os.Bundle;

import com.nirwal.gailconnect.ui.splash.SplashFragment;

public class StartScreenActivity extends AppCompatActivity {

    private NavController _navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
            _navController = Navigation.findNavController(this, R.id.nav_host_fragment_start_screen);
        boolean isAuth = getSharedPreferences(MyApp.SHEARED_PREF,MODE_PRIVATE)
                .getBoolean(Constants.USER_IS_AUTH,false);

        if(isAuth){
            Intent intent =  new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}