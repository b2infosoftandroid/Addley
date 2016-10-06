package com.b2infosoft.addley.global;

/**
 * Created by rajesh on 4/15/2016.
 **/

public class ServerPath {

/*----------- START LOCAL LINKS------------- */
/*
    private static final String SERVER_IP = "192.168.0.51";
    private static final String SERVER_ADDRESS = "http://" + SERVER_IP + "/rajesh/addley_services/";
    private static final String LOGO_OFFER_ADDRESS = "http://" + SERVER_IP + "/addley1.1/images/brands/";
    private static final String PAGES_ADDRESS = "http://" + SERVER_IP + "/addley1.1/";
    private static final String COUPON_SERVER_ADDRESS = "http://" + SERVER_IP + "/rajesh/addley_services/coupons/";
    private static final String COUPON_LOGO_SERVER_ADDRESS = "http://" + SERVER_IP + "/addley1.1/coupons/admin/uploa/category/";
    //  private static final String CATEGORY_ICON_ADDRESS="http://"+SERVER_IP+"/addley1.1/images/category/appicon/";
    private static final String CATEGORY_ICON_ADDRESS = "http://addley.in/images/category/appicon/";
*/
/*----------- END LOCAL LINKS------------- */

/*----------- START LIVE LINKS------------- */

    private static final String SERVER_ADDRESS="http://b2infosoft.in/addley_services/";
    private static final String LOGO_OFFER_ADDRESS="http://addley.in/images/brands/";
    private static final String PAGES_ADDRESS="http://addley.in/";
    private static final String COUPON_SERVER_ADDRESS="http://b2infosoft.in/addley_services/coupons/";
    private static final String COUPON_LOGO_SERVER_ADDRESS="http://addley.in/coupons/admin/uploa/category/";
    private static final String CATEGORY_ICON_ADDRESS="http://addley.in/images/category/appicon/";

/*----------- END LIVE LINKS------------- */

    public static String getVisitPath() {
        return "http://addley.in/coupons/";
    }

    public static String getPath() {
        return SERVER_ADDRESS;
    }

    public static String getCouponPath() {
        return COUPON_SERVER_ADDRESS;
    }

    public static String getCategoryIconPath(String icon) {
        return CATEGORY_ICON_ADDRESS.concat(icon);
    }

    public static String getLogoPath(String logo) {
        return LOGO_OFFER_ADDRESS.concat(logo);
    }

    public static String getCouponLogoPath(String logo) {
        return COUPON_LOGO_SERVER_ADDRESS.concat(logo);
    }

    public static String getPagesPath() {
        return PAGES_ADDRESS;
    }
}
