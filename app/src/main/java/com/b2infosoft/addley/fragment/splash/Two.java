package com.b2infosoft.addley.fragment.splash;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.b2infosoft.addley.R;


/**
 * Created by rajesh on 5/10/2016.
 */
public class Two extends Fragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_splash_two,container,false);

        return view;
    }
}
