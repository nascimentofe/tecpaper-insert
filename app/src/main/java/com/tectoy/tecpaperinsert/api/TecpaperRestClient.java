package com.tectoy.tecpaperinsert.api;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.loopj.android.http.*;
import com.tectoy.tecpaperinsert.list.ProductListAdapter;
import com.tectoy.tecpaperinsert.model.Product;
import com.tectoy.tecpaperinsert.utils.QueryUtils;

import org.json.JSONException;
import org.json.*;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class TecpaperRestClient {

    private static final String BASE_URL = "http://tecpaper.tk/tecpaper/public/api/products";
    private static AsyncHttpClient client = new AsyncHttpClient();
    private Context mContext;
    private Activity mActivity;

    public TecpaperRestClient(){

    }

    public TecpaperRestClient(Context context, Activity activity){
        mContext = context;
        mActivity = activity;
    }

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

    public void getProductsToListView(ListView listProducts, ProgressBar progressBar) throws JSONException {
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

    public void postProduct(ProgressBar progressBar, RequestParams params){

        TecpaperRestClient.post("", params, new JsonHttpResponseHandler(){

            @Override
            public void onStart() {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                progressBar.setVisibility(View.GONE);

                Log.i("##TESTE", response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                progressBar.setVisibility(View.GONE);
                Log.i("##TESTE", throwable.getMessage() + " : " + responseString);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                progressBar.setVisibility(View.GONE);
            }
        });

    }

}
