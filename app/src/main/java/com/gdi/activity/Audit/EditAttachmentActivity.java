package com.gdi.activity.Audit;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.gdi.activity.BaseActivity;
import com.gdi.activity.EditImageActivity;
import com.gdi.api.ApiEndPoints;
import com.gdi.api.DSSaveSubmitJsonRequest;
import com.gdi.api.EditBSAttachmentRequest;
import com.gdi.api.EditBSQuestionAttachmentRequest;
import com.gdi.api.EditDSAttachmentRequest;
import com.gdi.api.EditESAttachmentRequest;
import com.gdi.api.VolleyNetworkRequest;
import com.gdi.hotel.mystery.audits.R;
import com.gdi.model.audit.AddAttachment.AddAttachmentInfo;
import com.gdi.utils.ApiResponseKeys;
import com.gdi.utils.AppConstant;
import com.gdi.utils.AppLogger;
import com.gdi.utils.AppPrefs;
import com.gdi.utils.AppUtils;
import com.gdi.utils.Headers;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditAttachmentActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_attachment_image)
    ImageView attachmentImage;
    @BindView(R.id.edit_image)
    ImageButton editImage;


    @BindView(R.id.tv_attachment_name)
    TextView attachmentName;
    @BindView(R.id.cb_add_attachment_critical)
    CheckBox criticalCheckBox;
    @BindView(R.id.tv_critical_decription_btn)
    TextView descriptionBtn;
    @BindView(R.id.tv_attachment_description)
    EditText attachmentDescription;
    @BindView(R.id.tv_edit_btn)
    TextView editBtn;
    @BindView(R.id.attachment_save_btn)
    Button saveBtn;
    @BindView(R.id.attachment_name)
    TextView attachment_name;
    @BindView(R.id.attachment_name_layout)
    LinearLayout attachmentNameLayout;
    String attachType = "";
    String auditId = "";
    String sectionGroupId = "";
    String sectionId = "";
    String questionId = "";
    int isCritical = 0;
    private String editable = "";
    Context context;
    private ProgressDialog progressDialog;
    private AddAttachmentInfo addAttachmentInfo;
    private static final String TAG = EditAttachmentActivity.class.getSimpleName();


    public static  Drawable sDrawable=null;
    private String mFromWhere="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_attachment);
        ButterKnife.bind(this);
        context = this;
        initView();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        editBtn = findViewById(R.id.tv_edit_btn);
        attachmentName = findViewById(R.id.tv_attachment_name);
        criticalCheckBox = findViewById(R.id.cb_add_attachment_critical);
        descriptionBtn = findViewById(R.id.tv_critical_decription_btn);
        attachmentDescription = findViewById(R.id.tv_attachment_description);
        attachmentImage = findViewById(R.id.iv_attachment_image);

        editImage = findViewById(R.id.edit_image);


        saveBtn = findViewById(R.id.attachment_save_btn);
        attachment_name = findViewById(R.id.attachment_name);
        attachmentNameLayout = findViewById(R.id.attachment_name_layout);

        setActionBar();

        mFromWhere=getIntent().getStringExtra("LOCATION");
        editable = getIntent().getStringExtra("editable");
        auditId = getIntent().getStringExtra("auditId");
        sectionGroupId = getIntent().getStringExtra("sectionGroupId");
        sectionId = getIntent().getStringExtra("sectionId");
        questionId = getIntent().getStringExtra("questionId");
        attachType = getIntent().getStringExtra("attachType");
        addAttachmentInfo = getIntent().getParcelableExtra("attachmentDetail");
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading...");
        if (editable.equals("0")){
            editBtn.setVisibility(View.VISIBLE);
        }else {
            editBtn.setVisibility(View.GONE);
        }
        setData();
        editBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
        descriptionBtn.setOnClickListener(this);
        criticalCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    isCritical = 1;
                } else {
                    isCritical = 0;
                }
            }
        });


        editImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_edit_btn:
                attachmentDescription.setEnabled(true);
                criticalCheckBox.setEnabled(true);
                saveBtn.setVisibility(View.VISIBLE);
                editBtn.setVisibility(View.GONE);
                break;

            case R.id.edit_image:
                Intent intent = new Intent(context, EditImageActivity.class);
                intent.putExtra("location","EDIT");
                startActivityForResult(intent,123);
                break;

            case R.id.attachment_save_btn:
                switch (attachType) {
                    case "bsSection":
                        addBsFileAttachment();
                        break;
                    case "bsQuestion":
                        addQuestionFileAttachment();
                        break;
                    case "dsSection":
                        addDsFileAttachment();
                        break;
                    case "esSection":
                        addEsFileAttachment();
                        break;
                }
                break;
            case R.id.tv_critical_decription_btn:
                notificationDialog();
                break;
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 123 && resultCode == RESULT_OK)
        {
            Uri uri = Uri.fromFile(new File(data.getStringExtra("path")));
            attachmentImage.setImageURI(uri);
        }
    }

    private void setData() {
        if (!AppUtils.isStringEmpty(addAttachmentInfo.getFile_url())) {
            progressDialog.show();
            Glide.with(context)
                    .load(Headers.getUrlWithHeaders(addAttachmentInfo.getFile_url(),
                            AppPrefs.getAccessToken(context)))
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            progressDialog.dismiss();
                            AppUtils.toast(EditAttachmentActivity.this, "Image not available");
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            progressDialog.dismiss();
                            sDrawable=resource;
                            return false;
                        }
                    })
                    .into(attachmentImage);
        }
        if (!AppUtils.isStringEmpty(addAttachmentInfo.getClient_file_name())) {
            attachmentName.setText(addAttachmentInfo.getClient_file_name());
        }
        if (!AppUtils.isStringEmpty(addAttachmentInfo.getClient_file_name())) {
            attachment_name.setText(addAttachmentInfo.getClient_file_name());
        }
        if (!AppUtils.isStringEmpty(addAttachmentInfo.getDescription())) {
            attachmentDescription.setText(addAttachmentInfo.getDescription());
        }
        if (addAttachmentInfo.getIs_critical() == 1) {
            criticalCheckBox.setChecked(true);
        }
        switch (attachType) {
            case "bsSection":
                attachmentNameLayout.setVisibility(View.VISIBLE);
                attachment_name.setVisibility(View.GONE);
                break;
            case "bsQuestion":
                attachmentNameLayout.setVisibility(View.VISIBLE);
                attachment_name.setVisibility(View.GONE);
                break;
            case "dsSection":
                attachmentNameLayout.setVisibility(View.GONE);
                attachment_name.setVisibility(View.VISIBLE);
                break;
            case "esSection":
                attachmentNameLayout.setVisibility(View.GONE);
                attachment_name.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void setActionBar() {
        initToolbar(toolbar);
        setTitle("Attachments");
        enableBack(true);
        enableBackPressed();
    }

    private void notificationDialog() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(context);

        dialog.setTitle("GDI");
        dialog.setMessage("Please only mark this file as critical if you want to highlight this file out of all the file");

        dialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.create().show();
    }

    private void addBsFileAttachment() {
        showProgressDialog();
        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AppLogger.e(TAG, "AddAttachmentResponse: " + response);
                try {
                    JSONObject object = new JSONObject(response);

                    if (!object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        /*BrandStandardRootObject brandStandardRootObject = new GsonBuilder().create()
                                .fromJson(object.toString(), BrandStandardRootObject.class);
                        if (brandStandardRootObject.getData() != null &&
                                brandStandardRootObject.getData().toString().length() > 0) {
                            setQuestionList(brandStandardRootObject.getData());
                            //brandStandardAuditAdapter.notifyDataSetChanged();
                        }*/
                        AppUtils.toast((BaseActivity) context,
                                object.getString(ApiResponseKeys.RES_KEY_MESSAGE));
                        attachmentDescription.setEnabled(false);
                        criticalCheckBox.setEnabled(false);
                        saveBtn.setVisibility(View.GONE);
                        editBtn.setVisibility(View.VISIBLE);
                    } else if (object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        AppUtils.toast((BaseActivity) context,
                                object.getString(ApiResponseKeys.RES_KEY_MESSAGE));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                hideProgressDialog();
            }

        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
                AppLogger.e(TAG, "AddAttachmentError: " + error.getMessage());
                AppUtils.toast((BaseActivity) context, "Server temporary unavailable, Please try again");

            }
        };

        String url = ApiEndPoints.BSEDITATTACHMENT;

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseAuth.getInstance().getCurrentUser().getIdToken(true)
                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            if (task.isSuccessful()) {
                                EditBSAttachmentRequest editBSAttachmentRequest = new EditBSAttachmentRequest(
                                        AppPrefs.getAccessToken(context),task.getResult().getToken(), url, addAttachmentInfo.getClient_file_name(), auditId,
                                        addAttachmentInfo.getAudit_section_file_id(), addAttachmentInfo.getDescription(), isCritical, stringListener, errorListener);
                                VolleyNetworkRequest.getInstance(context).addToRequestQueue(editBSAttachmentRequest);

                            }
                        }
                    });
        }

    }

    private void addQuestionFileAttachment() {
        showProgressDialog();
        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AppLogger.e(TAG, "AddAttachmentResponse: " + response);
                try {
                    JSONObject object = new JSONObject(response);

                    if (!object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        /*BrandStandardRootObject brandStandardRootObject = new GsonBuilder().create()
                                .fromJson(object.toString(), BrandStandardRootObject.class);
                        if (brandStandardRootObject.getData() != null &&
                                brandStandardRootObject.getData().toString().length() > 0) {
                            setQuestionList(brandStandardRootObject.getData());
                            //brandStandardAuditAdapter.notifyDataSetChanged();
                        }*/
                        AppUtils.toast((BaseActivity) context,
                                object.getString(ApiResponseKeys.RES_KEY_MESSAGE));
                        attachmentDescription.setEnabled(false);
                        criticalCheckBox.setEnabled(false);
                        saveBtn.setVisibility(View.GONE);
                        editBtn.setVisibility(View.VISIBLE);
                    } else if (object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        AppUtils.toast((BaseActivity) context,
                                object.getString(ApiResponseKeys.RES_KEY_MESSAGE));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                hideProgressDialog();
            }

        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
                AppLogger.e(TAG, "AddAttachmentError: " + error.getMessage());
                AppUtils.toast((BaseActivity) context, "Server temporary unavailable, Please try again");

            }
        };

        String url = ApiEndPoints.BSEDITATTACHMENT;

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseAuth.getInstance().getCurrentUser().getIdToken(true)
                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            if (task.isSuccessful()) {
                                EditBSQuestionAttachmentRequest editBSAttachmentRequest = new EditBSQuestionAttachmentRequest(
                                        AppPrefs.getAccessToken(context),task.getResult().getToken(), url, addAttachmentInfo.getClient_file_name(), auditId,
                                        addAttachmentInfo.getAudit_section_file_id(), addAttachmentInfo.getAudit_question_file_id(), addAttachmentInfo.getDescription(), isCritical, stringListener, errorListener);
                                VolleyNetworkRequest.getInstance(context).addToRequestQueue(editBSAttachmentRequest);

                            }
                        }
                    });
        }

    }

    private void addDsFileAttachment() {
        showProgressDialog();
        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AppLogger.e(TAG, "AddAttachmentResponse: " + response);
                try {
                    JSONObject object = new JSONObject(response);

                    if (!object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        /*BrandStandardRootObject brandStandardRootObject = new GsonBuilder().create()
                                .fromJson(object.toString(), BrandStandardRootObject.class);
                        if (brandStandardRootObject.getData() != null &&
                                brandStandardRootObject.getData().toString().length() > 0) {
                            setQuestionList(brandStandardRootObject.getData());
                            //brandStandardAuditAdapter.notifyDataSetChanged();
                        }*/
                        AppUtils.toast((BaseActivity) context,
                                object.getString(ApiResponseKeys.RES_KEY_MESSAGE));
                        attachmentDescription.setEnabled(false);
                        criticalCheckBox.setEnabled(false);
                        saveBtn.setVisibility(View.GONE);
                        editBtn.setVisibility(View.VISIBLE);
                    } else if (object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        AppUtils.toast((BaseActivity) context,
                                object.getString(ApiResponseKeys.RES_KEY_MESSAGE));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                hideProgressDialog();
            }

        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
                AppLogger.e(TAG, "AddAttachmentError: " + error.getMessage());
                AppUtils.toast((BaseActivity) context, "Server temporary unavailable, Please try again");

            }
        };

        String url = ApiEndPoints.DSEDITATTACHMENT;

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseAuth.getInstance().getCurrentUser().getIdToken(true)
                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            if (task.isSuccessful()) {
                                EditDSAttachmentRequest addBSAttachmentRequest = new EditDSAttachmentRequest(
                                        AppPrefs.getAccessToken(context),task.getResult().getToken(),url, addAttachmentInfo.getClient_file_name(), auditId,
                                        sectionGroupId, sectionId, addAttachmentInfo.getDescription(), stringListener, errorListener);
                                VolleyNetworkRequest.getInstance(context).addToRequestQueue(addBSAttachmentRequest);

                            }
                        }
                    });
        }

    }

    private void addEsFileAttachment() {
        showProgressDialog();
        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AppLogger.e(TAG, "AddAttachmentResponse: " + response);
                try {
                    JSONObject object = new JSONObject(response);

                    if (!object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        /*BrandStandardRootObject brandStandardRootObject = new GsonBuilder().create()
                                .fromJson(object.toString(), BrandStandardRootObject.class);
                        if (brandStandardRootObject.getData() != null &&
                                brandStandardRootObject.getData().toString().length() > 0) {
                            setQuestionList(brandStandardRootObject.getData());
                            //brandStandardAuditAdapter.notifyDataSetChanged();
                        }*/
                        AppUtils.toast((BaseActivity) context,
                                object.getString(ApiResponseKeys.RES_KEY_MESSAGE));
                        attachmentDescription.setEnabled(false);
                        criticalCheckBox.setEnabled(false);
                        saveBtn.setVisibility(View.GONE);
                        editBtn.setVisibility(View.VISIBLE);
                    } else if (object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        AppUtils.toast((BaseActivity) context,
                                object.getString(ApiResponseKeys.RES_KEY_MESSAGE));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                hideProgressDialog();
            }

        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
                AppLogger.e(TAG, "AddAttachmentError: " + error.getMessage());
                AppUtils.toast((BaseActivity) context, "Server temporary unavailable, Please try again");

            }
        };

        String url = ApiEndPoints.ESEDITATTACHMENT;
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseAuth.getInstance().getCurrentUser().getIdToken(true)
                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            if (task.isSuccessful()) {
                                EditESAttachmentRequest addBSAttachmentRequest = new EditESAttachmentRequest(
                                        AppPrefs.getAccessToken(context),task.getResult().getToken(), url, addAttachmentInfo.getClient_file_name(), auditId, addAttachmentInfo.getDescription(), stringListener, errorListener);
                                VolleyNetworkRequest.getInstance(context).addToRequestQueue(addBSAttachmentRequest);

                            }
                        }
                    });
        }

    }

}
