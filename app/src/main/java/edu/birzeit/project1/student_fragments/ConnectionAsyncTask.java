package edu.birzeit.project1.student_fragments;

import android.app.Activity;
import android.os.AsyncTask;

import java.util.List;

import edu.birzeit.project1.database.HttpManager;
import edu.birzeit.project1.database.Product_JSONParser;
import edu.birzeit.project1.prelogin.WelcomeActivity;
import edu.birzeit.project1.entities.Product;


public class ConnectionAsyncTask extends AsyncTask<String, String, String> {


    static List<Product> products;
    Activity activity;

    public ConnectionAsyncTask(Activity activity) {

        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {

        ((WelcomeActivity)activity).setButtonText("Connecting");
        super.onPreExecute();
        ((WelcomeActivity)activity).setProgress(true);
    }

    @Override
    protected String doInBackground(String... params) {

        String data = HttpManager.getData(params[0]);

        return data;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        ((WelcomeActivity)activity).setProgress(false);
        ((WelcomeActivity)activity).setButtonText("connected");
        products = Product_JSONParser.getObjectFromJson(s);
        ((WelcomeActivity)activity).fillProducts(products);

    }
}