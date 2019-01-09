package com.gdi.utils;

import android.text.TextUtils;
import android.util.Patterns;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 3/5/2018.
 */

public class Validation {

    public static boolean isValidEmail(CharSequence email) {
        if (TextUtils.isEmpty(email)) {
            return false;
        } else {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }

    }

    public static boolean isValidPhoneNumber(CharSequence phoneNumber) {
        if (!TextUtils.isEmpty(phoneNumber)) {
            if (phoneNumber.toString().contains(" ")
                    || (phoneNumber.charAt(0) == '0')
                    || (!phoneNumber.toString().matches("[0-9]+"))
                    || (phoneNumber.length() != 10)) {
                return false;
            } else {
                return Patterns.PHONE.matcher(phoneNumber).matches();
            }

        }
        return false;
    }

    public static boolean isValidPassword(CharSequence password) {
        return !TextUtils.isEmpty(password) && password.length() >= 8;

    }

    public static boolean isValidPassword(CharSequence password, final String PASSWORD_PATTERN){
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
}
