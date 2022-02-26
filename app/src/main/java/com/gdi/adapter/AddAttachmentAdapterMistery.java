package com.gdi.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gdi.R;
import com.gdi.activity.Audit.AddAttachmentActivityMistery;
import com.gdi.activity.Audit.EditAttachmentActivity;
import com.gdi.activity.BaseActivity;
import com.gdi.activity.ExoVideoPlayer;
import com.gdi.api.ApiEndPoints;
import com.gdi.api.DeleteBSAttachmentRequest;
import com.gdi.api.DeleteBSQuestionAttachmentRequest;
import com.gdi.api.DeleteBSQuestionAttachmentRequestMistery;
import com.gdi.api.VolleyNetworkRequest;
import com.gdi.model.audit.AddAttachment.AddAttachmentInfo;
import com.gdi.utils.ApiResponseKeys;
import com.gdi.utils.AppLogger;
import com.gdi.utils.AppPrefs;
import com.gdi.utils.AppUtils;
import com.gdi.utils.Headers;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AddAttachmentAdapterMistery extends RecyclerView.Adapter<AddAttachmentAdapterMistery.AddAttachmentViewHolder> {

    private Context context;
    private ArrayList<AddAttachmentInfo> orderData;
    String attachType;
    String auditId;
    String sectionGroupId;
    String sectionId;
    String questionId;
    String editable;

    public AddAttachmentAdapterMistery(Context context, ArrayList<AddAttachmentInfo> orderData,
                                       String attachType, String auditId, String sectionGroupId, String sectionId, String questionId, String editable) {
        this.context = context;
        this.orderData = orderData;
        this.attachType = attachType;
        this.auditId = auditId;
        this.sectionGroupId = sectionGroupId;
        this.sectionId = sectionId;
        this.questionId = questionId;
        this.editable = editable;
    }

    @NonNull
    @Override
    public AddAttachmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_attachment_layout,
                parent, false);

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
              //  Glide.with(context).load(Headers.getUrlWithHeaders(addAttachmentInfo.getThumb_url(), AppPrefs.getAccessToken(context))).into(holder.attachedImage);
                Glide.with(context).load(Headers.getUrlWithHeaders(addAttachmentInfo.getThumb_url(),AppPrefs.getAccessToken(context))).apply(new RequestOptions().override(600, 150)).into(holder.attachedImage);

            }
        }
        else
        {
            holder.attachedImage.setImageResource(R.mipmap.video_preview);
            holder.playButton.setVisibility(View.VISIBLE);

        }
        holder.imageName.setText(addAttachmentInfo.getFile_name());



        holder.attachedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    int pos = Integer.parseInt(view.getTag(R.id.pos_tag).toString());
                    if (orderData.get(pos).getFile_type().contains("video")) {
                        Intent intent = new Intent(context, ExoVideoPlayer.class);
                        intent.putExtra("url", orderData.get(pos).getFile_url());
                        context.startActivity(intent);
                    } else {
                        Intent intent = new Intent(context, EditAttachmentActivity.class);
                        intent.putExtra("auditId", auditId);
                        intent.putExtra("sectionGroupId", sectionGroupId);
                        intent.putExtra("sectionId", sectionId);
                        intent.putExtra("questionId", questionId);
                        intent.putExtra("attachmentDetail", addAttachmentInfo);
                        intent.putExtra("attachType", attachType);
                        intent.putExtra("editable", editable);
                        context.startActivity(intent);
                    }
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

        if (editable.equals("0")){
            holder.deleteIcon.setVisibility(View.VISIBLE);
        }else {
            holder.deleteIcon.setVisibility(View.GONE);
        }

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
            imageName = itemView.findViewById(R.id.tv_add_attachment_file_name);
            playButton = itemView.findViewById(R.id.iv_playbutton);
            deleteIcon = itemView.findViewById(R.id.iv_remove_attachment_icon);
        }
    }

    private void deleteNotificationDialog(final AddAttachmentInfo addAttachmentInfo) {

        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);

        dialog.setTitle("GDI");
        dialog.setMessage("Do you want to delete the attachment");

        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (attachType) {
                    case "bsSection":
                        deleteBsFileAttachment(addAttachmentInfo);
                        break;
                    case "bsQuestion":
                        deleteQuestionFileAttachment(addAttachmentInfo);
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

    private void deleteBsFileAttachment(AddAttachmentInfo addAttachmentInfo) {
        ((AddAttachmentActivityMistery) context).showProgressDialog();
        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AppLogger.e("", "DeleteBSResponse: " + response);
                try {
                    JSONObject object = new JSONObject(response);

                    if (!object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        AppUtils.toast((BaseActivity) context, object.getString(ApiResponseKeys.RES_KEY_MESSAGE));

                        ((AddAttachmentActivityMistery) context).getBsAttachmentList();
                    } else if (object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        AppUtils.toast((BaseActivity) context, object.getString(ApiResponseKeys.RES_KEY_MESSAGE));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ((AddAttachmentActivityMistery) context).hideProgressDialog();
            }

        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ((AddAttachmentActivityMistery) context).hideProgressDialog();
                AppLogger.e("", "AddAttachmentError: " + error.getMessage());
                AppUtils.toast((BaseActivity) context, "Server temporary unavailable, Please try again");

            }
        };

        String url = ApiEndPoints.BSDELETEATTACHMENT_MISTERY;
        DeleteBSAttachmentRequest editBSAttachmentRequest = new DeleteBSAttachmentRequest(AppPrefs.getAccessToken(context), url, auditId, addAttachmentInfo.getAudit_section_file_id(),sectionGroupId,sectionId,stringListener, errorListener);
        VolleyNetworkRequest.getInstance(context).addToRequestQueue(editBSAttachmentRequest);
    }

    private void deleteQuestionFileAttachment(AddAttachmentInfo addAttachmentInfo) {
        ((AddAttachmentActivityMistery) context).showProgressDialog();
        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AppLogger.e("", "DeleteBSQuestionResponse: " + response);
                try {
                    JSONObject object = new JSONObject(response);

                    if (!object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        AppUtils.toast((BaseActivity) context, object.getString(ApiResponseKeys.RES_KEY_MESSAGE));
                        ((AddAttachmentActivityMistery) context).getQuestionAttachmentList();
                    } else if (object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        AppUtils.toast((BaseActivity) context, object.getString(ApiResponseKeys.RES_KEY_MESSAGE));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ((AddAttachmentActivityMistery) context).hideProgressDialog();
            }

        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ((AddAttachmentActivityMistery) context).hideProgressDialog();
                AppLogger.e("", "AddAttachmentError: " + error.getMessage());
                AppUtils.toast((BaseActivity) context, "Server temporary unavailable, Please try again");

            }
        };

        String url = ApiEndPoints.BSDELETEATTACHMENT_MISTERY;
        DeleteBSQuestionAttachmentRequestMistery editBSAttachmentRequest = new DeleteBSQuestionAttachmentRequestMistery(
                AppPrefs.getAccessToken(context), url, auditId,
                addAttachmentInfo.getAudit_section_file_id(),
                addAttachmentInfo.getAudit_question_file_id(),questionId,
                stringListener, errorListener);
        VolleyNetworkRequest.getInstance(context).addToRequestQueue(editBSAttachmentRequest);
    }


}
