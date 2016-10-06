package com.b2infosoft.addley;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.b2infosoft.addley.global.*;

public class FirstRun extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(com.b2infosoft.addley.global.Login.getDemo(this, Tag.DEMO_VIEW)==1){
            startActivity(new Intent(this,Splash_1.class));
            finish();
        }else{
            startActivity(new Intent(this,SplashDemo.class));
            finish();
        }
    }
}
