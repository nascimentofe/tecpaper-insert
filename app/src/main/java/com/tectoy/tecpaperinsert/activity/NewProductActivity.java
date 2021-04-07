package com.tectoy.tecpaperinsert.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.scwang.wave.MultiWaveHeader;
import com.tectoy.tecpaperinsert.R;
import com.tectoy.tecpaperinsert.fragment.NewProductFragment;
import com.tectoy.tecpaperinsert.fragment.ProductFragment;

import java.io.Serializable;

public class NewProductActivity extends AppCompatActivity {

    MultiWaveHeader wave;
    EditText editCode;
    ProgressBar progressBar;
    Activity activity;
    String conteudo;
    TextView txtAguardandoProduto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);

        conteudo = "";

        initViews();
        initActions();

    }

    @Override
    protected void onStart() {
        hideStatusBarAndNavigationBar();
        super.onStart();
    }

    @Override
    protected void onResume() {
        editCode.requestFocus();
        super.onResume();
    }

    private void initActions() {
        editCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() > 0 && !s.toString().isEmpty()){

                    if(!editCode.getText().toString().equals("")){

                        String strConteudo = editCode.getText().toString().replace("\n", "").replace(" ", "").trim();

                        if(conteudo.length() < strConteudo.length()){
                            conteudo = s.toString();
                        }else{
                            registerProduct(
                                    editCode.getText().toString().replace("\n", "").replace(" ", "").trim());
                        }
                    }
                }
            }
        });
    }

    private void initViews() {
        startWave();
        editCode = (EditText) findViewById(R.id.editCode);
        activity = (Activity) this;
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        txtAguardandoProduto = (TextView) findViewById(R.id.txt_aguardando_produto);
    }

    private void startWave() {
        wave = (MultiWaveHeader) findViewById(R.id.waveHeaderNewProduct);
        wave.setVelocity(6f);
        wave.setProgress(.8f);
        wave.isRunning();
        wave.setGradientAngle(360);
        wave.setWaveHeight(40);
        wave.setColorAlpha(.5f);
    }

    private void registerProduct(String strProduct){
        limparActivity();

        NewProductFragment newProductFragment = new NewProductFragment();
        Bundle data = new Bundle();
        newProductFragment.setArguments(data);
        data.putString("data", strProduct);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.newProduct_container, newProductFragment, "NewProduct");
        ft.commit();
    }

    private void limparActivity() {
        progressBar.setVisibility(View.GONE);
        txtAguardandoProduto.setText("");
    }

    private void hideStatusBarAndNavigationBar() {
        View decorView = getWindow().getDecorView();
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.orange));

        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION // hide nav bar
//                | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }
}