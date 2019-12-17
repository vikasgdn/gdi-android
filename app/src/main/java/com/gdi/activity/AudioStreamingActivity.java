package com.gdi.activity;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gdi.R;
import com.gdi.utils.AppUtils;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class AudioStreamingActivity extends BaseActivity {

    private int forwardTime = 5000;
    private int backwardTime = 5000;

    private ImageButton playBtn;
    private ImageButton pauseBtn;
    private MediaPlayer mediaPlayer;
    Toolbar toolbar;
    private double startTime = 0.0;
    private double finalTime = 0.0;

    private Handler myHandler = new Handler();
    private SeekBar seekbar;
    private TextView startTimeTxt;
    private TextView TimeTxt;

    private Uri audioUri;
    private Context context;
    public static int oneTimeOnly = 0;
    //public int oneTimeOnly = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stream_audio);
        context = this;
        String audio = getIntent().getStringExtra("audioFile");
        File audioFile = new File(audio);
        Uri audioUri = Uri.fromFile(audioFile);
        toolbar = findViewById(R.id.toolbar);
        setActionBar();
        playBtn = findViewById(R.id.play_btn);
        pauseBtn = findViewById(R.id.pause_btn);
        startTimeTxt = findViewById(R.id.textView2);
        mediaPlayer = MediaPlayer.create(context, audioUri);


        seekbar = findViewById(R.id.seekBar);
        seekbar.setClickable(false);
        //pauseBtn.setEnabled(false);

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Playing sound",Toast.LENGTH_SHORT).show();
                try {
                    mediaPlayer.start();
                    finalTime = mediaPlayer.getDuration();
                    startTime = mediaPlayer.getCurrentPosition();
                }catch (Exception e){
                    e.printStackTrace();
                    AppUtils.toast(AudioStreamingActivity.this, "Can't play this file");
                }



                if (oneTimeOnly == 0) {
                    seekbar.setMax((int) finalTime);
                    //oneTimeOnly = 1;
                }

                startTimeTxt.setText(String.format("%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                        startTime)))
                );

                seekbar.setProgress((int)startTime);
                myHandler.postDelayed(UpdateSongTime,100);
                pauseBtn.setVisibility(View.VISIBLE);
                playBtn.setVisibility(View.GONE);
            }
        });

        pauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Pausing sound",Toast.LENGTH_SHORT).show();
                try {
                    mediaPlayer.pause();
                }catch (Exception e){
                    e.printStackTrace();
                    AppUtils.toast(AudioStreamingActivity.this, "Can't play this file");
                }
                pauseBtn.setVisibility(View.GONE);
                playBtn.setVisibility(View.VISIBLE);
                //pauseBtn.setEnabled(false);
                //playBtn.setEnabled(true);
            }
        });

    }

    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            try {
                startTime = mediaPlayer.getCurrentPosition();
            }catch (Exception e){
                e.printStackTrace();
                AppUtils.toast(AudioStreamingActivity.this, "Can't play this file");
            }
            startTimeTxt.setText(String.format("%d.%d",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                    toMinutes((long) startTime)))
            );
            seekbar.setProgress((int)startTime);
            myHandler.postDelayed(this, 100);
        }
    };

    private void setActionBar() {
        initToolbar(toolbar);
        setTitle("");
        enableBack(true);
        enableBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (myHandler!=null) {
            myHandler.removeCallbacks(UpdateSongTime);
        }
        mediaPlayer.pause();
        mediaPlayer.stop();
        mediaPlayer.release();
    }
}
