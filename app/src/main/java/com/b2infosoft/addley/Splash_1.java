package com.b2infosoft.addley;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class Splash_1 extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash_1);
        zoom_in();
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {

                }
                Intent intent = new Intent(Splash_1.this, Main2.class);
                startActivity(intent);
                finish();
            }
        });
        th.start();
    }

    public void zoom_in() {
        ImageView image = (ImageView) findViewById(R.id.imageView2);
        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in);
        image.startAnimation(animation1);
    }

}
