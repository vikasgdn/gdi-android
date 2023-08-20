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

import com.gdi.activity.MainActivity;
import com.gdi.activity.internalaudit.AuditSubmitSignatureActivity;
import com.gdi.activity.internalaudit.BrandStandardAuditActivity;
import com.gdi.activity.internalaudit.BrandStandardAuditActivityPagingnation;
import com.gdi.activity.internalaudit.BrandStandardOptionsBasedQuestionActivity;
import com.gdi.activity.internalaudit.InternalAuditDashboardActivity;
import com.gdi.hotel.mystery.audits.R;

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

    public static   void messageDialogWithOKButton(final Activity activity,String message) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_with_okbutton);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = (int) (activity.getResources().getDisplayMetrics().widthPixels - activity.getResources().getDimension(R.dimen.d_10dp));
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView textView=dialog.findViewById(R.id.tv_dialog_message);
        textView.setText(""+message);
        TextView textYes=dialog.findViewById(R.id.tv_yes);

        try {
            dialog.findViewById(R.id.tv_yes).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   if(activity instanceof AuditSubmitSignatureActivity)
                    {
                        Intent intent = new Intent(activity, InternalAuditDashboardActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        activity.startActivity(intent);
                        activity.finish();
                    }
                    dialog.dismiss();
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
        dialog.show();

    }

    public static   void messageDialogWithYesNo(final Activity activity,String message) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_confirm_na);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = (int) (activity.getResources().getDisplayMetrics().widthPixels - activity.getResources().getDimension(R.dimen.d_10dp));
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView textView=dialog.findViewById(R.id.tv_dialog_message);
        textView.setText(message+"\n "+activity.getResources().getString(R.string.text_douwant_to_continue));

        try {
            dialog.findViewById(R.id.tv_yes).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.findViewById(R.id.tv_no).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (activity instanceof BrandStandardAuditActivity) {
                        ((BrandStandardAuditActivity) activity).isDialogSaveClicked=true;
                        ((BrandStandardAuditActivity) activity).saveBrandStandardQuestion();
                    }
                    else if (activity instanceof BrandStandardAuditActivityPagingnation)
                        ((BrandStandardAuditActivityPagingnation)activity).saveBrandStandardQuestion();
                    else
                    {
                        ((BrandStandardOptionsBasedQuestionActivity)activity).finish();
                    }
                    dialog.dismiss();
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
        dialog.show();

    }



}
