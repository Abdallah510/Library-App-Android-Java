package edu.birzeit.project1;

import android.app.Activity;
import android.os.AsyncTask;

import java.util.List;


public class ConnectionAsyncTask extends AsyncTask<String, String,
        String> {

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
        List<Product> products = Product_JSONParser.getObjectFromJson(s);
        ((WelcomeActivity)activity).fillProducts(products);

    }
}