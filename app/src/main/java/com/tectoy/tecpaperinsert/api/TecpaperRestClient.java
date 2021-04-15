package com.tectoy.tecpaperinsert.api;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.loopj.android.http.*;
import com.tectoy.tecpaperinsert.list.ProductListAdapter;
import com.tectoy.tecpaperinsert.model.Product;
import com.tectoy.tecpaperinsert.utils.QueryUtils;

import org.json.JSONException;
import org.json.*;

import java.io.FileInputStream;
import java.util.ArrayList;

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
        client.delete(url, params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }


    // PUBLIC METHODS
    public void getProductsToListView(ListView listProducts, ProgressBar progressBar) {

        listProducts.setAdapter(null);

        TecpaperRestClient.get("", null, new JsonHttpResponseHandler(){
            @Override
            public void onStart() {
                progressBar.setVisibility(View.VISIBLE);
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                ArrayList<Product> list = QueryUtils.extractProducts(response);
                ProductListAdapter adapter = new ProductListAdapter(mActivity, list);
                listProducts.setAdapter(adapter);
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

    public void deleteProduct(ListView listView, long id, ProgressBar progressBar){
        RequestParams params = new RequestParams("id", id);
        TecpaperRestClient.delete("", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                getProductsToListView(listView, progressBar);

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(mContext, "Falha: " + statusCode, Toast.LENGTH_LONG).show();
            }
        });
    }
}
