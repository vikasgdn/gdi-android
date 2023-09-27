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
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
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
import com.gdi.activity.internalaudit.model.audit.BrandStandard.BrandStandardQuestion;
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
                public void onClick(View v)
                {
                    if (activity instanceof BrandStandardAuditActivity) {
                        ((BrandStandardAuditActivity) activity).isDialogSaveClicked=true;
                        // ((BrandStandardAuditActivity)activity).finish();
                        ((BrandStandardAuditActivity) activity).handelNextPreviousPopUpYESNoClick();
                    }
                    else if (activity instanceof BrandStandardAuditActivityPagingnation)
                    {
                        // ((BrandStandardAuditActivityPagingnation)activity).saveBrandStandardQuestion();
                        ((BrandStandardAuditActivityPagingnation)activity).finish();
                    }
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


    public static void showEnterCommentForQuestionBS(BrandStandardQuestion bsQuestion, final Activity activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_question_comment_bs);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = (int) (activity.getResources().getDisplayMetrics().widthPixels - activity.getResources().getDimension(R.dimen.d_10dp));
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        final EditText mCommentET=(EditText)dialog.findViewById(R.id.et_commentbox);
        final TextView mCommentErrorTv=(TextView)dialog.findViewById(R.id.tv_commenterror);
        if (!TextUtils.isEmpty(bsQuestion.getAudit_comment()))
            mCommentET.setText(bsQuestion.getAudit_comment());

        try {
            // mCommentErrorTv.setText(activity.getResources().getString(R.string.text_please_enterminimum).replace("CCC",""+bsQuestion.getOptions().));


            dialog.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.findViewById(R.id.tv_send).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(TextUtils.isEmpty(mCommentET.getText().toString()))
                        mCommentErrorTv.setVisibility(View.VISIBLE);
                    else
                    {
                        bsQuestion.setAudit_comment(mCommentET.getText().toString());
                        // AppUtils.hideKeyboard(activity);
                        if (activity instanceof BrandStandardAuditActivity)
                            ((BrandStandardAuditActivity)activity).saveSingleBrandStandardQuestionEveryClick(bsQuestion);
                        else if (activity instanceof BrandStandardAuditActivityPagingnation)
                            ((BrandStandardAuditActivityPagingnation)activity).saveSingleBrandStandardQuestionEveryClick(bsQuestion);
                        else
                            ((BrandStandardOptionsBasedQuestionActivity)activity).saveSingleBrandStandardQuestionEveryClick(bsQuestion);

                        dialog.dismiss();
                    }

                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
        dialog.show();

    }

    public static void showeTexTypeAnswerForQuestionBS(BrandStandardQuestion bsQuestion, String hint, final Activity activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_question_text_answer);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = (int) (activity.getResources().getDisplayMetrics().widthPixels - activity.getResources().getDimension(R.dimen.d_10dp));
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        final EditText mAnswerET=(EditText)dialog.findViewById(R.id.et_answerbox);
        final TextView mErrorTv=(TextView)dialog.findViewById(R.id.tv_error);

        try {

            Log.e("getQuestion_type()","===>"+bsQuestion.getQuestion_type());
            if (bsQuestion.getQuestion_type().equalsIgnoreCase("textarea"))
            {
                mAnswerET.setInputType(InputType.TYPE_CLASS_TEXT);
                mAnswerET.setMinLines(4);
            }
            else if (bsQuestion.getQuestion_type().equalsIgnoreCase("text"))
            {
                mAnswerET.setInputType(InputType.TYPE_CLASS_TEXT);
                mAnswerET.setMinLines(2);

            }
            else
            {
                mAnswerET.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL|InputType.TYPE_NUMBER_FLAG_SIGNED);
                mAnswerET.setMinLines(1);
            }

          /*  else if (bsQuestion.getQuestion_type().equalsIgnoreCase("number") || bsQuestion.getQuestion_type().equalsIgnoreCase("temperature") || bsQuestion.getQuestion_type().equalsIgnoreCase("measurement") || bsQuestion.getQuestion_type().equalsIgnoreCase("target") )
            {
                mAnswerET.setMinLines(2);
                mAnswerET.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL|InputType.TYPE_NUMBER_FLAG_SIGNED);
            }
*/

            mAnswerET.setHint(""+hint);
            if (!TextUtils.isEmpty(bsQuestion.getAudit_answer()))
                mAnswerET.setText(bsQuestion.getAudit_answer());


            dialog.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.findViewById(R.id.tv_send).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(TextUtils.isEmpty(mAnswerET.getText().toString()))
                        mErrorTv.setVisibility(View.VISIBLE);
                    else
                    {
                        bsQuestion.setAudit_answer(mAnswerET.getText().toString());
                        if (activity instanceof BrandStandardAuditActivity)
                            ((BrandStandardAuditActivity)activity).saveSingleBrandStandardQuestionEveryClick(bsQuestion);
                        else if (activity instanceof BrandStandardAuditActivityPagingnation)
                            ((BrandStandardAuditActivityPagingnation)activity).saveSingleBrandStandardQuestionEveryClick(bsQuestion);
                        else
                            ((BrandStandardOptionsBasedQuestionActivity)activity).saveSingleBrandStandardQuestionEveryClick(bsQuestion);

                        dialog.dismiss();
                    }

                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
        //vikas
        dialog.show();

    }

}
