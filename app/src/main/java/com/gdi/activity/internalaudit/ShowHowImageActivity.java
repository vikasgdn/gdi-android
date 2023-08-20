package com.gdi.activity.internalaudit;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.gdi.activity.BaseActivity;
import com.gdi.apppreferences.AppPreferences;
import com.gdi.hotel.mystery.audits.R;
import com.gdi.utils.AppConstant;
import com.gdi.utils.AppUtils;
import com.gdi.utils.Headers;


import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowHowImageActivity extends BaseActivity {

    @BindView(R.id.image_view)
    ImageView imageView;
    @BindView(R.id.ll_parent_progress)
    RelativeLayout mProgressBarRL;
    private Context context;
    String image;
    TextView mTitalTV;
   // private ProgressDialog progressDialog;
    public static final String TAG = ShowHowImageActivity.class.getSimpleName();
    private String mFromWhere="";


    public static void start(Context context, String data,String location) {
        Intent i = new Intent(context, ShowHowImageActivity.class);
        i.putExtra(AppConstant.FILE_URL, data);
        i.putExtra(AppConstant.FROMWHERE, location);
        context.startActivity(i);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_refrence);
        context = this;
        ButterKnife.bind(ShowHowImageActivity.this);
         initView();
    }


     void initView() {
        mTitalTV = findViewById(R.id.tv_header_title);
        mProgressBarRL = findViewById(R.id.ll_parent_progress);
        findViewById(R.id.iv_header_left).setOnClickListener(this);


        imageView = findViewById(R.id.image_view);
        image = getIntent().getStringExtra(AppConstant.FILE_URL);
        mFromWhere = getIntent().getStringExtra(AppConstant.FROMWHERE);


        if (mFromWhere.equalsIgnoreCase(AppConstant.ACTIONPLAN))
            mTitalTV.setText(getString(R.string.text_attachment));
        else
            mTitalTV.setText(getString(R.string.text_ref_image));


        mProgressBarRL.setVisibility(View.VISIBLE);
      //  progressDialog.show();
        Glide.with(context)
                .load(Headers.getUrlWithHeaders(image, AppPreferences.INSTANCE.getAccessToken(this)))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                       // progressDialog.dismiss();
                        mProgressBarRL.setVisibility(View.GONE);
                        AppUtils.toast(ShowHowImageActivity.this, "Can't open this file");
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        //progressDialog.dismiss();
                        mProgressBarRL.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(imageView);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        finish();
    }
}
