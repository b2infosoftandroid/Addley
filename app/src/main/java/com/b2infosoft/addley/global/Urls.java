package com.b2infosoft.addley.global;

/**
 * Created by rajesh on 4/15/2016.
 */

public class Urls {
    public static String getDoLogin(){return ServerPath.getPath().concat("do_login.php");}
    public static String getUserAction(){
        return ServerPath.getPath().concat("user_action.php");
    }
    public static String getBestOffers(){
        return ServerPath.getPath().concat("best_offers.php");
    }
    public static String getTermsCondition(){return ServerPath.getPath().concat("terms_android.php");}
    public static String getCouponUserAction(){
        return ServerPath.getCouponPath().concat("user_action.php");
    }
}
