package com.tectoy.tecpaperinsert.list;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tectoy.tecpaperinsert.model.Product;

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

}
