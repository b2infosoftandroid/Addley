package com.b2infosoft.addley.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.b2infosoft.addley.R;
import com.b2infosoft.addley.adapter.TabPageAdapter;
import com.b2infosoft.addley.fragment.howtowork.How_One;
import com.b2infosoft.addley.fragment.howtowork.How_Three;
import com.b2infosoft.addley.fragment.howtowork.How_Two;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rajesh on 4/19/2016.
 */

public class HowToWork_1 extends Fragment implements ViewPager.OnPageChangeListener {
    private ViewPager viewPager;
    private List<Fragment> fragmentList;
    private TabPageAdapter tabPageAdapter;
    private ImageView img_0,img_1,img_2,lets_go;
    public HowToWork_1() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_how_to_work_layout_1, container, false);
        img_0 = (ImageView)view.findViewById(R.id.splash_one);
        img_1 = (ImageView)view.findViewById(R.id.splash_two);
        img_2 = (ImageView)view.findViewById(R.id.splash_three);
        fragmentList=new ArrayList<>();
        fragmentList.add(new How_One());
        fragmentList.add(new How_Two());
        fragmentList.add(new How_Three());
        tabPageAdapter =new TabPageAdapter(getChildFragmentManager(),fragmentList);
        viewPager = (ViewPager)view.findViewById(R.id.splash_demo_view_pager);
        viewPager.setAdapter(tabPageAdapter);
        viewPager.setOnPageChangeListener(this);
        return view;
    }
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }
    @Override
    public void onPageSelected(int position) {
        int selected[]={
                R.mipmap.ic_lens_black_18dp,
                R.mipmap.ic_panorama_fish_eye_black_18dp
        };
        if(position==0){
            img_0.setImageResource(selected[0]);
            img_1.setImageResource(selected[1]);
            img_2.setImageResource(selected[1]);
        }if(position==1){
            img_0.setImageResource(selected[1]);
            img_1.setImageResource(selected[0]);
            img_2.setImageResource(selected[1]);
        }if(position==2){
            img_0.setImageResource(selected[1]);
            img_1.setImageResource(selected[1]);
            img_2.setImageResource(selected[0]);
        }
    }
    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
