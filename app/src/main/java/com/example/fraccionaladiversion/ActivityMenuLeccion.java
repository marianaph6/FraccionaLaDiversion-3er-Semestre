package com.example.fraccionaladiversion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ActivityMenuLeccion extends AppCompatActivity {

    Button  btnJuegoSimplificar,btnJuegoOperaciones,btnJuegoGraficas;
    String Id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_lecciones);
        Conectar();
        Id = getIntent().getStringExtra("IdEstudiante");

        btnJuegoSimplificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent I = new Intent(getApplicationContext(), ActivityLeccion.class);
                I.putExtra("Puntaje",0);
                I.putExtra("Contador",0);
                I.putExtra("IdEstudiante",Id);
                I.putExtra("Tema","Simplificar");
                startActivity(I);
            }
        });
        btnJuegoOperaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent I = new Intent(getApplicationContext(), ActivityLeccion.class);
                I.putExtra("Puntaje",0);
                I.putExtra("Contador",0);
                I.putExtra("IdEstudiante",Id);
                I.putExtra("Tema","Operaciones");
                startActivity(I);
            }
        });

        btnJuegoGraficas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent I = new Intent(getApplicationContext(), ActivityLeccion.class);
                I.putExtra("Puntaje",0);
                I.putExtra("Contador",0);
                I.putExtra("IdEstudiante",Id);
                I.putExtra("Tema","Graficar");
                startActivity(I);
            }
        });
    }

    private void Conectar(){
        btnJuegoSimplificar = findViewById(R.id.btnJuegoSimplificar);
        btnJuegoOperaciones = findViewById(R.id.btnJuego2);
        btnJuegoGraficas=findViewById(R.id.btnGraficas);
    }
}
