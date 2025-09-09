package edu.birzeit.project1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);


        Button btnConnect = findViewById(R.id.connect_button);
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
                Toast.makeText(WelcomeActivity.this, "Connecting to library...", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
