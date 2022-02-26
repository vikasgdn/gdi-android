package com.gdi.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.gdi.R;
import com.gdi.activity.oditylychange.BaseActivityOditly;
import com.gdi.utils.AppConstant;
import com.gdi.utils.CustomVideoPlayer;


public class ExoVideoPlayer extends BaseActivityOditly implements CustomVideoPlayer.PlaybackListener
{

    private String url;
    private CustomVideoPlayer customVideoPlayer;
    public static void start(Context context, String data) {
        Intent i = new Intent(context, ExoVideoPlayer.class);
        i.putExtra(AppConstant.FILE_URL, data);
        context.startActivity(i);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  hideSystemUI();
        //  setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_exoplayer);

        initView();
    }

    @Override
    protected void initView() {
        super.initView();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            url = getIntent().getExtras().getString(AppConstant.FILE_URL);
        }
        findViewById(R.id.iv_header_left).setOnClickListener(this);
        customVideoPlayer = findViewById(R.id.customVideoPlayer);
        customVideoPlayer.setMediaUrl(url);
        customVideoPlayer.enableAutoMute(false);
        customVideoPlayer.enableAutoPlay(true);    // u can set auto play true accoding need
        customVideoPlayer.setMinHeight(300);
        customVideoPlayer.hideControllers(false);
        customVideoPlayer.setOnPlaybackListener(this);
        //customVideoPlayer.setOrientation(LinearLayout.VERTICAL);
        customVideoPlayer.build();


    }
    @Override
    public void onClick(View view) {
        super.onClick(view);
        finish();
    }

    @Override
    public void onVolumeChange(boolean volumeOn)
    {


    }

    @Override
    public void onPlayEvent() {

    }

    @Override
    public void onPauseEvent() {

    }

    @Override
    public void onCompletedEvent() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(customVideoPlayer!=null)
            customVideoPlayer.play();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(customVideoPlayer!=null && customVideoPlayer.isPlaying())
            customVideoPlayer.pause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(customVideoPlayer!=null && customVideoPlayer.isPlaying())
            customVideoPlayer.stop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(customVideoPlayer!=null && customVideoPlayer.isPlaying())
            customVideoPlayer.stop();
    }

}
