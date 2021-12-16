package com.example.fraccionaladiversion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    TextView tvRegistrar;
    EditText etUsuario, etContrasena;
    Button btnIngesar,btnVolver;
    String Usuario,Password,Nombre="";
    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;
    Boolean valor=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        conectar();
        database = FirebaseDatabase.getInstance();
        myRef=database.getReference();
        mAuth = FirebaseAuth.getInstance();
        final String Tipo = getIntent().getStringExtra("Tipo");

        tvRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent I = new Intent(getApplicationContext(), RegistrarActivity.class);
                startActivity(I);
            }
        });
        btnIngesar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etUsuario.getText().toString().equals("") || etContrasena.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Por favor llene todos los campos", Toast.LENGTH_LONG).show();
                }else{
                    Usuario=etUsuario.getText().toString();
                    Password=etContrasena.getText().toString();
                    if(Tipo.equals("Profesor")){
                        ValidarPerfil(etUsuario.getText().toString(),"Profesor");
                       mAuth.signInWithEmailAndPassword(etUsuario.getText().toString(),etContrasena.getText().toString()).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                           @Override
                           public void onComplete(@NonNull Task<AuthResult> task) {
                               if(!task.isSuccessful() || (valor==false)){
                                   Toast.makeText(getApplicationContext(), "Datos erroneos", Toast.LENGTH_LONG).show();
                               }else{
                                   Intent I = new Intent(getApplicationContext(), MenuProfesorActivity.class);
                                   I.putExtra("IdProfesor", etUsuario.getText().toString());
                                   startActivity(I);
                               }
                           }
                       });
                    }else{
                        ValidarPerfil(etUsuario.getText().toString(),"Estudiante");
                        mAuth.signInWithEmailAndPassword(etUsuario.getText().toString(),etContrasena.getText().toString()).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(!task.isSuccessful() || (valor==false)){
                                    Toast.makeText(getApplicationContext(), "Datos erroneos", Toast.LENGTH_LONG).show();
                                }else{
                                    Intent I = new Intent(getApplicationContext(), MenuEstudianteActivity.class);
                                    I.putExtra("IdEstudiante", etUsuario.getText().toString());
                                    startActivity(I);
                                }
                            }
                        });
                    }
                }
            }
        });

        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Intro_Screen.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void ValidarPerfil(final String correo,String perfil){
        if(perfil.equals("Profesor")){
            myRef.child("Profesores").addValueEventListener(new ValueEventListener() {
                @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                            Profesores p=snapshot.getValue(Profesores.class);
                            if(p.getEmail().equals(correo)){
                                valor=true;
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
        }else{
            myRef.child("Estudiante").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        Estudiante e=snapshot.getValue(Estudiante.class);
                        if(e.getEmail().equals(correo)){
                            valor=true;
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });

        }
    }

    private void conectar() {
        tvRegistrar = findViewById(R.id.tvRegistrar);
        etUsuario = findViewById(R.id.etUsuario);
        etContrasena = findViewById(R.id.etContra);
        btnIngesar = findViewById(R.id.btnIngresar);
        btnVolver=findViewById(R.id.btnvolver);
    }
}
