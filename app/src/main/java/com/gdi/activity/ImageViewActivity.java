package com.gdi.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.gdi.hotel.mystery.audits.R;
import com.gdi.utils.AppPrefs;
import com.gdi.utils.AppUtils;
import com.gdi.utils.Headers;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageViewActivity extends BaseActivity {

    @BindView(R.id.image_view)
    ImageView imageView;
    private Context context;
    String image;
    Toolbar toolbar;
    private ProgressDialog progressDialog;
    public static final String TAG = ImageViewActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);
        context = this;
        ButterKnife.bind(ImageViewActivity.this);
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading...");
        initView();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        setActionBar();
        imageView = findViewById(R.id.image_view);
        image = getIntent().getStringExtra("fileUrl");
        progressDialog.show();
        Glide.with(context)
                .load(Headers.getUrlWithHeaders(image, AppPrefs.getAccessToken(context)))
                .listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                progressDialog.dismiss();
                AppUtils.toast(ImageViewActivity.this, "Can't open this file");
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                progressDialog.dismiss();
                return false;
            }
        })
                .into(imageView);
    }

    private void setActionBar() {
        initToolbar(toolbar);
        setTitle("");
        enableBack(true);
        enableBackPressed();
    }
}
