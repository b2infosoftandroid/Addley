package com.b2infosoft.addley.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;

import com.b2infosoft.addley.R;
import com.b2infosoft.addley.adapter.TabPageAdapter;
import com.b2infosoft.addley.fragment.user.*;
import com.b2infosoft.addley.global.Global;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rajesh on 4/19/2016.
 */

public class User extends Fragment implements ViewPager.OnPageChangeListener,TabHost.OnTabChangeListener {
    private ViewPager viewPager;
    private TabHost tabHost;
    private TabPageAdapter tabPageAdapter;
    private String []tabs={"DASHBOARD","PERSONAL DETAILS","BANK A/C DETAILS"};
    private List<Fragment> fragmentList;
    public User() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_layout,container,false);
        fragmentList=new ArrayList<>();
        fragmentList.add(new com.b2infosoft.addley.fragment.user.Dashboard());
        fragmentList.add(new PersonalDetail());
        fragmentList.add(new BankAccountDetail());

        tabHost=(TabHost)view.findViewById(android.R.id.tabhost);
        tabHost.setup();
        for(String tab:tabs){
            TabHost.TabSpec tabSpec=tabHost.newTabSpec(tab);
            tabSpec.setIndicator(tab);
            tabSpec.setContent(new TabHost.TabContentFactory() {
                @Override
                public View createTabContent(String tag) {
                    View view1 = new View(getActivity().getApplicationContext());
                    view1.setMinimumHeight(0);
                    view1.setMinimumWidth(0);
                    return view1;
                }
            });
            tabHost.addTab(tabSpec);
        }
        tabHost.setOnTabChangedListener(this);
        tabPageAdapter =new TabPageAdapter(getChildFragmentManager(),fragmentList);
        viewPager=(ViewPager)view.findViewById(R.id.view_pager);
        viewPager.setAdapter(tabPageAdapter);
        viewPager.setOnPageChangeListener(this);
        setSelectedTabColor();
        return view;
    }
    private void setSelectedTabColor() {
        for(int i=0;i<tabHost.getTabWidget().getChildCount();i++)
        {
            TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title); //Unselected Tabs
            tv.setTextColor(Color.parseColor("#ffffff"));
            tv.setTypeface(Global.getRobotoFont(getActivity()));
        }
    }
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        tabHost.setCurrentTab(position);
        setSelectedTabColor();
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
