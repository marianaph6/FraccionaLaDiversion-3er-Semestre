package com.example.fraccionaladiversion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.ArrayList;

public class ActivityLeccion extends AppCompatActivity {

    ArrayAdapter adapter;
    VideoView videoView;
    MediaController mediaController;
    String tema;
    Button btnJugar;
    String Id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leccion);
        videoView = findViewById(R.id.video_view);
        btnJugar=findViewById(R.id.btnJugar);
        Bundle datos = this.getIntent().getExtras();
        tema=datos.getString("Tema");
        Id = getIntent().getStringExtra("IdEstudiante");

        switch (tema){
            case "Simplificar":
                if(mediaController==null){
                    mediaController=new MediaController(ActivityLeccion.this);
                    mediaController.setAnchorView(videoView);
                }
                videoView.setMediaController(mediaController);
                videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.simplificar));
                videoView.start();
                videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        btnJugar.setVisibility(View.VISIBLE);
                        btnJugar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent I = new Intent(getApplicationContext(), JuegoSimplificarActivity.class);
                                I.putExtra("Puntaje",0);
                                I.putExtra("Contador",0);
                                I.putExtra("IdEstudiante",Id);
                                startActivity(I);
                                finish();
                            }
                        });
                    }
                });
                break;
            case "Operaciones":
                if(mediaController==null){
                    mediaController=new MediaController(ActivityLeccion.this);
                    mediaController.setAnchorView(videoView);
                }
                videoView.setMediaController(mediaController);
                videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.suma));
                videoView.start();
                videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        btnJugar.setVisibility(View.VISIBLE);
                        btnJugar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent I = new Intent(getApplicationContext(), JuegoOperacionesActivity.class);
                                I.putExtra("Puntaje",0);
                                I.putExtra("Contador",0);
                                I.putExtra("IdEstudiante",Id);
                                startActivity(I);
                                finish();
                            }
                        });
                    }
                });
                break;
            case "Graficar":
                if(mediaController==null){
                    mediaController=new MediaController(ActivityLeccion.this);
                    mediaController.setAnchorView(videoView);
                }
                videoView.setMediaController(mediaController);
                videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.introduccion));
                videoView.start();
                videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        btnJugar.setVisibility(View.VISIBLE);
                        btnJugar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent I = new Intent(getApplicationContext(), Juego_Graficas.class);
                                I.putExtra("Puntaje",0);
                                I.putExtra("Contador",0);
                                I.putExtra("IdEstudiante",Id);
                                startActivity(I);
                                finish();
                            }
                        });
                    }
                });
                break;
        }


    }


}
