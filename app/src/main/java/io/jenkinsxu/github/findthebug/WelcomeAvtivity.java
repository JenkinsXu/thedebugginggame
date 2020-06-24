package io.jenkinsxu.github.findthebug;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class WelcomeAvtivity extends AppCompatActivity {

    private static int SPLASH_SCREEN = 4000;

    Handler mHandler = new Handler();
    Runnable endAnimationRunnable = new Runnable() {
        @Override
        public void run() {
            endAnimation();
        }
    };

    Animation topAnim, bottomAnim;
    ImageView imageView;
    TextView logo, name;
    Button skipButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);

        skipButton = findViewById(R.id.skip_button);
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endAnimation();
            }
        });

        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        imageView = findViewById(R.id.questioning_bug_image);
        logo = findViewById(R.id.title_text);
        name = findViewById(R.id.developer_text);

        imageView.setAnimation(bottomAnim);
        logo.setAnimation(topAnim);
        name.setAnimation(topAnim);

        mHandler.postDelayed(endAnimationRunnable, SPLASH_SCREEN);
    }

    private void endAnimation() {
        Intent intent = MenuActivity.makeIntent(WelcomeAvtivity.this);
        startActivity(intent);
        finish();
        mHandler.removeCallbacks(endAnimationRunnable);
    }
}