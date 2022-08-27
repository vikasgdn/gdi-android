package com.gdi.activity.oditylychange;

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
import com.gdi.hotel.mystery.audits.R;
import com.gdi.utils.AppConstant;
import com.gdi.utils.AppPrefs;
import com.gdi.utils.AppUtils;
import com.gdi.utils.Headers;


import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowHowImageActivity extends BaseActivityOditly {

    @BindView(R.id.image_view)
    ImageView imageView;
    private Context context;
    String image;
    TextView mTitalTV;
   // private ProgressDialog progressDialog;
    public static final String TAG = ShowHowImageActivity.class.getSimpleName();


    public static void start(Context context, String data) {
        Intent i = new Intent(context, ShowHowImageActivity.class);
        i.putExtra(AppConstant.FILE_URL, data);
        context.startActivity(i);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_refrence);
        context = this;
        ButterKnife.bind(ShowHowImageActivity.this);
       // progressDialog = new ProgressDialog(context);
       // progressDialog.setMessage("Loading...");
        initView();
    }

    @Override
    protected void initView() {
        super.initView();
        mTitalTV = findViewById(R.id.tv_header_title);
        findViewById(R.id.iv_header_left).setOnClickListener(this);
        mTitalTV.setText(getString(R.string.text_ref_image));

        imageView = findViewById(R.id.image_view);
        image = getIntent().getStringExtra(AppConstant.FILE_URL);
        showAppProgressDialog();
      //  progressDialog.show();
        Glide.with(context)
                .load(Headers.getUrlWithHeaders(image, AppPrefs.getAccessToken(context)))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                       // progressDialog.dismiss();
                        hideProgressDialog();
                        AppUtils.toast(ShowHowImageActivity.this, "Can't open this file");
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        //progressDialog.dismiss();
                        hideProgressDialog();
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
