package com.gdi.activity;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gdi.R;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class PlayAudioActivity extends AppCompatActivity {

    private ImageButton playBtn;
    private ImageButton pauseBtn;
    private MediaPlayer mediaPlayer;

    private double startTime = 0.0;
    private double finalTime = 0.0;

    private Handler myHandler = new Handler();;
    private SeekBar seekbar;
    private TextView startTimeTxt;

    private String filePath = "";
    private Uri audioUri;
    private Context context;
    public static int oneTimeOnly = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_audio);
        context = this;
        filePath = getIntent().getStringExtra("filePath");
        File audioFile = new File(filePath);
        audioUri = Uri.fromFile(audioFile);

        playBtn = (ImageButton) findViewById(R.id.button2);
        pauseBtn = (ImageButton)findViewById(R.id.button3);

        startTimeTxt = (TextView)findViewById(R.id.textView2);

        mediaPlayer = MediaPlayer.create(context, audioUri);
        seekbar = (SeekBar)findViewById(R.id.seekBar);
        seekbar.setClickable(false);
        pauseBtn.setEnabled(false);

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Playing audio",Toast.LENGTH_SHORT).show();
                        mediaPlayer.start();

                finalTime = mediaPlayer.getDuration();
                startTime = mediaPlayer.getCurrentPosition();

                if (oneTimeOnly == 0) {
                    seekbar.setMax((int) finalTime);
                    oneTimeOnly = 1;
                }

                startTimeTxt.setText(String.format("%d.%d",
                        TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                        finalTime)))
                );

                seekbar.setProgress((int)startTime);
                myHandler.postDelayed(UpdateSongTime,100);
                pauseBtn.setEnabled(true);
                playBtn.setEnabled(false);
            }
        });

        pauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Pausing audio",Toast.LENGTH_SHORT).show();
                        mediaPlayer.pause();
                pauseBtn.setEnabled(false);
                playBtn.setEnabled(true);
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
}
