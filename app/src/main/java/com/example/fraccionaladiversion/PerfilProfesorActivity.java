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
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PerfilProfesorActivity extends AppCompatActivity {

    String Id;
    Button btnEliminar, btnCerrar, btnActualizar;
    EditText etN1, etN2, etA1, etA2;
    FirebaseDatabase database;
    DatabaseReference myRef;
    String uid,documento;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_profesor);
        database = FirebaseDatabase.getInstance();
        myRef=database.getReference();
        conectar();
        Id = getIntent().getStringExtra("IdProfesor");
        llenarEditText(Id);
        btnCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent I = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(I);
                PerfilProfesorActivity.this.finish();
            }
        });
        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                actualizar(Id,uid,documento);
            }
        });
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogEliminar(uid);
            }
        });
    }

    private void eliminar(final String uid) {
        myRef.child("Profesores").child(uid).removeValue();
        Intent I = new Intent(getApplicationContext(), Intro_Screen.class);
        startActivity(I);
        PerfilProfesorActivity.this.finish();
    }

    private void DialogEliminar(final String uid) {
        AlertDialog.Builder builder = new AlertDialog.Builder(PerfilProfesorActivity.this);
        builder.setTitle("REGISTRADO");
        builder.setMessage("¿Esta seguro de querer eliminar la cuenta? NO PODRÁ RECUPERARLA")
                .setNeutralButton( "Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        eliminar(uid);
                    }
                }).setCancelable(true).show();
    }


    private void actualizar(final String correo,String uid,String id) {
        Profesores profesores=new Profesores();
        profesores.setEmail(correo);
        profesores.setUid(uid);
        profesores.setDocumento(id);
        profesores.setPrimerNombre(etN1.getText().toString());
        profesores.setSegundoNombre(etN2.getText().toString());
        profesores.setPrimerApellido(etA1.getText().toString());
        profesores.setSegundoApellido(etA2.getText().toString());
        myRef.child("Profesores").child(profesores.getUid()).setValue(profesores);
        AlertDialog.Builder builder = new AlertDialog.Builder(PerfilProfesorActivity.this);
        builder.setTitle("Datos Actualizados");
        builder.setMessage("Sus datos se han actualizado correctamente" )
                .setNeutralButton( "Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent I = new Intent(getApplicationContext(), MenuProfesorActivity.class);
                        I.putExtra("IdProfesor", correo);
                        startActivity(I);
                        PerfilProfesorActivity.this.finish();
                    }
                }).setCancelable(false).show();
    }

    private void llenarEditText(final String correo) {
        myRef.child("Profesores").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Profesores p=snapshot.getValue(Profesores.class);
                    if(p.getEmail().equals(correo)){
                        uid=p.getUid();
                        documento=p.getDocumento();
                        etN1.setText(p.PrimerNombre);
                        etN2.setText(p.SegundoNombre);
                        etA1.setText(p.PrimerApellido);
                        etA2.setText(p.SegundoApellido);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void conectar() {
        btnActualizar = findViewById(R.id.btnActualizar);
        btnCerrar = findViewById(R.id.btnCerrar);
        btnEliminar = findViewById(R.id.btnEliminar);
        etN1 = findViewById(R.id.etNombre1);
        etN2 = findViewById(R.id.etNombre2);
        etA1 = findViewById(R.id.etApellido1);
        etA2 = findViewById(R.id.etApellido2);

    }
}
