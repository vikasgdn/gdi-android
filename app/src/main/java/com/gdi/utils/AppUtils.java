package com.gdi.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.ConnectivityManager;

import com.gdi.activity.internalaudit.model.audit.BrandStandard.BrandStandardQuestion;
import com.gdi.activity.internalaudit.model.audit.BrandStandard.BrandStandardQuestionsOption;
import com.gdi.activity.internalaudit.model.audit.BrandStandard.BrandStandardSection;
import com.gdi.activity.internalaudit.model.audit.BrandStandard.BrandStandardSubSection;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.gdi.hotel.mystery.audits.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppUtils {
    public static String capitalizeHeading(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }



    public static void toast(Activity activity, String message) {
        if (message != null && !message.equals("") && activity != null) {
            Snackbar snack = Snackbar.make(activity.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
            ViewGroup group = (ViewGroup) snack.getView();
            group.setBackgroundColor(ContextCompat.getColor(activity, R.color.appThemeColour));
            snack.setDuration(1800);
            snack.show();
        }
    }


    public static String getFormatedDate(String dateS) {
        String resultDate = "N/A";
        try {
            if (TextUtils.isEmpty(dateS))
                return resultDate;
            SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
            Date date = dt.parse(dateS);
            SimpleDateFormat dt1 = new SimpleDateFormat("EEE, d MMM yyyy");
            resultDate = dt1.format(date);
            return resultDate;
        } catch (Exception e) {
            e.printStackTrace();
            return resultDate;
        }
    }
    public static void parseRefreshTokenRespone(JSONObject jsonObject, Context context) {
        }

    public Bitmap drawTextToBitmap(Context gContext, Bitmap bitmap, String gText) {
        Log.e("BITMAP TEXT", "" + gText);
        Bitmap drawBitmap = null;
        try {
            Resources resources = gContext.getResources();
            float scale = resources.getDisplayMetrics().density;

            Bitmap.Config bitmapConfig = bitmap.getConfig();
            // set default bitmap config if none
            if (bitmapConfig == null) {
                bitmapConfig = Bitmap.Config.ARGB_8888;
            }
            drawBitmap = bitmap.copy(bitmapConfig, true);

            Canvas canvas = new Canvas(drawBitmap);
            // new antialised Paint
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            // text color - #3D3D3D
            paint.setColor(Color.WHITE);
            // text size in pixels
            paint.setTextSize((int) (25 * scale));
            // text shadow
            paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);
            // draw text to the Canvas center
            Rect bounds = new Rect();

            paint.getTextBounds(gText, 0, gText.length(), bounds);
            int x = 20;
            int y = (drawBitmap.getHeight() - bounds.height() * 1);

            Paint mPaint = new Paint();
            mPaint.setColor(gContext.getResources().getColor(R.color.c_transparent_black));
            int left = 0;
            int top = (drawBitmap.getHeight() - bounds.height() * (1 + 1));
            int right = drawBitmap.getWidth();
            int bottom = drawBitmap.getHeight();
            canvas.drawRect(left, top, right, bottom, mPaint);
            canvas.drawText(gText, x, y, paint);
        } catch (Exception e) {
            drawBitmap = bitmap;
            e.printStackTrace();
        }

        return drawBitmap;
    }


    public static String getCurrentDateImage() {

        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat1 = new SimpleDateFormat("dd-MMM-yy hh:mm");
        System.out.println("" + date);

        return dateFormat1.format(date);

    }


    public static byte[] readBytes(Uri uri, Context context) throws IOException {
        InputStream inputStream = context.getContentResolver().openInputStream(uri);
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public static void toastDisplayForLong(Activity activity, String message) {
        if (message != null && !message.equals("") && activity != null) {
            Snackbar snack = Snackbar.make(activity.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
            ViewGroup group = (ViewGroup) snack.getView();
            group.setMinimumHeight(160);
            group.setBackgroundColor(ContextCompat.getColor(activity, R.color.appThemeColour));
            snack.setDuration(8000);
            snack.show();
        }
    }


    public static String getAuditDateCurrent()
    {
        String pattern = "yyyy-MM-dd";
        String dateInString =new SimpleDateFormat(pattern).format(new Date());
        return dateInString;
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
            check = phone.length() >= 6 && phone.length() <= 16;
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


    public static String getCurrentDate(){

        Date date = Calendar.getInstance().getTime();
            DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MMM-dd hh:mm:ss");
        System.out.println(""+date);

        return dateFormat1.format(date);

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


    public static Bitmap resizeImage(Bitmap image, int maxWidth, int maxHeight) {
        if (maxHeight > 0 && maxWidth > 0) {
            int width = image.getWidth();
            int height = image.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (ratioMax > ratioBitmap) {
                finalWidth = (int) ((float)maxHeight * ratioBitmap);
            } else {
                finalHeight = (int) ((float)maxWidth / ratioBitmap);
            }
            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
            return image;
        } else {
            return image;
        }
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


    public static void datePicker(final Context context, final TextView editText,  final boolean needTimePicker) {
        Calendar c = Calendar.getInstance();
        // Process to get Current Date
        final int currentYear = c.get(Calendar.YEAR);
        final int currentMonth = c.get(Calendar.MONTH);
        final int currentDay = c.get(Calendar.DAY_OF_MONTH);
        final int currentHour = c.get(Calendar.HOUR_OF_DAY);
        final int currentMinute = c.get(Calendar.MINUTE);
        int setYear = currentYear;
        int setMonth = currentMonth;
        int setDay = currentDay;
        int setHour = currentHour;
        int setMinute = currentMinute;


        int finalSetHour = setHour;
        int finalSetMinute = setMinute;
        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        if (year <= (currentYear) /*&& monthOfYear<mMonth && dayOfMonth<mDay*/) {
                            String strYEAR = String.valueOf(year);

                            //							Adding 0, If Date in Single Digit
                            String strDATE = "";
                            if (dayOfMonth < 10)
                                strDATE = "0" + dayOfMonth;
                            else
                                strDATE = String.valueOf(dayOfMonth);

                            //Adding 0, If Month in Single Digit
                            String strMONTH = "";
                            int month = monthOfYear + 1;
                            if (month < 10)
                                strMONTH = "0" + month;
                            else
                                strMONTH = String.valueOf(month);

                            // Display Selected date in TextView
                            /*DD/MM/YYYY*/
                            String date = strYEAR + "-" + strMONTH + "-" + strDATE;

                            if (needTimePicker)
                                timePicker(context,editText, date);
                            else
                                editText.setText(date);
                        } else {
                            Log.e("","Invalid Date!");
                        }
                    }
                }, setYear, setMonth, setDay);
        datePickerDialog.show();
        //        datePickerDialog.getDatePicker().setMaxDate(MDateUtils.getMSfromDate(
        //                (currentDay+1)+"/"+(currentMonth+1)+"/"+(currentYear), "dd/MM/yyyy"));
    }

    public static void timePicker(Context context,final TextView editText, final String date) {
        String time="";
        Calendar c = Calendar.getInstance();
        // Process to get Current Date
        final int currentHour = c.get(Calendar.HOUR_OF_DAY);
        final int currentMinute = c.get(Calendar.MINUTE);
        int setHour = currentHour;
        int setMinute = currentMinute;

        TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        String strMinute = String.valueOf(minute);
                        if ((strMinute).length() == 1) {
                            strMinute = "0" + strMinute;
                        }
                        String time = (date.isEmpty() ? date : (date + " ")) + hourOfDay + ":" + strMinute;
                        editText.setText(time);
                    }
                }, setHour, setMinute, true);
        timePickerDialog.show();
    }

    public static String getFormatedDateWithTime(String dateS) {
        //2020-09-16 15:48:25

        String resultDate = "N/A";
        try {
            if (TextUtils.isEmpty(dateS))
                return resultDate;
            SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            Date date = dt.parse(dateS);
            SimpleDateFormat dt1 = new SimpleDateFormat("EEE, d MMM yyyy, hh:mm aa");
            resultDate = dt1.format(date);
            return resultDate;
        } catch (Exception e) {
            e.printStackTrace();
            return resultDate;
        }
    }


    public static String getFormatedDateDayMonth(String dateS) {
        String resultDate = "N/A";
        try {
            if (TextUtils.isEmpty(dateS))
                return resultDate;
            SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
            Date date = dt.parse(dateS);
            SimpleDateFormat dt1 = new SimpleDateFormat("EEE, d MMM");
            resultDate = dt1.format(date);
            return resultDate;
        } catch (Exception e) {
            e.printStackTrace();
            return resultDate;
        }
    }

    public static JSONArray validateSubmitQuestionFinalSubmission(Activity activity, ArrayList<BrandStandardSection> brandStandardSection) {
        // boolean validate = true;
        int count = 0;
        int mMediaCount = 0, mCommentCount = 0, mActionPlanRequred = 0;
        ArrayList<BrandStandardQuestion> brandStandardQuestionsSubmissions = new ArrayList<>();
        for (int i = 0; i < brandStandardSection.size(); i++) {
            ArrayList<BrandStandardQuestion> brandStandardQuestion = brandStandardSection.get(i).getQuestions();
            count = 0;
            for (int j = 0; j < brandStandardQuestion.size(); j++) {
                count += 1;
                BrandStandardQuestion question = brandStandardQuestion.get(j);
                brandStandardQuestionsSubmissions.add(question);
                String questionType = question.getQuestion_type();
                if (brandStandardQuestion.size() > 0 && (questionType.equalsIgnoreCase("textarea") || questionType.equalsIgnoreCase("text") || questionType.equalsIgnoreCase("number") || questionType.equalsIgnoreCase("datetime") || questionType.equalsIgnoreCase("date") || questionType.equalsIgnoreCase("slider") || questionType.equalsIgnoreCase("temperature") || questionType.equalsIgnoreCase("measurement") || questionType.equalsIgnoreCase("target")))
                {
                    if (AppUtils.isStringEmpty(question.getAudit_answer()) && question.getAudit_answer_na() == 0 && question.getIs_required() == 1) {
                        // String message="You have not answered question no " + count + " in section " + brandStandardSection.get(i).getSection_title();
                        AppUtils.toastDisplayForLong(activity, activity.getResources().getString(R.string.text_youhave_not_answer_question_section).replace("CCC",""+count).replace("SSS",brandStandardSection.get(i).getSection_title()));
                        return null;
                    }
                } else {
                    if (question.getAudit_option_id().size() == 0 && question.getAudit_answer_na() == 0 && question.getIs_required() == 1 && !questionType.equalsIgnoreCase("media")) {
                        //  AppUtils.toastDisplayForLong(activity, "You have not answered " + "question no. " + count + " in section " + brandStandardSection.get(i).getSection_title());
                        AppUtils.toastDisplayForLong(activity, activity.getResources().getString(R.string.text_youhave_not_answer_question_section).replace("CCC",""+count).replace("SSS",brandStandardSection.get(i).getSection_title()));
                        return null;
                    }
                }

                mMediaCount = question.getMedia_count();
                mCommentCount = question.getHas_comment();
                mActionPlanRequred=0;
                if (question.getAudit_option_id() != null && question.getAudit_option_id().size() > 0) {
                    for (int k = 0; k < question.getOptions().size(); k++) {
                        BrandStandardQuestionsOption option = question.getOptions().get(k);
                        if (question.getAudit_option_id() != null && question.getAudit_option_id().contains(new Integer(option.getOption_id()))) {
                            if (question.getQuestion_type().equalsIgnoreCase("checkbox")) {
                                if (mActionPlanRequred == 0)
                                    mActionPlanRequred = option.getAction_plan_required();
                                if (mMediaCount < option.getMedia_count())
                                    mMediaCount = option.getMedia_count();
                                if (mCommentCount < option.getCommentCount())
                                    mCommentCount = option.getCommentCount();
                            } else {
                                mMediaCount = option.getMedia_count();
                                mCommentCount = option.getCommentCount();
                                mActionPlanRequred = option.getAction_plan_required();
                                break;
                            }

                        }
                    }
                }
                Log.e("Media || Comment Count ", "===> " + mMediaCount + " || " + mCommentCount);

                if ((question.getAudit_option_id() != null && question.getAudit_option_id().size() > 0) || !TextUtils.isEmpty(question.getAudit_answer()) || questionType.equalsIgnoreCase("media")) {
                    if (mActionPlanRequred > 0 && question.getAction_plan() == null) {
                        // String message="Please Create the Action Plan for question no. " + count + " in section " + brandStandardSection.get(i).getSection_title();
                        AppUtils.toastDisplayForLong(activity,activity.getResources().getString(R.string.text_create_actionplan_for_question_section).replace("CCC",""+count).replace("SSS",brandStandardSection.get(i).getSection_title()) );
                        return null;
                    }
                    if (mMediaCount > 0 && question.getAudit_question_file_cnt() < mMediaCount) {
                        // String message="Please submit the required " + mMediaCount + " image(s) for question no. " + count + " in section " + brandStandardSection.get(i).getSection_title();
                        AppUtils.toastDisplayForLong(activity,activity.getResources().getString(R.string.text_submit_requiredmedia_for_question_section).replace("MMM",""+mMediaCount).replace("CCC",""+count).replace("SSS",brandStandardSection.get(i).getSection_title()));
                        return null;
                    }
                    if (mCommentCount > 0 && question.getAudit_comment().length() < mCommentCount) {
                        //  String  message="Please enter the minimum required " + mCommentCount + " characters comment for question no." + count + " in section " + brandStandardSection.get(i).getSection_title();
                        AppUtils.toastDisplayForLong(activity,activity.getResources().getString(R.string.text_submit_require_comment_for_question_section).replace("MMM",""+mCommentCount).replace("CCC",""+count).replace("SSS",brandStandardSection.get(i).getSection_title()));
                        return null;
                    }
                }

            }

            ArrayList<BrandStandardSubSection> brandStandardSubSections = brandStandardSection.get(i).getSub_sections();
            try {
                for (int k = 0; k < brandStandardSubSections.size(); k++) {
                    ArrayList<BrandStandardQuestion> brandStandardSubQuestion = brandStandardSubSections.get(k).getQuestions();
                    for (int j = 0; j < brandStandardSubQuestion.size(); j++) {
                        brandStandardQuestionsSubmissions.add(brandStandardSubQuestion.get(j));
                        count += 1;
                        BrandStandardQuestion question = brandStandardSubQuestion.get(j);
                        String questionType = question.getQuestion_type();
                        if (brandStandardSubQuestion.size() > 0 && (questionType.equalsIgnoreCase("textarea") || questionType.equalsIgnoreCase("text") || questionType.equalsIgnoreCase("number") || questionType.equalsIgnoreCase("datetime") || questionType.equalsIgnoreCase("date") || questionType.equalsIgnoreCase("slider") || questionType.equalsIgnoreCase("temperature") || questionType.equalsIgnoreCase("measurement") || questionType.equalsIgnoreCase("target"))) {
                            if (AppUtils.isStringEmpty(question.getAudit_answer()) && question.getAudit_answer_na() == 0 && question.getIs_required() == 1) {
                                //  AppUtils.toastDisplayForLong(activity, "You have not answered " + "question no. " + count + " in section " + brandStandardSection.get(i).getSection_title());
                                AppUtils.toastDisplayForLong(activity, activity.getResources().getString(R.string.text_youhave_not_answer_question_section).replace("CCC",""+count).replace("SSS",brandStandardSection.get(i).getSection_title()));

                                return null;
                            }
                        } else {
                            if (brandStandardSubQuestion.size() > 0 && (question.getAudit_option_id().size() == 0 && question.getAudit_answer_na() == 0 && question.getIs_required() == 1) && !questionType.equalsIgnoreCase("media")) {
                                //AppUtils.toastDisplayForLong(activity, "You have not answered " + "question no " + count + " in section " + brandStandardSection.get(i).getSection_title());
                                AppUtils.toastDisplayForLong(activity, activity.getResources().getString(R.string.text_youhave_not_answer_question_section).replace("CCC",""+count).replace("SSS",brandStandardSection.get(i).getSection_title()));

                                return null;
                            }
                        }
                        mMediaCount = question.getMedia_count();
                        mCommentCount = question.getHas_comment();
                        mActionPlanRequred=0;
                        if (question.getAudit_option_id() != null && question.getAudit_option_id().size() > 0)
                        {
                            for (int y = 0; y < question.getOptions().size(); y++) {
                                BrandStandardQuestionsOption option = question.getOptions().get(y);
                                if (question.getAudit_option_id() != null && question.getAudit_option_id().contains(new Integer(option.getOption_id())))
                                {

                                    if (question.getQuestion_type().equalsIgnoreCase("checkbox")) {
                                        if (mActionPlanRequred == 0)
                                            mActionPlanRequred = option.getAction_plan_required();
                                        if (mMediaCount < option.getMedia_count())
                                            mMediaCount = option.getMedia_count();
                                        if (mCommentCount < option.getCommentCount())
                                            mCommentCount = option.getCommentCount();
                                    } else {
                                        mMediaCount = option.getMedia_count();
                                        mCommentCount = option.getCommentCount();
                                        mActionPlanRequred = option.getAction_plan_required();
                                        break;
                                    }
                                }
                            }
                        }
                        if ((question.getAudit_option_id() != null && question.getAudit_option_id().size() > 0) || (question.getAudit_answer() != null && question.getAudit_answer().length() > 0)) {
                            if (mActionPlanRequred > 0 && question.getAction_plan() == null) {
                                AppUtils.toastDisplayForLong(activity,activity.getResources().getString(R.string.text_create_actionplan_for_question_section).replace("CCC",""+count).replace("SSS",brandStandardSection.get(i).getSection_title()) );
                                //   AppUtils.toastDisplayForLong(activity, "Please Create the Action Plan for question no. " + count + " in section " + brandStandardSection.get(i).getSection_title());
                                return null;
                            }
                            if (mMediaCount > 0 && question.getAudit_question_file_cnt() < mMediaCount) {
                                AppUtils.toastDisplayForLong(activity,activity.getResources().getString(R.string.text_submit_requiredmedia_for_question_section).replace("MMM",""+mMediaCount).replace("CCC",""+count).replace("SSS",brandStandardSection.get(i).getSection_title()));
                                // AppUtils.toastDisplayForLong(activity, "Please submit the required " + mMediaCount + " image(s) for question no. " + count + " in section " + brandStandardSection.get(i).getSection_title());
                                return null;
                            }

                            if (mCommentCount > 0 && question.getAudit_comment().length() < mCommentCount) {
                                AppUtils.toastDisplayForLong(activity,activity.getResources().getString(R.string.text_submit_require_comment_for_question_section).replace("MMM",""+mCommentCount).replace("CCC",""+count).replace("SSS",brandStandardSection.get(i).getSection_title()));
                                //  String message="Please enter the minimum required " + mCommentCount + " characters comment for question no." + count +" in section " + brandStandardSection.get(i).getSection_title();
                                // AppUtils.toastDisplayForLong(activity, message);
                                return null;
                            }

                        }

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return AppUtils.getQuestionsArray(brandStandardQuestionsSubmissions);
    }


    public static JSONArray getOptionQuestionArray(ArrayList<BrandStandardQuestionsOption> optionsArray,ArrayList<Integer> auditOptonID) {
        try {
            ArrayList<JSONObject> mArrayList=new ArrayList<>();
            for (int i = 0; i < optionsArray.size(); i++)
            {
                BrandStandardQuestionsOption questionsOption = optionsArray.get(i);
                if (questionsOption.getQuestions() != null && questionsOption.getQuestions().size() > 0)
                {
                    for (int j = 0; j < questionsOption.getQuestions().size(); j++)
                    {
                        if (auditOptonID.contains(new Integer(questionsOption.getOption_id())))
                        {
                            if (questionsOption.getQuestions().get(j).getQuestion_type().equalsIgnoreCase("media") || (!TextUtils.isEmpty(questionsOption.getQuestions().get(j).getAudit_answer()) && !questionsOption.getQuestions().get(j).getAudit_answer().equalsIgnoreCase("0"))) {
                                //  JSONArray jsonArray = new JSONArray();
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("option_id", questionsOption.getOption_id());
                                jsonObject.put("questions", geSubQusetionsArrayArray(questionsOption.getQuestions()));
                                mArrayList.add(jsonObject);
                                break;
                                //jsonArray.put(jsonObject);
                                //return jsonArray;

                            } else {
                                if (questionsOption.getQuestions().get(j).getAudit_option_id() != null && questionsOption.getQuestions().get(j).getAudit_option_id().size() > 0) {
                                    // JSONArray jsonArray = new JSONArray();
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("option_id", questionsOption.getOption_id());
                                    jsonObject.put("questions", geSubQusetionsArrayArray(questionsOption.getQuestions()));
                                    mArrayList.add(jsonObject);
                                    break;
                                    // jsonArray.put(jsonObject);
                                    //return jsonArray;
                                }
                            }
                        }
                    }
                }
            }
            return new JSONArray(mArrayList);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JSONArray();
    }

    public static JSONArray geSubQusetionsArrayArray(ArrayList<BrandStandardQuestion> questionsArray) {

        try {
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < questionsArray.size(); i++) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("question_id", questionsArray.get(i).getQuestion_id());
                jsonObject.put("audit_option_id", getOptionIdArray(questionsArray.get(i).getAudit_option_id()));
                jsonObject.put("audit_answer", questionsArray.get(i).getAudit_answer());
                jsonObject.put("audit_comment", questionsArray.get(i).getAudit_comment());
                jsonArray.put(jsonObject);
            }
            return jsonArray;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JSONArray();
    }

    public static JSONArray getQuestionsArray(ArrayList<BrandStandardQuestion> brandStandardQuestions) {
        JSONArray jsonArray = new JSONArray();
        if (brandStandardQuestions == null || brandStandardQuestions.size() == 0)
            return jsonArray;

        for (int i = 0; i < brandStandardQuestions.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("question_id", brandStandardQuestions.get(i).getQuestion_id());
                jsonObject.put("audit_answer_na", brandStandardQuestions.get(i).getAudit_answer_na());
                jsonObject.put("audit_comment", brandStandardQuestions.get(i).getAudit_comment());
                jsonObject.put("audit_option_id", AppUtils.getOptionIdArray(brandStandardQuestions.get(i).getAudit_option_id()));
                jsonObject.put("options", getOptionQuestionArray(brandStandardQuestions.get(i).getOptions(),brandStandardQuestions.get(i).getAudit_option_id()));
                jsonObject.put("audit_answer", brandStandardQuestions.get(i).getAudit_answer());
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonArray;
    }

    public static JSONArray getOptionIdArray(ArrayList<Integer> arrayList) {
        JSONArray jsArray = null;
        try {
            if (arrayList != null && arrayList.size() > 0)
                jsArray = new JSONArray(arrayList);
            else
                jsArray = new JSONArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsArray;
    }


    @SuppressLint("NewApi")
    public static void datePicker(final Context context, final TextView editText, final boolean needTimePicker, final BrandStandardQuestion brandStandardQuestion) {
        Calendar c = Calendar.getInstance();
        // Process to get Current Date
        final int currentYear = c.get(Calendar.YEAR);
        final int currentMonth = c.get(Calendar.MONTH);
        final int currentDay = c.get(Calendar.DAY_OF_MONTH);
        final int currentHour = c.get(Calendar.HOUR_OF_DAY);
        final int currentMinute = c.get(Calendar.MINUTE);
        int setYear = currentYear;
        int setMonth = currentMonth;
        int setDay = currentDay;
        int setHour = currentHour;
        int setMinute = currentMinute;


        int finalSetHour = setHour;
        int finalSetMinute = setMinute;
        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        if (year <= (currentYear) /*&& monthOfYear<mMonth && dayOfMonth<mDay*/) {
                            String strYEAR = String.valueOf(year);

                            //							Adding 0, If Date in Single Digit
                            String strDATE = "";
                            if (dayOfMonth < 10)
                                strDATE = "0" + dayOfMonth;
                            else
                                strDATE = String.valueOf(dayOfMonth);

                            //Adding 0, If Month in Single Digit
                            String strMONTH = "";
                            int month = monthOfYear + 1;
                            if (month < 10)
                                strMONTH = "0" + month;
                            else
                                strMONTH = String.valueOf(month);

                            // Display Selected date in TextView
                            /*DD/MM/YYYY*/
                            String date = strYEAR + "-" + strMONTH + "-" + strDATE;

                            if (needTimePicker)
                                timePicker(context, editText, date, brandStandardQuestion);
                            else {
                                editText.setText(date);
                                brandStandardQuestion.setAudit_answer(date);
                            }
                        } else {
                            Log.e("", "Invalid Date!");
                        }
                    }
                }, setYear, setMonth, setDay);
        datePickerDialog.show();
        //        datePickerDialog.getDatePicker().setMaxDate(MDateUtils.getMSfromDate(
        //                (currentDay+1)+"/"+(currentMonth+1)+"/"+(currentYear), "dd/MM/yyyy"));
    }

    public static void timePicker(Context context, final TextView editText, final String date, final BrandStandardQuestion brandStandardQuestion) {
        String time = "";
        Calendar c = Calendar.getInstance();
        // Process to get Current Date
        final int currentHour = c.get(Calendar.HOUR_OF_DAY);
        final int currentMinute = c.get(Calendar.MINUTE);
        int setHour = currentHour;
        int setMinute = currentMinute;

        TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String strMinute = String.valueOf(minute);
                String strHour = String.valueOf(hourOfDay);

                if ((strHour).length() == 1) {
                    strHour = "0" + strHour;
                }
                if ((strMinute).length() == 1) {
                    strMinute = "0" + strMinute;
                }
                String time = (date.isEmpty() ? date : (date + " ")) + strHour + ":" + strMinute + ":00";
                editText.setText(time);
                brandStandardQuestion.setAudit_answer("" + time);
            }
        }, setHour, setMinute, true);
        timePickerDialog.show();
    }

    public static Bitmap resizeImage(Uri uriImage, Bitmap image, int maxWidth, int maxHeight) throws IOException {
        if (maxHeight > 0 && maxWidth > 0) {
            int width = image.getWidth();
            int height = image.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (ratioMax > ratioBitmap) {
                finalWidth = (int) ((float) maxHeight * ratioBitmap);
            } else {
                finalHeight = (int) ((float) maxWidth / ratioBitmap);
            }

            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);


            // image= rotateImageIfRequired(image,uriImage);
            return image;
            // return rotateImageIfRequired(image,uriImage);
        } else {
            return image;
        }
    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();

            deleteDir(dir);
        } catch (Exception e) { e.printStackTrace();}
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }


}

