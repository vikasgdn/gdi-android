package com.gdi.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gdi.R;
import com.gdi.utils.AppPrefs;
import com.gdi.utils.Headers;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageViewActivity extends BaseActivity {

    @BindView(R.id.image_view)
    ImageView imageView;
    private Context context;
    String image;
    public static final String TAG = ImageViewActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_image_view);
        context = this;
        ButterKnife.bind(ImageViewActivity.this);
        initView();
    }

    private void initView() {
        //setActionBar();
        imageView = (ImageView)findViewById(R.id.image_view);
        image = getIntent().getStringExtra("fileUrl");
        Glide.with(context)
                .load(Headers.getUrlWithHeaders(image, AppPrefs.getAccessToken(context)))
                .into(imageView);
    }

    private void setActionBar() {
        enableBack(true);
        enableBackPressed();
    }
}
