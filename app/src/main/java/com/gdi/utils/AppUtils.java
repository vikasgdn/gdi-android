package com.gdi.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.gdi.R;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppUtils {

    public static void toast(Activity activity, String message) {
        if (message != null && !message.equals("") && activity != null) {
            Snackbar snack = Snackbar.make(activity.findViewById(android.R.id.content),
                    message, Snackbar.LENGTH_LONG);
            ViewGroup group = (ViewGroup) snack.getView();
            group.setBackgroundColor(ContextCompat.getColor(activity, R.color.appThemeColour));
            snack.show();
        }
    }

    public static String getString(JSONObject data, String key) {
        try {
            if (data.has(key) && !data.isNull(key)) {
                return data.getString(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return "";
    }

    public static int getInt(JSONObject data, String key) {
        try {
            if (data.has(key) && !data.isNull(key)) {
                return data.getInt(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        return 0;
    }

    public static boolean isStringEmpty(String string) {
        if (string == null)
            return true;
        return string.equals("") || string.equals("NULL") || string.equals("null");
    }

    public static boolean isValidPhoneNumber(CharSequence phoneNumber) {
        if (phoneNumber.toString().contains(" ")
                || (phoneNumber.charAt(0) == '0')
                || (!phoneNumber.toString().matches("[0-9]+"))
                || ((phoneNumber.length() < 6) || phoneNumber.length() > 16)) {
            return false;
        } else {
            return Patterns.PHONE.matcher(phoneNumber).matches();
        }
    }

    public static boolean isValidMobile(String phone) {
        boolean check;
        if(!Pattern.matches("[a-zA-Z]+", phone)) {
            if(phone.length() < 6 || phone.length() > 16) {
                check = false;
            }else {
                check = true;
            }
        } else {
            check=false;
        }
        return check;
    }

    public static boolean isValidEmail(CharSequence email) {
        if (TextUtils.isEmpty(email)) {
            return false;
        } else {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
    }

    public static boolean hasDigitsOnly(String str){
        if (str == null){
            return false;
        }
        String scoreReplace = str.replace("%", "");
        Pattern pattern = Pattern.compile("[0-9]+.+");
        Matcher matcher = pattern.matcher(scoreReplace);
        return matcher.matches();

    }

    public static void hideKeyboard(Context context, View view) {
        if (context != null && view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static String getAuditMonth(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        return dateFormat.format(date);
    }

    public static String getAuditDate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    public static String getDate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return dateFormat.format(date);
    }

    public static String setAuditMonth(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("MM-yyyy");
        return dateFormat.format(date);
    }

    public static void setScoreColor(String score, TextView tv_score, Context context) {
        String scoreReplace = score.replace("%", "");
        try {
            float rep_score = Float.valueOf(scoreReplace);
            if (rep_score >= 80.0) {
                tv_score.setTextColor(context.getResources().getColor(R.color.scoreGreen));
            } else if (rep_score < 80.0 && rep_score >= 65.0) {
                tv_score.setTextColor(context.getResources().getColor(R.color.scoreGold));
            } else {
                tv_score.setTextColor(context.getResources().getColor(R.color.scoreRed));
            }
        }catch (NumberFormatException e){
            e.printStackTrace();
        }

    }

    public static void setStatusColor(int status, TextView tv_score, Context context) {
        switch (status){
            case 0:
                tv_score.setBackground(context.getResources().getDrawable(R.drawable.audit_na_status_border));
                tv_score.setTextColor(context.getResources().getColor(R.color.colorWhite));
                break;
            case 1:
                tv_score.setBackground(context.getResources().getDrawable(R.drawable.audit_created_status_border));
                tv_score.setTextColor(context.getResources().getColor(R.color.colorBlack));
                break;
            case 2:
                tv_score.setBackground(context.getResources().getDrawable(R.drawable.audit_created_status_border));
                tv_score.setTextColor(context.getResources().getColor(R.color.colorBlack));
                break;
            case 3:
                tv_score.setBackground(context.getResources().getDrawable(R.drawable.audit_rejected_status_border));
                tv_score.setTextColor(context.getResources().getColor(R.color.colorWhite));
                break;
            case 4:
                tv_score.setBackground(context.getResources().getDrawable(R.drawable.audit_submitted_status_border));
                tv_score.setTextColor(context.getResources().getColor(R.color.colorWhite));
                break;
            case 5:
                tv_score.setBackground(context.getResources().getDrawable(R.drawable.audit_reviewed_status_border));
                tv_score.setTextColor(context.getResources().getColor(R.color.colorWhite));
                break;
        }
    }

    public static String getShowDate(String date){

        SimpleDateFormat  dateFormat = new SimpleDateFormat("yyyy-MM-DD");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            Date date1 = dateFormat.parse(date);
            DateFormat dateFormat1 = new SimpleDateFormat("MMM dd,\nyyyy");
            return dateFormat1.format(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(""+date);
        return "";

    }

    public static String getDSAuditDate(String date){

        SimpleDateFormat  dateFormat = new SimpleDateFormat("yyyy-MM-DD hh:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            Date date1 = dateFormat.parse(date);
            DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-DD");
            return dateFormat1.format(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(""+date);
        return "";

    }

    public static String getDSAuditTime(String date){

        SimpleDateFormat  dateFormat = new SimpleDateFormat("yyyy-MM-DD hh:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            Date date1 = dateFormat.parse(date);
            DateFormat dateFormat1 = new SimpleDateFormat("hh:mm");
            return dateFormat1.format(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(""+date);
        return "";

    }

    public static String getAuditDate(String date){

        SimpleDateFormat  dateFormat = new SimpleDateFormat("yyyy-MM-DD");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            Date date1 = dateFormat.parse(date);

            return getFormattedDate(date1);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(""+date);
        return "";

    }

    private static String getFormattedDate(Date date) {
        DateFormat dateFormat1 = new SimpleDateFormat("d");
        AppLogger.e("dateFormate", "" + dateFormat1);
        String stringDate = dateFormat1.format(date);
        AppLogger.e("stringDate", "" + stringDate);
        int day = Integer.valueOf(stringDate);
        AppLogger.e("intDate", "" + stringDate);

        switch (day % 10) {
            case 1:
                AppLogger.e("return", "1");
                return new SimpleDateFormat("d'st' MMM yyyy").format(date);
            case 2:
                AppLogger.e("return", "2");
                return new SimpleDateFormat("d'nd' MMM yyyy").format(date);
            case 3:
                AppLogger.e("return", "3");
                return new SimpleDateFormat("d'rd' MMM yyyy").format(date);
            default:
                AppLogger.e("return", "4");
                return new SimpleDateFormat("d'th' MMM yyyy").format(date);
        }
    }

    public static String getDate(String dateTime) {

        SimpleDateFormat formatter = new SimpleDateFormat("MMM, dd yyyy");
        Date date = new Date(dateTime);
        try {
            return formatter.format(date);
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    public static String getDSAuditDate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    public static String getDSAuditTime(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("hh:mm");
        return dateFormat.format(date);
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    public static void showDoNothingDialog(final Context ctx, String btnText,
                                           String message) {
        try {
            AlertDialog.Builder dialog = new AlertDialog.Builder(ctx);
            dialog.setMessage(message);
            dialog.setCancelable(false);
            dialog.setNegativeButton(btnText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    ((Activity)ctx).finish();
                }
            });
            dialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showHeaderDescription(final Context ctx, String message) {
        try {
            AlertDialog.Builder dialog = new AlertDialog.Builder(ctx);
            //dialog.setTitle("GDI");
            dialog.setMessage(message);
            //dialog.setCancelable(false);
            /*dialog.setNegativeButton(btnText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    //((Activity)ctx).finish();
                }
            });*/
            dialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

