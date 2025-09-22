package edu.birzeit.project1.database;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

import edu.birzeit.project1.entities.Product;

public class Product_JSONParser {
    public static List<Product> getObjectFromJson(String json) {
        List<Product> products = new ArrayList<>();
        try {
            if (json == null)
                return null;
            JSONObject root = new JSONObject(json);
            JSONArray jsonArray = root.getJSONArray("categories");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                Product product = new Product();
                product.setID(jsonObject.getInt("id"));
                product.setName(jsonObject.getString("name"));
                product.setIconURL(jsonObject.getString("icon_url"));

                products.add(product);
            }
        } catch (JSONException e) {
            return null;
        }
        return products;
    }
}
