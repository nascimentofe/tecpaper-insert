package com.tectoy.tecpaperinsert.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tectoy.tecpaperinsert.R;
import com.tectoy.tecpaperinsert.activity.NewProductActivity;
import com.tectoy.tecpaperinsert.api.TecpaperRestClient;
import com.tectoy.tecpaperinsert.list.ProductListAdapter;
import com.tectoy.tecpaperinsert.utils.QueryUtils;
import com.tectoy.tecpaperinsert.model.Product;

import org.json.JSONException;

import java.util.ArrayList;

public class ProductFragment extends Fragment {

    ListView listProducts;
    FloatingActionButton fab;
    ProgressBar progressBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup vProduct = (ViewGroup) inflater.inflate(R.layout.fragment_product, container, false);

        startViews(vProduct);

        startListView();

        return vProduct;
    }

    @Override
    public void onResume() {
        startListView();
        super.onResume();
    }

    private void startViews(ViewGroup vProduct) {
        progressBar = (ProgressBar) vProduct.findViewById(R.id.progressBarListProduct);
        listProducts = (ListView) vProduct.findViewById(R.id.listProduct);
        listProducts.setOnItemClickListener((parent, view, position, id) -> {
            Toast.makeText(getContext(), "aqui foi, pos: " + position, Toast.LENGTH_LONG).show();
        });
        fab = (FloatingActionButton) vProduct.findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent i = new Intent(getContext(), NewProductActivity.class);
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), view, ViewCompat.getTransitionName(view));
            startActivity(i, options.toBundle());
        });
    }

    private void startListView() {
        resetList();

        try {
            TecpaperRestClient client = new TecpaperRestClient(getContext(), getActivity());
            client.getProductsToListView(listProducts, progressBar);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void resetList(){
        listProducts.setAdapter(null);
    }

}
