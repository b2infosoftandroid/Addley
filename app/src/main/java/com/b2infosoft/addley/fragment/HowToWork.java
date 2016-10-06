package com.b2infosoft.addley.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.b2infosoft.addley.R;


/**
 * Created by rajesh on 4/19/2016.
 */

public class HowToWork extends Fragment {
    public HowToWork() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_how_to_work_layout, container, false);

        return view;
    }
}
