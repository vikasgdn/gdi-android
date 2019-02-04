package com.gdi.activity;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gdi.R;
import com.gdi.utils.AppPrefs;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class PlayAudioActivity extends BaseActivity {

    private ImageButton playBtn;
    private MediaPlayer mediaPlayer;
    Toolbar toolbar;
    private double startTime = 0.0;
    private double finalTime = 0.0;

    private Handler myHandler = new Handler();;
    private SeekBar seekbar;
    private TextView startTimeTxt;

    private Uri audioUri;
    private Context context;
    Map<String, String> headers = new HashMap<String, String>();
    public static int oneTimeOnly = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_audio);
        context = this;
        String audioUrl = getIntent().getStringExtra("audioUrl");
        audioUri = Uri.parse(audioUrl);
        headers.put("access-token", AppPrefs.getAccessToken(context));
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setActionBar();
        playBtn = (ImageButton) findViewById(R.id.play_btn);
        startTimeTxt = (TextView)findViewById(R.id.textView2);
        mediaPlayer = new MediaPlayer();
        seekbar = (SeekBar)findViewById(R.id.seekBar);
        seekbar.setClickable(false);
        playBtn.setEnabled(true);

        seekbar.setProgress((int)startTime);
        myHandler.postDelayed(UpdateSongTime,100);

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Playing audio",Toast.LENGTH_SHORT).show();
                mediaPlayer.reset();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                try {
                    mediaPlayer.setDataSource(context, audioUri, headers);
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mediaPlayer) {
                            mediaPlayer.start();
                        }
                    });
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                finalTime = mediaPlayer.getDuration();
                startTime = mediaPlayer.getCurrentPosition();

                if (oneTimeOnly == 0) {
                    seekbar.setMax((int) finalTime);
                    //oneTimeOnly = 1;
                }

                startTimeTxt.setText(String.format("%d.%d", TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                        finalTime)))
                );

                seekbar.setProgress((int)finalTime);
                myHandler.postDelayed(UpdateSongTime,100);
                playBtn.setEnabled(false);
            }
        });

    }

    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
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
