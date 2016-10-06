package com.b2infosoft.addley;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.b2infosoft.addley.global.Global;

public class Splash extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splesh);
        startAnimation();
    }
    private void startAnimation(){
        Animation animation= AnimationUtils.loadAnimation(this, R.anim.alpha);
        animation.reset();
        animation=AnimationUtils.loadAnimation(this,R.anim.translate);
        animation.reset();
        ImageView imageView=(ImageView)findViewById(R.id.imageView);
        imageView.clearAnimation();
        imageView.startAnimation(animation);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Splash.this, Main2.class);
                startActivity(intent);
                Splash.this.finish();
            }
        }, Global.SPLASH_TIME_OUT);
    }
}
