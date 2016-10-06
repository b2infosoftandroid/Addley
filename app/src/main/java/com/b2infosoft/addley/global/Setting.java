package com.b2infosoft.addley.global;

import com.b2infosoft.addley.R;

/**
 * Created by rajesh on 5/7/2016.
 */

public class Setting {
    private   int user_profile_pic;
    public int getUser_profile_pic() {
        return user_profile_pic;
    }
    public void setUser_profile_pic(int user_profile_pic) {
        this.user_profile_pic = user_profile_pic;
        
    }
    private static int [] profile_pic={
        R.mipmap.ic_account_circle_white,
        R.mipmap.ic_face_male_black_24dp,
        R.mipmap.ic_face_female_black_24dp
    };

}
