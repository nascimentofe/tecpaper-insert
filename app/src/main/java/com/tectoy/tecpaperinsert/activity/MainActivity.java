package com.tectoy.tecpaperinsert.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import com.scwang.wave.MultiWaveHeader;
import com.tectoy.tecpaperinsert.R;
import com.tectoy.tecpaperinsert.fragment.HomeFragment;

/**
 * @company TECTOY
 * @department development and support
 *
 * @author fenascimento
 *
 */

public class MainActivity extends AppCompatActivity {

    MultiWaveHeader wave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        startHomeFragment();
    }

    private void initViews() {
        wave = (MultiWaveHeader)findViewById(R.id.waveHeader);
        wave.setVelocity(6f);
        wave.setProgress(.8f);
        wave.isRunning();
        wave.setGradientAngle(360);
        wave.setWaveHeight(40);
        wave.setColorAlpha(.5f);
    }

    @Override
    protected void onStart() {
        hideStatusBarAndNavigationBar();
        super.onStart();
    }

    private void hideStatusBarAndNavigationBar() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION // hide nav bar
//                | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    public void startHomeFragment(){
        HomeFragment home = new HomeFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragmentContainer, home, "Home");
        ft.commit();
    }
}