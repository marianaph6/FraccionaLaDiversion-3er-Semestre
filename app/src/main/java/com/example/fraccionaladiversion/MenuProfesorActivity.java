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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MenuProfesorActivity extends AppCompatActivity {

    Button btnCrearGrupo,btnListaGrupos, btnPerfil;
    String Id;
    TextView tvInfoProfesor;
    FirebaseDatabase database;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_profesor);
        database = FirebaseDatabase.getInstance();
        myRef=database.getReference();
        Conectar();
        Id = getIntent().getStringExtra("IdProfesor");
        llenarTextView(Id);
        btnCrearGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent I = new Intent(getApplicationContext(), CrearGrupoActivity.class);
                I.putExtra("IdProfesor", Id);
                startActivity(I);
            }
        });
        btnListaGrupos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent I = new Intent(getApplicationContext(), ListaGruposActivity.class);
                I.putExtra("IdProfesor", Id);
                startActivity(I);
            }
        });
        btnPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent I = new Intent(getApplicationContext(), PerfilProfesorActivity.class);
                I.putExtra("IdProfesor", Id);
                startActivity(I);
            }
        });
    }

    private void llenarTextView(final String correo) {
        final String line;
        myRef.child("Profesores").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    myRef.child("Profesores").child(snapshot.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Profesores profesores = snapshot.getValue(Profesores.class);
                            String pNombre = profesores.getPrimerNombre();
                            String sNombre = profesores.getSegundoNombre();
                            String pApellido = profesores.getPrimerApellido();
                            String sApellido = profesores.getSegundoApellido();
                            String email = profesores.getEmail();
                            if (correo.equals(email)) {
                                tvInfoProfesor.setText("Nombre: " + pNombre + " " + sNombre + "\nApellido: " + pApellido + " "
                                        + sApellido + "\nCorreo: " + email);
                                //Toast.makeText(getApplicationContext(), "yes", Toast.LENGTH_LONG).show();
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

    private void Conectar(){
        btnCrearGrupo=findViewById(R.id.btnCrearGrupo);
        btnListaGrupos=findViewById(R.id.btnListaGrupos);
        tvInfoProfesor = findViewById(R.id.tvInfoPerfil);
        btnPerfil = findViewById(R.id.btnEditarPerfin);
    }
}
