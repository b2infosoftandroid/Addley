package com.b2infosoft.addley;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.b2infosoft.addley.adapter.TabPageAdapter;
import com.b2infosoft.addley.global.Global;
import com.b2infosoft.addley.global.Tag;
import com.b2infosoft.addley.fragment.profile.Overview;
import com.b2infosoft.addley.fragment.profile.Summary;
import com.b2infosoft.addley.global.Login;

import java.util.ArrayList;
import java.util.List;

public class Profile extends AppCompatActivity implements ViewPager.OnPageChangeListener,TabHost.OnTabChangeListener{
    private ViewPager viewPager;
    private TabHost tabHost;
    private TabPageAdapter tabPageAdapter;
    private String []tabs={"Overview","Summary"};
    private List<Fragment> fragmentList;
    private ImageView profile_pic,profile_edit;
    private TextView user_name,user_email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fragmentList=new ArrayList<>();
        fragmentList.add(new Overview());
        fragmentList.add(new Summary());
        tabHost=(TabHost)findViewById(android.R.id.tabhost);
        tabHost.setup();
        for(String tab:tabs){
            TabHost.TabSpec tabSpec=tabHost.newTabSpec(tab.toUpperCase());
            tabSpec.setIndicator(tab.toLowerCase());
            tabSpec.setContent(new TabHost.TabContentFactory() {
                @Override
                public View createTabContent(String tag) {
                    View view1 = new View(getApplicationContext());
                    view1.setMinimumHeight(0);
                    view1.setMinimumWidth(0);
                    return view1;
                }
            });
            tabHost.addTab(tabSpec);
        }
        tabHost.setOnTabChangedListener(this);
        tabPageAdapter =new TabPageAdapter(getSupportFragmentManager(),fragmentList);
        viewPager=(ViewPager)findViewById(R.id.view_pager);
        viewPager.setAdapter(tabPageAdapter);
        viewPager.setOnPageChangeListener(this);
        setSelectedTabColor();
        profile_pic =(ImageView)findViewById(R.id.profile_user_profile);
        profile_pic.setImageResource(Login.getUserProfilePic(getBaseContext()));
        profile_edit = (ImageView)findViewById(R.id.profile_edit);
        profile_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Profile.this, Edit.class));
            }
        });
        user_name = (TextView)findViewById(R.id.profile_user_name);
        user_name.setText(Login.getValue(getBaseContext(), Tag.USER_NAME));
        user_email = (TextView)findViewById(R.id.profile_user_email);
        user_email.setText(Login.getValue(getBaseContext(), Tag.USER_EMAIL));
    }
    private void setSelectedTabColor() {
        for(int i=0;i<tabHost.getTabWidget().getChildCount();i++)
        {
            TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title); //Unselected Tabs
            tv.setTextColor(getResources().getColor(R.color.colorAccent));
            tv.setTypeface(Global.getRobotoFont(this));
        }
        TextView tv = (TextView) tabHost.getCurrentTabView().findViewById(android.R.id.title);
        tv.setTypeface(Global.getRobotoFont(this));
        tv.setTextColor(getResources().getColor(R.color.ThemeColor));
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setSelectedTabColor();
        tabHost.setCurrentTab(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onTabChanged(String tabId) {
        int selected_item=tabHost.getCurrentTab();
        viewPager.setCurrentItem(selected_item);
    }

}
