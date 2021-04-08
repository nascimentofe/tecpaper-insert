package com.tectoy.tecpaperinsert.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tectoy.tecpaperinsert.model.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class QueryUtils {

    public static ArrayList<Product> extractProducts(JsonArray jsonArray){

        ArrayList<Product> products = new ArrayList<>();

        for(int i=0; i < jsonArray.size(); i++){
            JsonObject obj = jsonArray.get(i).getAsJsonObject();

            products.add(new Product(
                    obj.get("id").getAsLong(),
                    obj.get("name").getAsString(),
                    obj.get("description").getAsString(),
                    obj.get("price").getAsDouble(),
                    obj.get("image").getAsString()

            ));
        }

        return products;
    }

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
