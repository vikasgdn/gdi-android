package com.gdi.activity.internalaudit.adapter;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.gdi.activity.BaseActivity;
import com.gdi.activity.internalaudit.AddAttachmentActivity;
import com.gdi.activity.internalaudit.model.audit.AddAttachment.AddAttachmentInfo;
import com.gdi.api.DeleteBSAttachmentRequest;
import com.gdi.api.DeleteBSQuestionAttachmentRequest;
import com.gdi.api.NetworkURL;
import com.gdi.api.VolleyNetworkRequest;
import com.gdi.apppreferences.AppPreferences;
import com.gdi.hotel.mystery.audits.R;
import com.gdi.utils.AppConstant;
import com.gdi.utils.AppLogger;
import com.gdi.utils.AppUtils;
import com.gdi.utils.Headers;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;


import org.json.JSONObject;

import java.util.ArrayList;

public class AddAttachmentAdapter extends RecyclerView.Adapter<AddAttachmentAdapter.AddAttachmentViewHolder> {

    private Context context;
    private ArrayList<AddAttachmentInfo> orderData;
    private  String attachType;
    private String auditId;
    private String sectionGroupId;
    private String sectionId;
    private String questionId;

    public AddAttachmentAdapter(Context context, ArrayList<AddAttachmentInfo> orderData, String attachType, String auditId, String sectionGroupId, String sectionId, String questionId) {
        this.context = context;
        this.orderData = orderData;
        this.attachType = attachType;
        this.auditId = auditId;
        this.sectionGroupId = sectionGroupId;
        this.sectionId = sectionId;
        this.questionId = questionId;
    }

    @NonNull
    @Override
    public AddAttachmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_attachment_layout, parent, false);

        return new AddAttachmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddAttachmentViewHolder holder, int position) {
        final AddAttachmentInfo addAttachmentInfo = orderData.get(position);
        String fileType = addAttachmentInfo.getFile_type();
        holder.attachedImage.setTag(R.id.pos_tag,""+position);
        if (fileType.contains("image/")) {
            holder.playButton.setVisibility(View.GONE);
            if (!AppUtils.isStringEmpty(addAttachmentInfo.getThumb_url())) {
                RequestOptions requestOptions = new RequestOptions()
                        .override(600,200)
                        .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
                        .skipMemoryCache(true);
                Glide.with(context).load(Headers.getUrlWithHeaders(addAttachmentInfo.getThumb_url(), AppPreferences.INSTANCE.getAccessToken(context))).apply(requestOptions).into(holder.attachedImage);
            }
        }
        else
        {
            if (!AppUtils.isStringEmpty(addAttachmentInfo.getFile_url())) {
                RequestOptions requestOptions = new RequestOptions();
                requestOptions.placeholder(R.mipmap.video_preview);
                requestOptions.error(R.mipmap.video_preview);
                Glide.with(context).load(Headers.getUrlWithHeaders(addAttachmentInfo.getFile_url(), AppPreferences.INSTANCE.getAccessToken(context))).apply(requestOptions).into(holder.attachedImage);
            }
            holder.playButton.setVisibility(View.VISIBLE);

        }
        holder.imageName.setText(addAttachmentInfo.getFile_name());


        holder.attachedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               try {
                   /*  int pos = Integer.parseInt(view.getTag(R.id.pos_tag).toString());
                    if (orderData.get(pos).getFile_type().contains("image")) {
                        Intent intent = new Intent(context, EditAttachmentActivity.class);
                        intent.putExtra("auditId", auditId);
                        intent.putExtra("sectionGroupId", sectionGroupId);
                        intent.putExtra("sectionId", sectionId);
                        intent.putExtra("questionId", questionId);
                        intent.putExtra("attachmentDetail", addAttachmentInfo);
                        intent.putExtra("attachType", attachType);
                        intent.putExtra("editable", "0");
                        context.startActivity(intent);
                    } else {
                        ExoVideoPlayer.start(context,orderData.get(pos).getFile_url(),"");
                    }*/
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        holder.deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteNotificationDialog(addAttachmentInfo);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderData.size();
    }

    public class AddAttachmentViewHolder extends RecyclerView.ViewHolder {

        ImageView attachedImage;
        ImageView playButton;
        TextView imageName;
        RelativeLayout deleteIcon;

        public AddAttachmentViewHolder(View itemView) {
            super(itemView);

            attachedImage = itemView.findViewById(R.id.iv_add_attachment_image);
            playButton = itemView.findViewById(R.id.iv_playbutton);
            imageName = itemView.findViewById(R.id.tv_add_attachment_file_name);
            deleteIcon = itemView.findViewById(R.id.iv_remove_attachment_icon);
        }
    }

    private void deleteNotificationDialog(final AddAttachmentInfo addAttachmentInfo) {

        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);

        dialog.setTitle(R.string.app_name);
        dialog.setMessage("Do you want to delete the attachment");

        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (attachType) {
                    case "bsSection":
                        deleteBsSectionAndQuestionAttachment(addAttachmentInfo, AppConstant.BS_SECTION);
                        break;
                    case "bsQuestion":
                        deleteBsSectionAndQuestionAttachment(addAttachmentInfo,AppConstant.BS_QUESTION);
                        break;
                }
            }
        });
        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        });
        dialog.create().show();
    }

    private void deleteBsSectionAndQuestionAttachment(AddAttachmentInfo addAttachmentInfo,String type) {
        ((AddAttachmentActivity) context).showProgressDialog();
        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AppLogger.e("", "DeleteBSResponse: " + response);
                try {
                    if (type.equalsIgnoreCase(AppConstant.BS_SECTION)) {
                        JSONObject object = new JSONObject(response);
                        if (!object.getBoolean(AppConstant.RES_KEY_ERROR)) {
                            AppUtils.toast((BaseActivity) context, object.getString(AppConstant.RES_KEY_MESSAGE));
                            ((AddAttachmentActivity) context).getOldMediaAttachmentList("bsSection");
                        } else if (object.getBoolean(AppConstant.RES_KEY_ERROR)) {
                            AppUtils.toast((BaseActivity) context, object.getString(AppConstant.RES_KEY_MESSAGE));
                        }
                    }
                    else
                    {
                        JSONObject object = new JSONObject(response);
                        if (!object.getBoolean(AppConstant.RES_KEY_ERROR)) {
                            AppUtils.toast((BaseActivity) context, object.getString(AppConstant.RES_KEY_MESSAGE));
                            ((AddAttachmentActivity) context).getOldMediaAttachmentList("bsQuestion");
                        } else if (object.getBoolean(AppConstant.RES_KEY_ERROR)) {
                            AppUtils.toast((BaseActivity) context, object.getString(AppConstant.RES_KEY_MESSAGE));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ((AddAttachmentActivity) context).hideProgressDialog();
            }

        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ((AddAttachmentActivity) context).hideProgressDialog();
                AppLogger.e("", "AddAttachmentError: " + error.getMessage());
                AppUtils.toast((BaseActivity) context, "Server temporary unavailable, Please try again");

            }
        };

        String url = NetworkURL.BS_DELETE_ATTACHMENT_NEW;

            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                FirebaseAuth.getInstance().getCurrentUser().getIdToken(true)
                        .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                            public void onComplete(@NonNull Task<GetTokenResult> task) {
                                if (task.isSuccessful()) {
                                    String token = task.getResult().getToken();
                                    if (type.equalsIgnoreCase(AppConstant.BS_SECTION)) {
                                        DeleteBSAttachmentRequest editBSAttachmentRequest = new DeleteBSAttachmentRequest(AppPreferences.INSTANCE.getAccessToken(context), url, auditId, addAttachmentInfo.getAudit_section_file_id(), "", "", token, context, stringListener, errorListener);
                                        VolleyNetworkRequest.getInstance(context).addToRequestQueue(editBSAttachmentRequest);
                                    } else {
                                        DeleteBSQuestionAttachmentRequest editBSAttachmentRequest = new DeleteBSQuestionAttachmentRequest(AppPreferences.INSTANCE.getAccessToken(context), url, auditId, addAttachmentInfo.getAudit_section_file_id(), addAttachmentInfo.getAudit_question_file_id(), token, context, stringListener, errorListener);
                                        VolleyNetworkRequest.getInstance(context).addToRequestQueue(editBSAttachmentRequest);
                                    }

                                }
                            }
                        });
            }


    }


}
