package com.tectoy.tecpaperinsert.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
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

/**
 * @company TECTOY
 * @department development and support
 * @author nascimentofe
 *
 */

public class ProductFragment extends Fragment {

    ListView listProducts;
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
        listProducts.setOnItemLongClickListener((parent, view, position, id) -> {

            Product product = (Product) parent.getAdapter().getItem(position);

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            AlertDialog alertDialog =  builder.setMessage("Tem certeza que deseja excluir esse produto?")
                    .setPositiveButton("Sim", (dialog, which) -> {
                        excluirItem(product);

                        hideStatusBarAndNavigationBar();
                    })
                    .setNegativeButton("Não", (dialog, which) -> {
                        dialog.dismiss();

                        hideStatusBarAndNavigationBar();
                    })
                    .create();
            alertDialog.setOnShowListener(dialog -> {
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(getContext(), R.color.orange));
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(getContext(), R.color.orange));

                hideStatusBarAndNavigationBar();
            });
            alertDialog.show();

            return false;
        });

        fab = (FloatingActionButton) vProduct.findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent i = new Intent(getContext(), NewProductActivity.class);
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), view, ViewCompat.getTransitionName(view));
            startActivity(i, options.toBundle());
        });
    }

    private void excluirItem(Product product) {
        client.deleteProduct(listProducts, product.getId(), progressBar);
    }

    private void startListView() {
        client = new TecpaperRestClient(getContext(), getActivity());
        client.getProductsToListView(listProducts, progressBar);
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
