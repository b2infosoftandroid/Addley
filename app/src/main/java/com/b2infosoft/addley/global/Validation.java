package com.b2infosoft.addley.global;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by rajesh on 4/20/2016.
 */
public class Validation {

    static final String PATTERN_MOBILE = "/^\\(?([0-9]{3})\\)?[-. ]?([0-9]{3})[-. ]?([0-9]{4})$/";
    static final String PATTERN_EMAIL = "/^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+$/";

    public static boolean isEmailValid(String email) {
            if (email == null) {
                return false;
            } else {
                return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
            }
        /*
        if(email == null){
            return false;
        }else {
            Pattern pattern   =  Pattern.compile(PATTERN_EMAIL);
            Matcher matcher = pattern.matcher(email);
            return matcher.matches();
        }
        */
    }
    public static boolean isMobileValid(String mobile){
      if(mobile == null){
          return false;
      }else {
          Pattern pattern   =  Pattern.compile(PATTERN_MOBILE);
          Matcher matcher = pattern.matcher(mobile);
          return matcher.matches();
      }
    }
    public static boolean isPasswordValid(String password) {

       return password.length() > 0;
    }
}
