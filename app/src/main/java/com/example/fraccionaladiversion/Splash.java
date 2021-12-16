package com.example.fraccionaladiversion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;

public class Splash extends AppCompatActivity {

    private ImageView imgKid, imgOps, imgLogo;
    private TextView appName;
    CountDownTimer myTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        connect();
        animations();
    }
    private void connect()
    {
        imgKid = findViewById(R.id.splashKid);
        imgOps = findViewById(R.id.splashOps);
        imgLogo = findViewById(R.id.splashImg);
        appName = findViewById(R.id.splashtxt);
    }
    private void animations()
    {
        imgLogo.animate().setDuration(1600).alpha(1);
        appName.animate().setDuration(1600).alpha(1);
        myTimer = new CountDownTimer(1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                imgKid.animate().setDuration(1000).alpha(1);
                imgOps.animate().setDuration(1000).alpha(1);
                myTimer = new CountDownTimer(1700,1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                    }

                    @Override
                    public void onFinish() {
                        Intent intent = new Intent(Splash.this, Intro_Screen.class);
                        startActivity(intent);
                        Splash.this.finish();
                    }
                }.start();
            }
        }.start();
    }
}
