package com.tectoy.tecpaperinsert.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.koushikdutta.ion.Ion;
import com.tectoy.tecpaperinsert.R;
import com.tectoy.tecpaperinsert.list.ProductListAdapter;
import com.tectoy.tecpaperinsert.list.QueryUtils;
import com.tectoy.tecpaperinsert.model.Product;

import java.util.ArrayList;

public class ProductFragment extends Fragment {

    ListView listProducts;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup vProduct = (ViewGroup) inflater.inflate(R.layout.fragment_product, container, false);
        listProducts = (ListView) vProduct.findViewById(R.id.listProduct);
        listProducts.setOnItemClickListener((parent, view, position, id) -> {
            Toast.makeText(getContext(), "aqui foi, pos: " + position, Toast.LENGTH_LONG).show();
        });

        startListView();

        return vProduct;
    }

    @Override
    public void onResume() {
        startListView();
        super.onResume();
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
