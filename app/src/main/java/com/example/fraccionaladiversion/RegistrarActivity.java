package com.example.fraccionaladiversion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RegistrarActivity extends AppCompatActivity {

    EditText etIdUsuario, etPNombre, etSNombre, etPApellido, etSApellido, etEmail, etIdGrupo,etPassword;
    RadioButton rbEstudiante, rbProfesor;
    Button btnRegistrar;
    String IdUsuario, PNombre, SNombre, PApellido, SApellido, Email, IdGrupo,Password;
    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);
        conectar();
        database = FirebaseDatabase.getInstance();
        myRef=database.getReference();
        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        rbEstudiante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etIdGrupo.setText("");
                etIdGrupo.setVisibility(View.VISIBLE);
            }
        });
        rbProfesor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etIdGrupo.setText("");
                etIdGrupo.setVisibility(View.INVISIBLE);
            }
        });
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((etIdUsuario.getText().toString().equals("") || etPNombre.getText().toString().equals("") || etPApellido.getText().toString().equals("") ||
                        etEmail.getText().toString().equals("")) || (rbEstudiante.isChecked() && etIdGrupo.getText().toString().equals("")) || (!rbProfesor.isChecked() && !rbEstudiante.isChecked())){
                    Toast.makeText(getApplicationContext(), "Por favor llene todos los campos obligatorios **", Toast.LENGTH_LONG).show();
                }else{
                    IdUsuario=etIdUsuario.getText().toString();
                    PNombre=etPNombre.getText().toString();
                    SNombre=etSNombre.getText().toString();
                    PApellido=etPApellido.getText().toString();
                    SApellido=etSApellido.getText().toString();
                    Email=etEmail.getText().toString();
                    IdGrupo=etIdGrupo.getText().toString();
                    Password=etPassword.getText().toString();
                    if(rbProfesor.isChecked()) {
                            RegistrarAuthentication(Email,Password,"profesor",IdUsuario,PNombre, SNombre,PApellido,SApellido);
                    }else{
                         RegistrarAuthentication(Email,Password,"estudiante",IdUsuario,PNombre, SNombre,PApellido,SApellido);
                    }
                }
            }
        });
    }

    private void RegistrarAuthentication(final String email, String password, final String perfil, final String Idprofesor, final String PrimerNombre,
                                         final String SegundoNombre, final String PrimerApellido, final String SegundoApellido) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Toast.makeText(getApplicationContext(),"Exito", Toast.LENGTH_LONG).show();
                            if(perfil.equals("profesor")){
                                RegistrarProfesor(Idprofesor,PrimerNombre,SegundoNombre,PrimerApellido,SegundoApellido,email);
                                DialogDeIngreso("profesor");
                            }else{
                                validarGrupo(IdGrupo);
                            }
                        } else {
                            Toast.makeText(getApplicationContext(),"Fail", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void RegistrarProfesor(String Id,String PrimerNombre,String SegundoNombre,String PrimerApellido,String SegundoApellido
            ,String Email){
        Profesores p=new Profesores();
        p.setUid(UUID.randomUUID().toString());
        p.setDocumento(Id);
        p.setPrimerNombre(PrimerNombre);
        p.setSegundoNombre(SegundoNombre);
        p.setPrimerApellido(PrimerApellido);
        p.setSegundoApellido(SegundoApellido);
        p.setEmail(Email);
        myRef.child("Profesores").child(p.getUid()).setValue(p);
    }

    public void RegistrarEstudiante(String Id,String PrimerNombre,String SegundoNombre,String PrimerApellido,String SegundoApellido
            ,String Email,String IdGrupo){
        Estudiante e=new Estudiante();
        e.setUid(UUID.randomUUID().toString());
        e.setDocumento(Id);
        e.setPrimerNombre(PrimerNombre);
        e.setSegundoNombre(SegundoNombre);
        e.setPrimerApellido(PrimerApellido);
        e.setSegundoApellido(SegundoApellido);
        e.setEmail(Email);
        e.setIdGrupo(IdGrupo);
        myRef.child("Estudiante").child(e.getUid()).setValue(e);
    }

    private void DialogDeIngreso(String Tipo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(RegistrarActivity.this);
        builder.setTitle("REGISTRADO");
        if(Tipo.equals("profesor")){
            builder.setMessage("Usuario: " + Email + "\n Constraseña: "+Password )
                    .setNeutralButton( "Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent I = new Intent(getApplicationContext(), MenuProfesorActivity.class);
                            I.putExtra("IdProfesor", Email);
                            startActivity(I);
                            RegistrarActivity.this.finish();
                        }
                    }).setCancelable(false).show();
        }else{
            builder.setMessage("Usuario: " + Email + "\n Constraseña: "+Password )
                    .setNeutralButton( "Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent I = new Intent(getApplicationContext(), MenuEstudianteActivity.class);
                            I.putExtra("IdEstudiante", Email);
                            startActivity(I);
                            RegistrarActivity.this.finish();
                        }
                    }).setCancelable(false).show();
            builder.setCancelable(false);
        }
    }

    private void validarGrupo(final String codigo){
        myRef.child("Grupo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(final DataSnapshot snapshot: dataSnapshot.getChildren()){
                    myRef.child("Grupo").child(snapshot.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Grupo grupo=snapshot.getValue(Grupo.class);
                            String idg=grupo.getCodigo();
                            if(idg.equals(codigo)){
                                RegistrarEstudiante(IdUsuario,PNombre,SNombre,PApellido,SApellido,Email,IdGrupo);
                                DialogDeIngreso("estudiante");
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
        });
    }


    private void conectar() {
        etPNombre = findViewById(R.id.etPrimerNombre);
        etSNombre = findViewById(R.id.etSegundoNombre);
        etPApellido = findViewById(R.id.etPrimerApellido);
        etSApellido = findViewById(R.id.etSegundoApellido);
        etIdUsuario = findViewById(R.id.etId);
        etIdGrupo = findViewById(R.id.etIdGrupo);
        etEmail = findViewById(R.id.etEmail);
        etPassword=findViewById(R.id.etPassword);
        rbEstudiante = findViewById(R.id.rbEstudiante);
        rbProfesor = findViewById(R.id.rbProfe);
        btnRegistrar = findViewById(R.id.btnRegistrar);
    }
}