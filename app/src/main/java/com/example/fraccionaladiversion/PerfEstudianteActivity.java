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

public class PerfEstudianteActivity extends AppCompatActivity {

    String Id;
    Button btnEliminar, btnCerrar, btnActualizar;
    EditText etN1, etN2, etA1, etA2;
    FirebaseDatabase database;
    DatabaseReference myRef;
    String uid,documento,idgrupo;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perf_estudiante);
        database = FirebaseDatabase.getInstance();
        myRef=database.getReference();
        conectar();
        Id = getIntent().getStringExtra("IdEstudiante");
        llenarEditText(Id);
        btnCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent I = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(I);
                PerfEstudianteActivity.this.finish();
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
        myRef.child("Estudiante").child(uid).removeValue();
        Intent I = new Intent(getApplicationContext(), Intro_Screen.class);
        startActivity(I);
        PerfEstudianteActivity.this.finish();
    }

    private void DialogEliminar(final String uid) {
        AlertDialog.Builder builder = new AlertDialog.Builder(PerfEstudianteActivity.this);
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
        Estudiante estudiante=new Estudiante();
        estudiante.setEmail(correo);
        estudiante.setUid(uid);
        estudiante.setDocumento(id);
        estudiante.setPrimerNombre(etN1.getText().toString());
        estudiante.setSegundoNombre(etN2.getText().toString());
        estudiante.setPrimerApellido(etA1.getText().toString());
        estudiante.setSegundoApellido(etA2.getText().toString());
        myRef.child("Estudiante").child(estudiante.getUid()).setValue(estudiante);
        AlertDialog.Builder builder = new AlertDialog.Builder(PerfEstudianteActivity.this);
        builder.setTitle("Datos Actualizados");
            builder.setMessage("Sus datos se han actualizado correctamente" )
                    .setNeutralButton( "Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent I = new Intent(getApplicationContext(), MenuEstudianteActivity.class);
                            I.putExtra("IdEstudiante", correo);
                            startActivity(I);
                            PerfEstudianteActivity.this.finish();
                        }
                    }).setCancelable(false).show();

    }

    private void llenarEditText(final String correo) {
        myRef.child("Estudiante").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Estudiante e=snapshot.getValue(Estudiante.class);
                    if(e.getEmail().equals(correo)){
                        uid=e.getUid();
                        documento=e.getDocumento();
                        etN1.setText(e.PrimerNombre);
                        etN2.setText(e.SegundoNombre);
                        etA1.setText(e.PrimerApellido);
                        etA2.setText(e.SegundoApellido);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void conectar() {
        btnActualizar = findViewById(R.id.btnActualiz);
        btnCerrar = findViewById(R.id.btnCerr);
        btnEliminar = findViewById(R.id.btnElim);
        etN1 = findViewById(R.id.etNom1);
        etN2 = findViewById(R.id.etNom2);
        etA1 = findViewById(R.id.etApell1);
        etA2 = findViewById(R.id.etApell2);

    }
}
