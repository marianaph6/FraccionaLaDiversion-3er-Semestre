package com.example.fraccionaladiversion;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class  CrearGrupoActivity extends AppCompatActivity {

    EditText etColegio, etGrado;
    Button btnCrear;
    String CodigoGrupo;
    FirebaseDatabase database;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_grupo);
        conectar();
        database = FirebaseDatabase.getInstance();
        myRef=database.getReference();
        final String Id=getIntent().getStringExtra("IdProfesor");
        btnCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GenerarCodigoGrupo();
                AlertDialog.Builder builder = new AlertDialog.Builder(CrearGrupoActivity.this);
                builder.setTitle("GRUPO CREADO");
                builder.setMessage("Codigo grupo: " + CodigoGrupo)
                        .setNeutralButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                agregar(etColegio.getText().toString(),etGrado.getText().toString(),CodigoGrupo,Id);
                                Toast.makeText(getApplicationContext(),"Grupo creado",Toast.LENGTH_LONG).show();
                                Intent I = new Intent(getApplicationContext(), MenuProfesorActivity.class);
                                I.putExtra("IdProfesor", Id);
                                startActivity(I);
                                CrearGrupoActivity.this.finish();
                            }
                        }).setCancelable(false).show();
            }
        });
    }

    private void GenerarCodigoGrupo() {
        String [] miArreglo = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M",
                "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
        Random r = new Random();
        String cod = "";
        for(int i = 0; i < 6; i++){
            if(i < 3){
                cod += (r.nextInt((9 - 0) + 1) + 0);
            }else{
                int a = r.nextInt((25 - 0) + 1) + 0;
                cod += miArreglo[a];
            }
        }
        CodigoGrupo = cod;
    }

    public void agregar(String colegio,String grado,String codigo,String Idprofesor){
        Grupo e=new Grupo();
        e.setUid(UUID.randomUUID().toString());
        e.setCodigo(codigo);
        e.setColegio(colegio);
        e.setGrado(grado);
        e.setIdProfesor(Idprofesor);
        myRef.child("Grupo").child(e.getUid()).setValue(e);
    }


    private void conectar() {
        etColegio = findViewById(R.id.etColegio);
        etGrado = findViewById(R.id.etGrado);
        btnCrear = findViewById(R.id.btnCrear);
    }
}
