package com.example.fraccionaladiversion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditarGrupo extends AppCompatActivity {


    EditText txtColegio,txtGrado;
    Button btnEditar;
    FirebaseDatabase database;
    DatabaseReference myRef;
    String uid,Id,IdProfesor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_grupo);
        database = FirebaseDatabase.getInstance();
        myRef=database.getReference();
        txtColegio=findViewById(R.id.txtColegio);
        txtGrado=findViewById(R.id.txtGrado);
        btnEditar=findViewById(R.id.btnEditar);
        Id = getIntent().getStringExtra("Id");
        IdProfesor = getIntent().getStringExtra("IdProfesor");
        LLenar(Id);
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Actualizar(Id,IdProfesor);
            }
        });
    }

    private void Actualizar(final String id,final String idprofe) {
        myRef.child("Grupo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Grupo e=snapshot.getValue(Grupo.class);
                    if(e.getCodigo().equals(id)){
                        uid=e.getUid();
                        e.setGrado(txtGrado.getText().toString());
                        e.setColegio(txtColegio.getText().toString());
                        myRef.child("Grupo").child(e.getUid()).setValue(e);
                        AlertDialog.Builder builder = new AlertDialog.Builder(EditarGrupo.this);
                        builder.setTitle("Datos del grupo actualizados");
                        builder.setMessage("Sus datos se han actualizado correctamente" )
                                .setNeutralButton( "Okay", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent I = new Intent(getApplicationContext(), MenuProfesorActivity.class);
                                        I.putExtra("IdProfesor", idprofe);
                                        startActivity(I);
                                        EditarGrupo.this.finish();
                                    }
                                }).setCancelable(false).show();

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void LLenar(final String id) {
        myRef.child("Grupo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Grupo e=snapshot.getValue(Grupo.class);
                    if(e.getCodigo().equals(id)){
                        txtColegio.setText(e.getColegio());
                        txtGrado.setText(e.getGrado());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
