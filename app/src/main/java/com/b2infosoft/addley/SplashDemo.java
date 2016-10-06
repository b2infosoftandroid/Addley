package com.b2infosoft.addley;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.b2infosoft.addley.adapter.TabPageAdapter;
import com.b2infosoft.addley.global.Tag;
import com.b2infosoft.addley.fragment.splash.Three;
import com.b2infosoft.addley.fragment.splash.Two;
import com.b2infosoft.addley.global.Login;
import com.b2infosoft.addley.fragment.splash.Four;
import com.b2infosoft.addley.fragment.splash.One;

import java.util.ArrayList;
import java.util.List;

public class SplashDemo extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    private ViewPager viewPager;
    private List<Fragment> fragmentList;
    private TabPageAdapter tabPageAdapter;
    private ImageView img_0, img_1, img_2;
    private TextView lets_go;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_demo);
        img_0 = (ImageView) findViewById(R.id.splash_one);
        img_1 = (ImageView) findViewById(R.id.splash_two);
        img_2 = (ImageView) findViewById(R.id.splash_three);
        lets_go = (TextView) findViewById(R.id.splash_lets_go);
        lets_go.setEnabled(false);
        lets_go.setVisibility(View.GONE);
        lets_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login.setDemo(SplashDemo.this, Tag.DEMO_VIEW, Tag.DEMO_VIEW_SHOW_YES);
                startActivity(new Intent(SplashDemo.this, Main2.class));
                finish();
            }
        });
        fragmentList = new ArrayList<>();
        fragmentList.add(new One());
        fragmentList.add(new Two());
        fragmentList.add(new Three());
        fragmentList.add(new Four());
        tabPageAdapter = new TabPageAdapter(getSupportFragmentManager(), fragmentList);
        viewPager = (ViewPager) findViewById(R.id.splash_demo_view_pager);
        viewPager.setAdapter(tabPageAdapter);
        viewPager.setOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (position == 3) {
            Login.setDemo(SplashDemo.this, Tag.DEMO_VIEW, Tag.DEMO_VIEW_SHOW_YES);
            //startActivity(new Intent(SplashDemo.this, Main2.class));
            startActivity(new Intent(SplashDemo.this, Splash_1.class));
            finish();
        }
    }

    @Override
    public void onPageSelected(int position) {
        int selected[] = {
                R.mipmap.ic_lens_black_18dp,
                R.mipmap.ic_panorama_fish_eye_black_18dp
        };
        if (position == 0) {
            img_0.setImageResource(selected[0]);
            img_1.setImageResource(selected[1]);
            img_2.setImageResource(selected[1]);
//            lets_go.setVisibility(View.GONE);
        }
        if (position == 1) {
            img_0.setImageResource(selected[1]);
            img_1.setImageResource(selected[0]);
            img_2.setImageResource(selected[1]);
//          lets_go.setVisibility(View.GONE);
        }
        if (position == 2) {
            img_0.setImageResource(selected[1]);
            img_1.setImageResource(selected[1]);
            img_2.setImageResource(selected[0]);
//            lets_go.setVisibility(View.VISIBLE);
        }
        if (position == 3) {
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
