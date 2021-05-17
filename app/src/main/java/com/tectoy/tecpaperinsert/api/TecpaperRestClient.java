package com.tectoy.tecpaperinsert.api;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.loopj.android.http.*;
import com.tectoy.tecpaperinsert.R;
import com.tectoy.tecpaperinsert.adapter.ProductAdapter;
import com.tectoy.tecpaperinsert.fragment.NewProductFragment;
import com.tectoy.tecpaperinsert.fragment.ProductFragment;
import com.tectoy.tecpaperinsert.sec.Security;
import com.tectoy.tecpaperinsert.utils.QueryUtils;

import org.json.JSONException;
import org.json.*;

import cz.msebera.android.httpclient.Header;

/**
 * @company TECTOY
 * @department development and support
 * @author nascimentofe
 *
 */

public class TecpaperRestClient {

    private static final String BASE_URL = "http://tecpaper.tk/tecpaper/public/api/products";
    private static final AsyncHttpClient client = new AsyncHttpClient();
    private final Context mContext;
    private final Activity mActivity;


    // CONSTRUCTOR
    public TecpaperRestClient(Context context, Activity activity){
        mContext = context;
        mActivity = activity;
    }


    // PRIVATE METHODS
    private static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }


    private static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }


    private static void delete(String url, RequestParams params, AsyncHttpResponseHandler responseHandler){
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }


    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }


    // PUBLIC METHODS
    public void getProducts(RecyclerView recyclerProducts, ProgressBar progressBar) {

        recyclerProducts.setAdapter(null);

        TecpaperRestClient.get("", null, new JsonHttpResponseHandler(){
            @Override
            public void onStart() {
                progressBar.setVisibility(View.VISIBLE);
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                ProductAdapter productAdapter = new ProductAdapter(mContext, mActivity, QueryUtils.extractProducts(response));
                recyclerProducts.setAdapter(productAdapter);

                progressBar.setVisibility(View.GONE);
            }
        });
    }

    public void postProduct(ProgressDialog dialog, RequestParams params){

        TecpaperRestClient.post("", params, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                dialog.dismiss();

                try {
                    Toast.makeText(mContext, response.getString("result"), Toast.LENGTH_LONG).show();

                    if (response.getString("result").equals("REGISTRO INSERIDO")){
                        mActivity.finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.i("##TESTE", response.toString());
            }
        });

    }

    public void deleteProduct(long id, ProgressDialog dialog){

        String url = "?id=" + id + "&pass=" + Security.ADMPASS;
        TecpaperRestClient.delete(url, null, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                dialog.dismiss();

                ProductFragment productFragment = new ProductFragment();
                FragmentManager fm = ((FragmentActivity) mActivity).getSupportFragmentManager();
                fm.beginTransaction()
                        .setCustomAnimations(
                                R.anim.right_to_left, R.anim.exit_rigth_to_left,
                                R.anim.left_to_right, R.anim.exit_left_to_rigth)
                        .addToBackStack("Home")
                        .replace(R.id.fragmentContainer, productFragment, "Product")
                        .commit();

            }
        });
    }

    public void updateProduct(ProgressDialog dialog, RequestParams params) {
        TecpaperRestClient.post("", params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                dialog.dismiss();

                try{
                    String text = response.getString("result");

                    Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();

                    if (text.equals("REGISTRO ATUALIZADO")){
                        mActivity.onBackPressed();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }


            }
        });
    }
}
