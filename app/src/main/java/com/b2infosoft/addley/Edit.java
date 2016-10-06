package com.b2infosoft.addley;

import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.b2infosoft.addley.adapter.TabPageAdapter;
import com.b2infosoft.addley.fragment.user.BankAccountDetail;
import com.b2infosoft.addley.fragment.user.PersonalDetail;
import com.b2infosoft.addley.global.Global;
import com.b2infosoft.addley.global.Login;

import java.util.ArrayList;
import java.util.List;

public class Edit extends AppCompatActivity implements ViewPager.OnPageChangeListener,TabHost.OnTabChangeListener {
    private ViewPager viewPager;
    private TabHost tabHost;
    private TabPageAdapter tabPageAdapter;
    private String []tabs={"Personal","Bank A/c"};
    private List<Fragment> fragmentList;
    private ImageView profile_pic,profile_pic_edit,profile_pic_male,profile_pic_female;
    private FrameLayout profile_pic_layout;
    private LinearLayout profile_pic_layout_edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fragmentList=new ArrayList<>();
        fragmentList.add(new PersonalDetail());
        fragmentList.add(new BankAccountDetail());
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
        profile_pic_layout = (FrameLayout)findViewById(R.id.profile_pic_layout);
        profile_pic_layout.setVisibility(View.VISIBLE);
        profile_pic_layout_edit = (LinearLayout)findViewById(R.id.profile_pic_layout_edit);
        profile_pic_layout_edit.setVisibility(View.GONE);

        profile_pic = (ImageView)findViewById(R.id.edit_profile_pic);
        profile_pic.setImageResource(Login.getUserProfilePic(getApplicationContext()));
        profile_pic_edit = (ImageView)findViewById(R.id.edit_profile_pic_edit);
        profile_pic_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profile_pic_layout.setVisibility(View.GONE);
                profile_pic_layout_edit.setVisibility(View.VISIBLE);
            }
        });
        profile_pic_male = (ImageView)findViewById(R.id.edit_profile_pic_choose_male);
        profile_pic_male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login.setUserProfilePic(getBaseContext(), R.mipmap.ic_face_male_black_48dp);
                profile_pic_layout.setVisibility(View.VISIBLE);
                profile_pic_layout_edit.setVisibility(View.GONE);
                updateProfilePic();
            }
        });
        profile_pic_female = (ImageView)findViewById(R.id.edit_profile_pic_choose_female);
        profile_pic_female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login.setUserProfilePic(getBaseContext(), R.mipmap.ic_face_female_black_48dp);
                profile_pic_layout.setVisibility(View.VISIBLE);
                profile_pic_layout_edit.setVisibility(View.GONE);
                updateProfilePic();
            }
        });
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
    private void updateProfilePic(){
        profile_pic.setImageResource(Login.getUserProfilePic(getApplicationContext()));
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
