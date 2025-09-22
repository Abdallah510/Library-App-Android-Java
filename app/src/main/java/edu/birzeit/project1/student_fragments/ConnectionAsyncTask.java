package edu.birzeit.project1.student_fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.List;

import edu.birzeit.project1.database.HttpManager;
import edu.birzeit.project1.database.Product_JSONParser;
import edu.birzeit.project1.prelogin.LoginActivity;
import edu.birzeit.project1.prelogin.WelcomeActivity;
import edu.birzeit.project1.entities.Product;

public class ConnectionAsyncTask extends AsyncTask<String, String, String> {

    public static List<Product> products;
    Activity activity;

    public ConnectionAsyncTask(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        ((WelcomeActivity) activity).setButtonText("Connecting");
        ((WelcomeActivity) activity).setProgress(true);
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        return HttpManager.getData(params[0]);
    }

    @Override
    protected void onPostExecute(String s) {
        ((WelcomeActivity) activity).setProgress(false);

        products = Product_JSONParser.getObjectFromJson(s);
        ((WelcomeActivity) activity).fillProducts(products);
        WelcomeActivity.isConnected = true;
        if (products==null){
            Toast.makeText(activity, "Couldn't Connect...", Toast.LENGTH_SHORT).show();
            ((WelcomeActivity) activity).setButtonText("Connect");
            return;
        }
        ((WelcomeActivity) activity).setButtonText("connected");
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
        super.onPostExecute(s);
    }
}
