package com.example.fraccionaladiversion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Connection;

public class MenuEstudianteActivity extends AppCompatActivity {

    TextView tvinfoPerfil;
    Button btnLeccion, btnPerfil, btnInfo,btnJuegos;
    String Id;
    FirebaseDatabase database;
    DatabaseReference myRef;
    String codGrupo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_estudiante);
        database = FirebaseDatabase.getInstance();
        myRef=database.getReference();
        conectar();
        Id = getIntent().getStringExtra("IdEstudiante");
        llenarTextView(Id);
        btnPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent I = new Intent(getApplicationContext(), PerfEstudianteActivity.class);
                I.putExtra("IdEstudiante", Id);
                startActivity(I);
            }
        });
        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent I = new Intent(getApplicationContext(), InfoEstudianteActivity.class);
                I.putExtra("IdEstudiante", Id);
                I.putExtra("IdGrupo", codGrupo);
                startActivity(I);
            }
        });
        btnLeccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent I = new Intent(getApplicationContext(), ActivityMenuLeccion.class);
                I.putExtra("IdEstudiante", Id);
                startActivity(I);
            }
        });

    }

    private void llenarTextView(final String correo) {
        myRef.child("Estudiante").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    myRef.child("Estudiante").child(snapshot.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Estudiante estudiante = snapshot.getValue(Estudiante.class);
                            String pNombre = estudiante.getPrimerNombre();
                            String sNombre = estudiante.getSegundoNombre();
                            String pApellido = estudiante.getPrimerApellido();
                            String sApellido = estudiante.getSegundoApellido();
                            String email = estudiante.getEmail();
                            String cod=estudiante.getIdGrupo();
                            if (correo.equals(email)) {
                                tvinfoPerfil.setText("Nombre: " + pNombre + " " + sNombre + "\nApellido: " + pApellido + " "
                                        + sApellido + "\nCorreo: " + email);
                                codGrupo=cod;
                                //Toast.makeText(getApplicationContext(), "yes"+codGrupo, Toast.LENGTH_LONG).show();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        }
        );
    }

    private void conectar() {
        btnLeccion = findViewById(R.id.btnAprende);
        tvinfoPerfil=findViewById(R.id.tvInfoPerfil);
        //btnJuegos=findViewById(R.id.btnPractica);
        btnPerfil = findViewById(R.id.btnEditarPerfil);
        btnInfo = findViewById(R.id.btnInfoEst);
    }
}
