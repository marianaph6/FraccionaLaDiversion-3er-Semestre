package com.example.fraccionaladiversion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class Intro_Screen extends AppCompatActivity {

    private MediaPlayer bgMusic;
    private MediaPlayer soundEstudiante;
    private MediaPlayer soundProfesor;
    private ImageView imgSoyEstudiante, imgSoyProfesor;
    private TextView txtSoyEstudiante, txtSoyProfesor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro__screen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        play();
        connect();
        soundPass();
        imgSoyEstudiante.setEnabled(true);
        imgSoyProfesor.setEnabled(true);
    }
    public void play()
    {
        if(bgMusic == null)
        {
            bgMusic = MediaPlayer.create(this, R.raw.muskids);
            bgMusic.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    bgMusic.release();
                    bgMusic = null;
                    play();
                }
            });
        }
        bgMusic.start();
    }
    private void connect()
    {
        imgSoyEstudiante = findViewById(R.id.btnSoyEstudiante);
        imgSoyProfesor = findViewById(R.id.btnSoyProfesor);
        txtSoyEstudiante = findViewById(R.id.txtSoyEstudiante);
        txtSoyProfesor = findViewById(R.id.txtSoyProfesor);
    }
    private void soundPass()
    {
        imgSoyProfesor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(soundProfesor == null)
                {
                    soundProfesor = MediaPlayer.create(Intro_Screen.this, R.raw.morningstudents);
                    soundProfesor.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            soundProfesor.stop();
                            soundProfesor.release();
                            soundProfesor = null;
                            Intent launchMain = new Intent(Intro_Screen.this, MainActivity.class);
                            launchMain.putExtra("Tipo", "Profesor");
                            startActivity(launchMain);
                            Intro_Screen.this.finish();
                        }
                    });
                }
                bgMusic.stop();
                bgMusic.release();
                bgMusic = null;
                imgSoyProfesor.setEnabled(false);
                soundProfesor.start();
            }
        });
        imgSoyEstudiante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(soundEstudiante == null)
                {
                    soundEstudiante = MediaPlayer.create(Intro_Screen.this, R.raw.yay);
                    soundEstudiante.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            soundEstudiante.stop();
                            soundEstudiante.release();
                            soundEstudiante= null;
                            Intent launchMain = new Intent(Intro_Screen.this, MainActivity.class);
                            launchMain.putExtra("Tipo", "Estudiante");
                            startActivity(launchMain);
                            Intro_Screen.this.finish();
                        }
                    });
                }
                bgMusic.stop();
                bgMusic.release();
                bgMusic = null;
                imgSoyEstudiante.setEnabled(false);
                soundEstudiante.start();
            }
        });
    }
}
