package com.tectoy.tecpaperinsert.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.app.SharedElementCallback;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback;
import com.google.android.material.transition.platform.MaterialElevationScale;
import com.koushikdutta.ion.Ion;
import com.tectoy.tecpaperinsert.R;
import com.tectoy.tecpaperinsert.activity.NewProductActivity;
import com.tectoy.tecpaperinsert.list.ProductListAdapter;
import com.tectoy.tecpaperinsert.list.QueryUtils;
import com.tectoy.tecpaperinsert.model.Product;

import java.util.ArrayList;

public class ProductFragment extends Fragment {

    ListView listProducts;
    FloatingActionButton fab;

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

        Ion.with(getContext())
                .load("http://tecpaper.tk/tecpaper/public/api/products/")
                .asJsonArray()
                .setCallback((e, result) -> {
                    if(result != null){
                        ArrayList<Product> products = QueryUtils.extractProducts(result);
                        ProductListAdapter adapter = new ProductListAdapter(getActivity(), products);
                        listProducts.setAdapter(adapter);
                    }
                });
    }

    public void resetList(){
        listProducts.setAdapter(null);
    }

}
