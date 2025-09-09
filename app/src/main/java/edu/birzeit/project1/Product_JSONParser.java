package edu.birzeit.project1;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class Product_JSONParser {
    public static List<Product> getObjectFromJson(String json) {
        List<Product> products = new ArrayList<>();
        try {
            JSONObject root = new JSONObject(json); // parse root object
            JSONArray jsonArray = root.getJSONArray("categories"); // get array

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                Product product = new Product();
                product.setID(jsonObject.getInt("id"));
                product.setName(jsonObject.getString("name"));
                product.setIconURL(jsonObject.getString("icon_url"));

                products.add(product);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return products;
    }
}
