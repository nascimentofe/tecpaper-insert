package com.tectoy.tecpaperinsert.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.tectoy.tecpaperinsert.R;

/**
 * @company TECTOY
 * @department development and support
 *
 * @author fenascimento
 *
 */

public class HomeFragment extends Fragment {

    LinearLayout btnComecar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup vHome = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);

        btnComecar =  vHome.findViewById(R.id.btnScan);
        btnComecar.setOnClickListener(v -> startProductFragment());

        return vHome;
    }

    private void startProductFragment() {
        ProductFragment product = new ProductFragment();
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragmentContainer, product, "Product");
        ft.commit();
    }
}
