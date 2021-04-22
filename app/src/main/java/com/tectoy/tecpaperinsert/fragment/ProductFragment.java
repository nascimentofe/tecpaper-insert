package com.tectoy.tecpaperinsert.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tectoy.tecpaperinsert.R;
import com.tectoy.tecpaperinsert.activity.NewProductActivity;
import com.tectoy.tecpaperinsert.api.TecpaperRestClient;
import com.tectoy.tecpaperinsert.model.Product;

import java.util.List;

/**
 * @company TECTOY
 * @department development and support
 * @author nascimentofe
 *
 */

public class ProductFragment extends Fragment {

    RecyclerView recyclerProducts;
    FloatingActionButton fab;
    ProgressBar progressBar;
    TecpaperRestClient client;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup vProduct = (ViewGroup) inflater.inflate(R.layout.fragment_product, container, false);

        startViews(vProduct);

        startRecyclerView();

        return vProduct;
    }

    @Override
    public void onResume() {
        startRecyclerView();
        super.onResume();
    }

    private void startViews(ViewGroup vProduct) {
        progressBar = (ProgressBar) vProduct.findViewById(R.id.progressBarListProduct);
        recyclerProducts = (RecyclerView) vProduct.findViewById(R.id.recyclerProduct);
        recyclerProducts.setHasFixedSize(true);
        recyclerProducts.setLayoutManager(new LinearLayoutManager(getContext()));



        fab = (FloatingActionButton) vProduct.findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent i = new Intent(getContext(), NewProductActivity.class);
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), view, ViewCompat.getTransitionName(view));
            startActivity(i, options.toBundle());
        });
    }

    private void excluirItem(Product product) {
        client.deleteProduct(recyclerProducts, product.getId(), progressBar);
    }

    private void startRecyclerView() {
        client = new TecpaperRestClient(getContext(), getActivity());
        client.getProductsToListView(recyclerProducts, progressBar);
    }

    private void hideStatusBarAndNavigationBar() {
        View decorView = getActivity().getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION // hide nav bar
//                | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
    }

}
