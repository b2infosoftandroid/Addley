package com.b2infosoft.addley.global;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by rajesh on 6/2/2016.
 */
public class Visit {
    private static final int PRIVATE_KEY = 0;
    private static final String USER_CREDENTIALS = "user_visit_rajesh";
    private static final String FRAGMENT_SUP = "fragment_sup";
    private static final String FRAGMENT_SUB = "fragment_sub";

    public static void setActiveSupFragment(Context context, int flag) {
        SharedPreferences setting = context.getSharedPreferences(USER_CREDENTIALS, PRIVATE_KEY);
        setting.edit()
                .putInt(FRAGMENT_SUP, flag)
                .commit();
    }
    public static void setActiveSubFragment(Context context, int flag) {
        SharedPreferences setting = context.getSharedPreferences(USER_CREDENTIALS, PRIVATE_KEY);
        setting.edit()
                .putInt(FRAGMENT_SUB, flag)
                .commit();
    }

    public static int getLastActiveSupFragment(Context context) {
        SharedPreferences setting = context.getSharedPreferences(USER_CREDENTIALS, PRIVATE_KEY);
        return setting.getInt(FRAGMENT_SUP, 0);
    }

    public static int getLastActiveSubFragment(Context context) {
        SharedPreferences setting = context.getSharedPreferences(USER_CREDENTIALS, PRIVATE_KEY);
        return setting.getInt(FRAGMENT_SUB, 0);
    }

}