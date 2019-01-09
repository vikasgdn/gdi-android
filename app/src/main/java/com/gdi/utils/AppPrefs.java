package com.gdi.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AppPrefs {

    private static final String PREF_KEY_LOGIN = "logged_in";
    private static final String PREF_KEY_ACCESS_TOKEN = "access-token";

    public static boolean isLoggedIn(Context ctx) {
        return getBoolean(ctx, PREF_KEY_LOGIN, false);
    }

    public static void setLoggedIn(Context ctx, boolean value) {
        putBoolean(ctx, PREF_KEY_LOGIN, value);
    }

    public static String getAccessToken(Context ctx) {
        return getString(ctx, PREF_KEY_ACCESS_TOKEN, "");
    }
    public static void setAccessToken(Context ctx, String val) {
        putString(ctx, PREF_KEY_ACCESS_TOKEN, val);
    }





    public static void putBoolean(Context ctx, String key, boolean value) {
        SharedPreferences.Editor prefsEditor = PreferenceManager.getDefaultSharedPreferences(ctx)
                .edit();
        prefsEditor.putBoolean(key, value).commit();

    }

    public static boolean getBoolean(Context ctx, String key) {
        return getBoolean(ctx, key, false);

    }

    public static boolean getBoolean(Context ctx, String key, boolean defaulValue) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return prefs.getBoolean(key, defaulValue);

    }

    public static void putString(Context ctx, String key, String value) {
        SharedPreferences.Editor prefsEditor = PreferenceManager.getDefaultSharedPreferences(ctx)
                .edit();
        prefsEditor.putString(key, value).commit();

    }

    public static String getString(Context ctx, String key) {
        return getString(ctx, key, null);

    }

    public static String getString(Context ctx, String key, String defaultValue) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return prefs.getString(key, defaultValue);

    }

    public static void putInt(Context ctx, String key, int value) {
        SharedPreferences.Editor prefsEditor = PreferenceManager.getDefaultSharedPreferences(ctx)
                .edit();
        prefsEditor.putInt(key, value).commit();

    }

    public static void clear(Context ctx) {
        SharedPreferences.Editor prefsEditor = PreferenceManager.getDefaultSharedPreferences(ctx)
                .edit();
        prefsEditor.clear();
        prefsEditor.commit();

    }

    public static int getInt(Context ctx, String key) {
        return getInt(ctx, key, 0);

    }
    public static int getInt(Context ctx, String key, int defaultValue) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return prefs.getInt(key, defaultValue);

    }
}
