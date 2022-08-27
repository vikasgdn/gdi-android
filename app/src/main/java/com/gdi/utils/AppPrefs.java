package com.gdi.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AppPrefs {

    private static final String PREF_KEY_LOGIN = "logged_in";
    private static final String PREF_KEY_ACCESS_TOKEN = "access-token";
    private static final String PREF_KEY_FILTER_BRAND = "filter_brand";
    private static final String PREF_KEY_FILTER_CAMPAIGN = "filter_campaign";
    private static final String PREF_KEY_FILTER_CITY = "filter_city";
    private static final String PREF_KEY_FILTER_COUNTRY = "filter_country";
    private static final String PREF_KEY_FILTER_LOCATION = "filter_location";
    private static final String PREF_KEY_IA_FILTER_BRAND = "ia_filter_brand";
    private static final String PREF_KEY_IA_FILTER_AUDIT_TYPE = "ia_filter_audit_type";
    private static final String PREF_KEY_IA_FILTER_AUDIT = "ia_filter_audit";
    private static final String PREF_KEY_IA_FILTER_MONTH = "ia_filter_month";
    private static final String PREF_KEY_LOCALE = "locale";
    private static final String PREF_KEY_IA_FILTER_LOCATION = "ia_filter_location";
    private static final String PREF_KEY_INSTALLED = "installed";
    private static final String PREF_KEY_FAQ_TITLE = "faq_title";
    private static final String PREF_KEY_LOCAL_DB = "bs_local_db";
    private static final String PREF_KEY_DS_LOCAL_DB = "ds_local_db";
    private static final String PREF_KEY_ES_LOCAL_DB = "es_local_db";
    private static final String PREF_KEY_USER_ROLE = "user_role";
    private static final String PREF_KEY_CLIENT_ROLE_NAME = "client_role_name";
    private static final String PREF_KEY_CLIENT_ROLE_ID = "client_role_id";
    private static final String PREF_KEY_UPDATECHECK = "update_check";


    public static boolean isLoggedIn(Context ctx) {
        return getBoolean(ctx, PREF_KEY_LOGIN, false);
    }

    public static void setLoggedIn(Context ctx, boolean value) {
        putBoolean(ctx, PREF_KEY_LOGIN, value);
    }

    public static int getUserRole(Context ctx) {
        return getInt(ctx, PREF_KEY_USER_ROLE, 0);
    }
    public static void setUserRole(Context ctx, int val) {
        putInt(ctx, PREF_KEY_USER_ROLE, val);
    }

    public static int getClientRoleId(Context ctx) {
        return getInt(ctx, PREF_KEY_CLIENT_ROLE_ID, 0);
    }
    public static void setClientRoleId(Context ctx, int val) {
        putInt(ctx, PREF_KEY_CLIENT_ROLE_ID, val);
    }

    public static String getClientRoleName(Context ctx) {
        return getString(ctx, PREF_KEY_CLIENT_ROLE_NAME, "");
    }
    public static void setClientRoleName(Context ctx, String val) {
        putString(ctx, PREF_KEY_CLIENT_ROLE_NAME, val);
    }

    public static String getAccessToken(Context ctx) {
        return getString(ctx, PREF_KEY_ACCESS_TOKEN, "");
    }
    public static void setAccessToken(Context ctx, String val) {
        putString(ctx, PREF_KEY_ACCESS_TOKEN, val);
    }


    public static String getLocalDB(Context ctx) {
        return getString(ctx, PREF_KEY_LOCAL_DB, "");
    }
    public static void setLocalDB(Context ctx, String val) {
        putString(ctx, PREF_KEY_LOCAL_DB, val);
    }

    public static String getDSLocalDB(Context ctx) {
        return getString(ctx, PREF_KEY_DS_LOCAL_DB, "");
    }
    public static void setDSLocalDB(Context ctx, String val) {
        putString(ctx, PREF_KEY_DS_LOCAL_DB, val);
    }

    public static String getESLocalDB(Context ctx) {
        return getString(ctx, PREF_KEY_ES_LOCAL_DB, "");
    }
    public static void setESLocalDB(Context ctx, String val) {
        putString(ctx, PREF_KEY_ES_LOCAL_DB, val);
    }

    public static int getFilterBrand(Context ctx) {
        return getInt(ctx, PREF_KEY_FILTER_BRAND, 0);
    }
    public static void setFilterBrand(Context ctx, int val) {
        putInt(ctx, PREF_KEY_FILTER_BRAND, val);
    }

    public static int getFilterCampaign(Context ctx) {
        return getInt(ctx, PREF_KEY_FILTER_CAMPAIGN, 0);
    }
    public static void setFilterCampaign(Context ctx, int val) {
        putInt(ctx, PREF_KEY_FILTER_CAMPAIGN, val);
    }

    public static int getFilterCity(Context ctx) {
        return getInt(ctx, PREF_KEY_FILTER_CITY, 0);
    }
    public static void setFilterCity(Context ctx, int val) {
        putInt(ctx, PREF_KEY_FILTER_CITY, val);
    }

    public static int getFilterCountry(Context ctx) {
        return getInt(ctx, PREF_KEY_FILTER_COUNTRY, 0);
    }
    public static void setFilterCountry(Context ctx, int val) {
        putInt(ctx, PREF_KEY_FILTER_COUNTRY, val);
    }

    public static int getFilterLocation(Context ctx) {
        return getInt(ctx, PREF_KEY_FILTER_LOCATION, 0);
    }
    public static void setFilterLocation(Context ctx, int val) {
        putInt(ctx, PREF_KEY_FILTER_LOCATION, val);
    }

    public static int getIaFilterBrand(Context ctx) {
        return getInt(ctx, PREF_KEY_IA_FILTER_BRAND, 0);
    }
    public static void setIaFilterBrand(Context ctx, int val) {
        putInt(ctx, PREF_KEY_IA_FILTER_BRAND, val);
    }

    public static int getIaFilterAuditType(Context ctx) {
        return getInt(ctx, PREF_KEY_IA_FILTER_AUDIT_TYPE, 0);
    }
    public static void setIaFilterAuditType(Context ctx, int val) {
        putInt(ctx, PREF_KEY_IA_FILTER_AUDIT_TYPE, val);
    }

    public static String getIaFilterMonth(Context ctx) {
        return getString(ctx, PREF_KEY_IA_FILTER_MONTH, "");
    }
    public static void setIaFilterMonth(Context ctx, String val) {
        putString(ctx, PREF_KEY_IA_FILTER_MONTH, val);
    }


    public static String getLocaleSelected(Context ctx) {
        return getString(ctx, PREF_KEY_LOCALE, "en");
    }
    public static void setLocaleSelected(Context ctx, String val) {
        putString(ctx, PREF_KEY_LOCALE, val);
    }


    public static int getIaFilterLocation(Context ctx) {
        return getInt(ctx, PREF_KEY_IA_FILTER_LOCATION, 0);
    }
    public static void setIaFilterLocation(Context ctx, int val) {
        putInt(ctx, PREF_KEY_IA_FILTER_LOCATION, val);
    }

    public static int getIaFilterAuditName(Context ctx) {
        return getInt(ctx, PREF_KEY_IA_FILTER_AUDIT, 0);
    }
    public static void setIaFilterAuditName(Context ctx, int val) {
        putInt(ctx, PREF_KEY_IA_FILTER_AUDIT, val);
    }

    public static boolean isInstalled(Context ctx) {
        return getBoolean(ctx, PREF_KEY_INSTALLED, true);
    }

    public static void setInstalled(Context ctx, boolean value) {
        putBoolean(ctx, PREF_KEY_INSTALLED, value);
    }

    public static String getFaqTitle(Context ctx) {
        return getString(ctx, PREF_KEY_FAQ_TITLE, "");
    }
    public static void setFaqTitle(Context ctx, String val) {
        putString(ctx, PREF_KEY_FAQ_TITLE, val);
    }

    public static long getLastHitTime(Context ctx) {
        return getLong(ctx, PREF_KEY_UPDATECHECK);
    }
    public static void setLastHitTime(Context ctx, long val) {
        putLong(ctx, PREF_KEY_UPDATECHECK, val);
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
        SharedPreferences.Editor prefsEditor = PreferenceManager.getDefaultSharedPreferences(ctx).edit();
        prefsEditor.putInt(key, value).commit();

    }

    public static void putLong(Context ctx, String key, long value) {
        SharedPreferences.Editor prefsEditor = PreferenceManager.getDefaultSharedPreferences(ctx).edit();
        prefsEditor.putLong(key, value).commit();

    }

    public static long getLong(Context ctx, String key)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return prefs.getLong(key,0);

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
