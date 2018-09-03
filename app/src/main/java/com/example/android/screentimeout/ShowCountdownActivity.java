package com.example.android.screentimeout;


import android.app.admin.DevicePolicyManager;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.concurrent.TimeUnit;

import static android.R.attr.min;


/**
 * Shos the user the time remaining using progressbar and initiates phone screen lock when the timer expires
 */

public class ShowCountdownActivity extends AppCompatActivity {

    TextView ctDwn_timer ;
    private CountDownTimer timer_value ;
    private ProgressBar progressBarValue ;
    private long timeInMilliSeconds ;
    DevicePolicyManager deviceManger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_countdown);
        initViews();
    }

    /*
         Initialize the timer
     */
    private void initViews(){

        ctDwn_timer = (TextView) findViewById(R.id.countdown_timer) ;
        progressBarValue = (ProgressBar) findViewById(R.id.progressBarTimer) ;
        deviceManger = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);

        Intent i = getIntent() ;
        timeInMilliSeconds = i.getIntExtra("HOUR",0) * 60;
        timeInMilliSeconds += i.getIntExtra("MIN",0);
        timeInMilliSeconds  = timeInMilliSeconds * 60 * 1000 ;
        ctDwn_timer.setText(i.getIntExtra("HOUR",0)+":"+i.getIntExtra("MIN",0)+":"+"00");
        setProgressBarValue();
        startTimer();


    }

    private void startTimer(){

        timer_value = new CountDownTimer(timeInMilliSeconds,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                ctDwn_timer.setText(timeFormatter(millisUntilFinished));
                progressBarValue.setProgress((int) (millisUntilFinished /1000));

            }

            @Override
            public void onFinish() {

                // To initiate screen lock when the timer expires

                deviceManger.lockNow() ;


            }
        }.start();


    }

    /*
       To set the ProgressBar Value
     */
    private void setProgressBarValue(){

        progressBarValue.setMax( (int) timeInMilliSeconds / 1000   );
        progressBarValue.setProgress( (int) timeInMilliSeconds / 1000  );

    }



    private String timeFormatter(long timeTobeFormatted){

          long hoursRemaining = TimeUnit.MILLISECONDS.toHours(timeTobeFormatted);
          long minutesRemaining = TimeUnit.MILLISECONDS.toMinutes(timeTobeFormatted) -  TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeTobeFormatted));
          long secRemaining = TimeUnit.MILLISECONDS.toSeconds(timeTobeFormatted) -  TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeTobeFormatted));
          String op = String.format("%02d:%02d:%02d",hoursRemaining,minutesRemaining,secRemaining ) ;
          return op;



    }





}
