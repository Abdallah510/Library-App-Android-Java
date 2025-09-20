package edu.birzeit.project1.prelogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import edu.birzeit.project1.librarian_fragment.MainLibrarianActivity;
import edu.birzeit.project1.student_fragments.MainActivity;
import edu.birzeit.project1.R;
import edu.birzeit.project1.entities.Product;
import edu.birzeit.project1.student_fragments.ConnectionAsyncTask;


public class WelcomeActivity extends AppCompatActivity {

    Button btnConnect;
    ProgressBar progressBar;
    static String APILINK = "https://mocki.io/v1/af54077b-7fc7-447f-b131-89d664069995";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);


        btnConnect = findViewById(R.id.connect_button);
        ImageView ivLogo = findViewById(R.id.logo);
        LinearLayout llRoot = findViewById(R.id.root_layout);
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation fadeInQuick = AnimationUtils.loadAnimation(this, R.anim.fade_in_quick);

        TextView tvTitle = findViewById(R.id.intro_title);
        TextView tvDescription = findViewById(R.id.intro_description);

        btnConnect.setVisibility(View.INVISIBLE);
        tvTitle.setVisibility(View.INVISIBLE);
        tvDescription.setVisibility(View.INVISIBLE);

        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
//                ivLogo.post(() -> llRoot.removeView(ivLogo));
                btnConnect.startAnimation(fadeInQuick);
                tvTitle.startAnimation(fadeInQuick);
                tvDescription.startAnimation(fadeInQuick);
                btnConnect.setVisibility(View.VISIBLE);
                tvTitle.setVisibility(View.VISIBLE);
                tvDescription.setVisibility(View.VISIBLE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        ivLogo.startAnimation(fadeIn);
        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivLogo.setVisibility(View.GONE);
                ConnectionAsyncTask connectionAsyncTask = new ConnectionAsyncTask(WelcomeActivity.this);
                try {
                    connectionAsyncTask.execute(APILINK);
                    // https://api.jsonsilo.com/demo/3e0eced2-0dea-4967-acc6-1d123e99e709
                    // https://mocki.io/v1/dbf9b62e-ec38-4726-ab95-407098ba5974
                    Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class); startActivity(intent);
                   startActivity(intent);

                }
                catch (Exception e){
                    Toast.makeText(WelcomeActivity.this, "Couldn't Connect...", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    public void setButtonText(String text) {
        btnConnect.setText(text);
    }

    public void fillProducts(List<Product> products) {
        LinearLayout linearLayout = findViewById(R.id.root_layout);
        linearLayout.removeAllViews();
        for (int i = 0; i < products.size(); i++) {
            TextView textView = new TextView(this);
            textView.setText(products.get(i).toString());
            textView.setTextColor(Color.WHITE);
            linearLayout.addView(textView);
        }
    }
    public void setProgress(boolean progress) {
        if (progress) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }
}




