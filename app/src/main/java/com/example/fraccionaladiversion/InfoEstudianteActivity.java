package com.example.fraccionaladiversion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class InfoEstudianteActivity extends AppCompatActivity {

    String Id,Codigo;
    TextView tvInfo;
    Button btnProgreso, btnRating;
    String codGrupo="";
    FirebaseDatabase database;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_estudiante);
        Id = getIntent().getStringExtra("IdEstudiante");
        Codigo = getIntent().getStringExtra("IdGrupo");
        conectar();
        llenarTextView();
        btnProgreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent I = new Intent(getApplicationContext(), ProgresoEstActivity.class);
                I.putExtra("IdEstudiante", Id);
                startActivity(I);
            }
        });

        btnRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent I = new Intent(getApplicationContext(), RatingActivity.class);
                I.putExtra("IdGrupo", Codigo);
                startActivity(I);
            }
        });
    }

    private void llenarTextView() {

    }

    private void conectar() {
        btnProgreso = findViewById(R.id.btnProgreso);
        btnRating = findViewById(R.id.btnRat);
    }
}
