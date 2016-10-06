package com.b2infosoft.addley.global;

import android.content.Context;
import android.graphics.Typeface;

import com.b2infosoft.addley.R;

/**
 * Created by rajesh on 3/8/2016.
 */

public class Global {
    public static final int SPLASH_TIME_OUT=4000;
    public static final String FONT_ROBOTO="fonts/Roboto-Regular.ttf";
    public static final String DUMMY_CREDIDENTIALS[]={"admin@b2infosoft.com:admin","rajesh@b2infosoft.com:rajesh","dhara@b2infosoft.com:dhara"};
    public static final Typeface getRobotoFont(Context context){
        return Typeface.createFromAsset(context.getAssets(),FONT_ROBOTO);
    }
    public static final String getRupee(Context context){
        return context.getResources().getString(R.string.Rs);
    }
}
