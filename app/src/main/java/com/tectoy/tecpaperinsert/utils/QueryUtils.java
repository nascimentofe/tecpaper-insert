package com.tectoy.tecpaperinsert.utils;

import com.tectoy.tecpaperinsert.model.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @company TECTOY
 * @department development and support
 * @author nascimentofe
 *
 */

public class QueryUtils {

    public static ArrayList<Product> extractProducts(JSONArray jsonArray){

        ArrayList<Product> products = new ArrayList<>();

        for(int i=0; i < jsonArray.length(); i++){
            try {
                JSONObject obj = (JSONObject) jsonArray.get(i);
                products.add(new Product(
                        obj.getLong("id"),
                        obj.getString("name"),
                        obj.getString("description"),
                        obj.getDouble("price"),
                        obj.getString("image")

                ));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return products;
    }

}
