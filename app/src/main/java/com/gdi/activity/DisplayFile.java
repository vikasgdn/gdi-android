package com.gdi.activity;

import android.content.Context;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.gdi.R;
import com.gdi.utils.AppLogger;
import com.gdi.utils.AppPrefs;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DisplayFile extends BaseActivity {

    @BindView(R.id.display_file_web_view)
    WebView displayFile;
    String url = "";
    Context context;
    private static final String TAG = DisplayFile.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_file);
        context = this;
        ButterKnife.bind(DisplayFile.this);
        initView();
        //webView.loadUrl("https://docs.google.com/viewer?url=" + "url of pdf file");
    }

    private void initView() {
        displayFile = (WebView)findViewById(R.id.display_file_web_view);
        url = getIntent().getStringExtra("fileUrl");
        AppLogger.e(TAG, "PdfFileinDisplay: " + url);
        displayFile.getSettings().setJavaScriptEnabled(true);
        //displayFile.getSettings().setPluginState(WebSettings.PluginState.ON);
        /*HashMap<String, String> headers = new HashMap<>();
        headers.put("access-token", AppPrefs.getAccessToken(context));
        AppLogger.e(TAG, "HeaderDisplay: " + headers);*/
        displayFile.loadUrl(url);
    }
}
