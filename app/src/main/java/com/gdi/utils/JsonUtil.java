package com.gdi.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 3/5/2018.
 */

public class JsonUtil {

    public static String getString(JSONObject jsonObject, String key)  {

        if (!jsonObject.has(key) || jsonObject.isNull(key)){
            return null;
        }else{
            try {
                return jsonObject.getString(key);
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public static String getString(JSONObject jsonObject, String key, String defaultValue) {
        if (!jsonObject.has(key) || jsonObject.isNull(key)){
            return defaultValue;
        }

        try {
            String val = jsonObject.getString(key);
            return val;
        } catch (JSONException e) {
            e.printStackTrace();
            return defaultValue;
        }

    }

    public static int getInt(JSONObject jsonObject, String key){
        int val = 0;
        if (!jsonObject.has(key) || jsonObject.isNull(key)){
            return val;
        }

        try {
            val = jsonObject.getInt(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return val;

    }
    public static int getInt(JSONObject jsonObject, String key, int defaultValue){
        int val = defaultValue;
        if (!jsonObject.has(key) || jsonObject.isNull(key)){
            return val;
        }

        try {
            val = jsonObject.getInt(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return val;

    }

    public static long getLong(JSONObject jsonObject, String key){
        long val = 0;
        if (!jsonObject.has(key) || jsonObject.isNull(key)){
            return val;
        }

        try {
            val = jsonObject.getLong(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return val;

    }

    public static double getDouble(JSONObject jsonObject, String key){
        double val = 0.0;
        if (!jsonObject.has(key) || jsonObject.isNull(key)){
            return val;
        }

        try {
            val = jsonObject.getDouble(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return val;

    }

    public static JSONArray getJSONArray(JSONObject jsonObject, String key){
        JSONArray val = null;
        if (!jsonObject.has(key) || jsonObject.isNull(key)){
            return val;
        }

        try {
            val = jsonObject.getJSONArray(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return val;

    }
    public static JSONObject getJSONObject(JSONObject jsonObject, String key){
        JSONObject val = null;
        if (!jsonObject.has(key) || jsonObject.isNull(key)){
            return val;
        }

        try {
            val = jsonObject.getJSONObject(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return val;

    }
}
