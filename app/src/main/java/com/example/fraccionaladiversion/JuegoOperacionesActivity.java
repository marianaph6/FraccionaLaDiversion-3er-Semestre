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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class JuegoOperacionesActivity extends AppCompatActivity {

    TextView txtNum1A, txtNum2A, txtNum1B, txtNum2B, txtOp;
    EditText txtResA, txtResB;
    Button btnAceptar;
    String[] simbolo = {"+","-","x","/"};
    int num1A, num2A, num1B, num2B, i, numerador, denominador;
    int puntaje,cont;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/M/yyyy", Locale.getDefault());
    String fecha = simpleDateFormat.format(new Date());
    FirebaseDatabase database;
    DatabaseReference myRef;
    String codigo="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego_operaciones);
        database = FirebaseDatabase.getInstance();
        myRef=database.getReference();
        Connect();
        generarEjercicio();
        generarRespuestaCorrecta();
        Bundle datos = this.getIntent().getExtras();
        puntaje=datos.getInt("Puntaje");
        cont=datos.getInt("Contador");
        final String idEstudiante=datos.getString("IdEstudiante");
        if(cont==10){
            ObtenerGrupo(idEstudiante);
            AgregarPuntaje(codigo,idEstudiante,puntaje,"Operaciones",fecha);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("JUEGO TERMINADO");
            builder.setMessage("Puntaje total" + "\n" + "Correctas: " +puntaje + "\n" + "Incorrectas: " +(10-puntaje));
            builder.setPositiveButton("Volver a jugar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ReiniciarActivity(0,0);
                }
            });
            builder.setNegativeButton("Volver al menú", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(getApplicationContext(), MenuEstudianteActivity.class);
                    intent.putExtra("IdEstudiante",idEstudiante);
                    startActivity(intent);
                    finish();

                }
            });
            builder.setCancelable(false);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtResA.getText().toString().equals(String.valueOf(numerador)) && txtResB.getText().toString().equals(String.valueOf(denominador))){
                    Toast.makeText(getApplicationContext(), "Respuesta Correcta", Toast.LENGTH_LONG).show();
                    puntaje++;
                }else{
                    Toast.makeText(getApplicationContext(), "Respuesta Incorrecta", Toast.LENGTH_LONG).show();
                }
                cont++;
                ReiniciarActivity(puntaje,cont);
            }
        });
    }

    private void ReiniciarActivity(int puntaje,int cont){
        Intent intent = getIntent();
        intent.putExtra("Puntaje",puntaje);
        intent.putExtra("Contador",cont);
        finish();
        startActivity(intent);
    }

    private void generarEjercicio() {
        num1A = (int) (Math.random()*10+1);
        num2A = (int) (Math.random()*10+1);
        num1B = (int) (Math.random()*10+1);
        num2B = (int) (Math.random()*10+1);
        i = (int) (Math.random()*4);

        txtNum1A.setText(num1A+"");
        txtNum2A.setText(num2A+"");
        txtOp.setText(simbolo[i]);
        txtNum1B.setText(num1B+"");
        txtNum2B.setText(num2B+"");
    }
    private void generarRespuestaCorrecta() {
        int a,b;
        if(i == 0){
            if(num2A == num2B){
                numerador = num1A + num1B;
                denominador = num2A;
            }else{
                a = num1A * num2B;
                b = num1B * num2A;
                numerador = a + b;
                denominador = num2A * num2B;
            }
        }else if(i == 1){
            if(num2A == num2B){
                numerador = num1A - num1B;
                denominador = num2A;
            }else{
                a = num1A * num2B;
                b = num1B * num2A;
                numerador = a - b;
                denominador = num2A * num2B;
            }
        }else if(i == 2){
            numerador = num1A * num1B;
            denominador = num2A * num2B;
        }else{
            numerador = num1A * num2B;
            denominador = num2A * num1B;
        }
    }

    public void AgregarPuntaje(String idGrupo, String idEstudiante, int puntaje, String tipoJuego, String fecha){
        Map<String,Object> datos=new HashMap<>();
        datos.put("Fecha",fecha);
        datos.put("IdGrupo",idGrupo);
        datos.put("IdEstudiante",idEstudiante);
        datos.put("Puntaje",puntaje);
        datos.put("TipoJuego",tipoJuego);
        myRef.child("Progreso").push().setValue(datos);
    }

    public void ObtenerGrupo(final String email){
        myRef.child("Estudiante").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(final DataSnapshot snapshot: dataSnapshot.getChildren()){
                    myRef.child("Estudiante").child(snapshot.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Estudiante estudiante=snapshot.getValue(Estudiante.class);
                            String idgrupo=estudiante.getIdGrupo();
                            String correo=estudiante.getEmail();
                            if(email.equals(correo)){
                                codigo=idgrupo;
                                AgregarPuntaje(codigo,email,puntaje,"Operaciones",fecha);
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


    private void Connect() {
        txtNum1A = findViewById(R.id.txtNum1A);
        txtNum2A = findViewById(R.id.txtNum2A);
        txtNum1B = findViewById(R.id.txtNum1B);
        txtNum2B = findViewById(R.id.txtNum2B);
        txtOp = findViewById(R.id.txtOp);
        txtResA = findViewById(R.id.txtRespuestaA);
        txtResB = findViewById(R.id.txtRespuestaB);
        btnAceptar = findViewById(R.id.btnGo);
    }
}
