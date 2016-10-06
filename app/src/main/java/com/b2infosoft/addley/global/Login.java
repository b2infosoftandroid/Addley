package com.b2infosoft.addley.global;

import android.content.Context;
import android.content.SharedPreferences;

import com.b2infosoft.addley.R;


/**
 * Created by rajesh on 4/16/2016.
 */

public class Login {
    private static final String USER_CREDENTIALS="dmr_rajesh";
    private static final int PRIVATE_KEY=0;
    private static final String DEVICE_LOGIN="device_login";
    private static final boolean STATUS[]={false,true};
    private static final String USER_PROFILE_PIC = "user_profile_picture";
    private static final String WALLET_AMOUNT="wallet_amount";
    public static void setKey(Context context,String key,String value){
        SharedPreferences setting= context.getSharedPreferences(USER_CREDENTIALS,PRIVATE_KEY);
        setting.edit()
                .putString(key,value)
                .commit();
    }
    public static String getValue(Context context,String key){
        SharedPreferences setting= context.getSharedPreferences(USER_CREDENTIALS,0);
        return setting.getString(key,"");
    }
    public static void setDemo(Context context,String key,int value){
        SharedPreferences setting= context.getSharedPreferences(USER_CREDENTIALS,PRIVATE_KEY);
        setting.edit()
                .putInt(key,value)
                .commit();
    }
    public static int getDemo(Context context,String key){
        SharedPreferences setting= context.getSharedPreferences(USER_CREDENTIALS,0);
        return setting.getInt(key,0);
    }
    public static boolean removeKey(Context context,String key){
        SharedPreferences setting= context.getSharedPreferences(USER_CREDENTIALS,PRIVATE_KEY);
        SharedPreferences.Editor editor = setting.edit();
        editor.remove(key);
        return editor.commit();
    }
    public static void setLogOutAll(Context context){
        SharedPreferences setting= context.getSharedPreferences(USER_CREDENTIALS, PRIVATE_KEY);
        setting.edit()
                .clear()
                .commit();
    }
    public static void setLogin(Context context){
        SharedPreferences setting= context.getSharedPreferences(USER_CREDENTIALS, PRIVATE_KEY);
        setting.edit()
                .putBoolean(DEVICE_LOGIN, STATUS[1])
                .commit();
    }
    public static void setLogOut(Context context){
        SharedPreferences setting= context.getSharedPreferences(USER_CREDENTIALS,PRIVATE_KEY);
        setting.edit()
                .putBoolean(DEVICE_LOGIN, STATUS[0])
                .commit();
    }
    public static boolean isLogin(Context context){
        SharedPreferences setting= context.getSharedPreferences(USER_CREDENTIALS,PRIVATE_KEY);
        return setting.getBoolean(DEVICE_LOGIN, false);
    }
    public static void setUserProfilePic(Context context,int id){
        SharedPreferences setting= context.getSharedPreferences(USER_CREDENTIALS,PRIVATE_KEY);
        setting.edit()
                .putInt(USER_PROFILE_PIC, id)
                .commit();
    }
    public static int getUserProfilePic(Context context){
        SharedPreferences setting= context.getSharedPreferences(USER_CREDENTIALS,PRIVATE_KEY);
        return  setting.getInt(USER_PROFILE_PIC, profile_pic[0]);
    }
    public static void setWalletAmount(Context context,float amount){
        SharedPreferences setting= context.getSharedPreferences(USER_CREDENTIALS,PRIVATE_KEY);
        setting.edit()
                .putFloat(WALLET_AMOUNT, amount)
                .commit();
    }
    public static float getWalletAmount(Context context){
        SharedPreferences setting= context.getSharedPreferences(USER_CREDENTIALS,PRIVATE_KEY);
        return  setting.getFloat(WALLET_AMOUNT,0.0f);
    }
    public static void setRatingStatus(Context context,int i){
        SharedPreferences setting= context.getSharedPreferences(USER_CREDENTIALS,PRIVATE_KEY);
        setting.edit()
                .putFloat(Tag.RATING, i)
                .commit();
    }
    public static int getRatingStatus(Context context){
        SharedPreferences setting= context.getSharedPreferences(USER_CREDENTIALS,PRIVATE_KEY);
        return  setting.getInt(Tag.RATING,Tag.RATING_DEFAULT);
    }
    public static void setGuest(Context context,int amount){
        SharedPreferences setting= context.getSharedPreferences(USER_CREDENTIALS,PRIVATE_KEY);
        setting.edit()
                .putInt(Tag.CONTINUE_AS_GUEST, amount)
                .commit();
    }
    public static int isGuest(Context context){
        SharedPreferences setting= context.getSharedPreferences(USER_CREDENTIALS,PRIVATE_KEY);
        return  setting.getInt(Tag.CONTINUE_AS_GUEST,Tag.CONTINUE_AS_GUEST_DEACTIVE);
    }
    public static int getGuestId(){
        return 1;
    }
    private static int [] profile_pic={
            R.mipmap.ic_account_circle_black,
            R.mipmap.ic_face_male_black_48dp,
            R.mipmap.ic_face_female_black_48dp
    };
    private static int []rating = {0,1,2,3};

}
