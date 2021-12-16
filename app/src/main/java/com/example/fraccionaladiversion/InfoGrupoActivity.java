package com.example.fraccionaladiversion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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

public class InfoGrupoActivity extends AppCompatActivity {

    TextView tvInfo, tvResultado;
    Button btnEditar, btnEliminar, btnRating, btnEspecifico, btnCantidad;
    String Id, info;
    FirebaseDatabase database;
    DatabaseReference myRef;
    int cantidadEst;
    String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_grupo);
        conectar();
        database = FirebaseDatabase.getInstance();
        myRef=database.getReference();
        Id = getIntent().getStringExtra("Id");
        //Toast.makeText(getApplicationContext(), Id, Toast.LENGTH_SHORT).show();
        info = getIntent().getStringExtra("Info");
        final String IdProfesor = getIntent().getStringExtra("IdProfesor");
        Cantidad(Id);
        btnCantidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "El grupo tiene " + cantidadEst+ " estudiantes.", Toast.LENGTH_LONG).show();
            }
        });
        btnRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent I = new Intent(getApplicationContext(), RatingActivity.class);
                I.putExtra("IdGrupo", Id);
                startActivity(I);
            }
        });
        btnEspecifico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent I = new Intent(getApplicationContext(), DiaEspecificoActivity.class);
                I.putExtra("Id", Id);
                startActivity(I);
            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "uid"+uid, Toast.LENGTH_LONG).show();
                Eliminar(Id,IdProfesor);
            }
        });

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent I = new Intent(getApplicationContext(), EditarGrupo.class);
                I.putExtra("Id", Id);
                I.putExtra("IdProfesor", IdProfesor);
                startActivity(I);
                InfoGrupoActivity.this.finish();
            }
        });
    }

    private void Cantidad(final String idgrupo) {
        myRef.child("Estudiante").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Estudiante e=snapshot.getValue(Estudiante.class);
                    if(e.getIdGrupo().equals(idgrupo)){
                        cantidadEst++;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void Eliminar(final String id,final String Id) {
        myRef.child("Grupo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Grupo e=snapshot.getValue(Grupo.class);
                    if(e.getCodigo().equals(id)){
                        uid=e.getUid();
                        DialogEliminar(uid,Id);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void eliminar(final String uid,String id) {
        myRef.child("Grupo").child(uid).removeValue();
        Intent I = new Intent(getApplicationContext(), MenuProfesorActivity.class);
        I.putExtra("IdProfesor", id);
        startActivity(I);
        InfoGrupoActivity.this.finish();
    }

    private void DialogEliminar(final String uid,final String Id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(InfoGrupoActivity.this);
        builder.setTitle("EIMINAR GRUPO");
        builder.setMessage("¿Esta seguro de querer el grupo cuenta? NO PODRÁ RECUPERARLO")
                .setNeutralButton( "Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        eliminar(uid,Id);
                    }
                }).setCancelable(true).show();
    }

    private void conectar() {
        btnCantidad = findViewById(R.id.btnCantidad);
        btnEditar = findViewById(R.id.btnEditar);
        btnEliminar = findViewById(R.id.btnEliminar);
        btnRating = findViewById(R.id.btnRating);
        btnEspecifico = findViewById(R.id.btnDiaEspecifico);
    }
}
