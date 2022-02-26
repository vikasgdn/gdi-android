package com.gdi.dialog;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.gdi.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Vikas on 22/9/16.
 */

public class AppDialog {


    public static void brandstandardTitleMessageDialog(final Activity activity,String title,String location,String checklist) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_location_title_checklist);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = (int) (activity.getResources().getDisplayMetrics().widthPixels - activity.getResources().getDimension(R.dimen.d_10dp));
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView titleTV=(TextView)dialog.findViewById(R.id.tv_titleheader);
        TextView locationTV=(TextView)dialog.findViewById(R.id.tv_location);
        TextView checklistTV=(TextView)dialog.findViewById(R.id.tv_checklist);

        try {

            titleTV.setText(""+title);
            locationTV.setText(":"+location);
            checklistTV.setText(""+checklist);

            dialog.findViewById(R.id.iv_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        dialog.show();

    }
  /*  public static void showChangeLanguageDialog(final Activity activity )
    {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_language_change);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = (int) (activity.getResources().getDisplayMetrics().widthPixels - activity.getResources().getDimension(R.dimen.d_10dp));
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        RadioGroup rbGroup = (RadioGroup) dialog.findViewById(R.id.rd_languagechange);
        RadioButton rbItalian = (RadioButton) dialog.findViewById(R.id.radio_italian);
        RadioButton rbEnglish = (RadioButton) dialog.findViewById(R.id.radio_english);
        RadioButton rbFranch = (RadioButton) dialog.findViewById(R.id.radio_france);
        RadioButton rbGerman = (RadioButton) dialog.findViewById(R.id.radio_german);

        if(AppPreferences.INSTANCE.isItalianLanguage().equalsIgnoreCase("it"))
            rbItalian.setChecked(true);
       else if(AppPreferences.INSTANCE.isItalianLanguage().equalsIgnoreCase("fr"))
            rbFranch.setChecked(true);
       else if(AppPreferences.INSTANCE.isItalianLanguage().equalsIgnoreCase("de"))
            rbGerman.setChecked(true);
        else
            rbEnglish.setChecked(true);

        try
        {
            dialog.findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String value = ((RadioButton)dialog.findViewById(rbGroup.getCheckedRadioButtonId() )).getText().toString();
                    Toast.makeText(activity,"Language change to "+value,Toast.LENGTH_SHORT).show();

                    if(value.equalsIgnoreCase(activity.getResources().getString(R.string.text_italian))) {
                        AppPreferences.INSTANCE.setLanguage("it");
                        rbItalian.setChecked(true);
                        LocaleHelper.setLocale(activity, "it");
                        Intent intent1 = new Intent(activity, DashBoardActivity.class);
                        activity.startActivity(intent1);
                    }
                    else if(value.equalsIgnoreCase(activity.getResources().getString(R.string.text_france)))
                    {
                        AppPreferences.INSTANCE.setLanguage("fr");
                        rbFranch.setChecked(true);
                        LocaleHelper.setLocale(activity, "fr");
                        Intent intent1 = new Intent(activity, DashBoardActivity.class);
                        activity.startActivity(intent1);
                    }
                    else if(value.equalsIgnoreCase(activity.getResources().getString(R.string.text_german)))
                    {
                        AppPreferences.INSTANCE.setLanguage("de");
                        rbGerman.setChecked(true);
                        LocaleHelper.setLocale(activity, "de");
                        Intent intent1 = new Intent(activity, DashBoardActivity.class);
                        activity.startActivity(intent1);
                    }
                    else
                    {
                        AppPreferences.INSTANCE.setLanguage("en");
                        rbEnglish.setChecked(true);
                        LocaleHelper.setLocale(activity, "en");
                        Intent intent1 = new Intent(activity, DashBoardActivity.class);
                        activity.startActivity(intent1);
                    }
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        dialog.show();

    }*/

   /* public static void showExitDialog(final Activity activity )
    {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_exit);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = (int) (activity.getResources().getDisplayMetrics().widthPixels - activity.getResources().getDimension(R.dimen.d_10dp));
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final TextView tvMessage;
        tvMessage = (TextView) dialog.findViewById(R.id.tv_dialog_message);

        try {
            tvMessage.setText(activity.getResources().getString(R.string.are_you_want_exit));

            dialog.findViewById(R.id.btn_no).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.findViewById(R.id.btn_yes).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(intent);
                    activity.finish();
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        dialog.show();
    }*/



  /*  public static void showPlayStoreLinkDialog(final Activity activity )
    {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_playstore);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = (int) (activity.getResources().getDisplayMetrics().widthPixels - activity.getResources().getDimension(R.dimen.d_10dp));
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        final TextView tvMessage;
        tvMessage = (TextView) dialog.findViewById(R.id.tv_dialog_message);

        try {
            tvMessage.setText(activity.getResources().getString(R.string.There_are_some_update));

            dialog.findViewById(R.id.btn_yes).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.zlate87.app_activity"));
                    activity.startActivity(i);
                    activity.finish();
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        dialog.show();
    }
*/


}
