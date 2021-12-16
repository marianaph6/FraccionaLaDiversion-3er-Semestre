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
import java.util.Random;

public class JuegoSimplificarActivity extends AppCompatActivity {

    Button btn1, btn2, btn3;
    TextView tvEjercicioA, tvEjercicioB;
    int NumeroA, NumeroB;
    int RespCorrectaA = 0, RespCorrectaB = 0, ubicacionCorrecta = 0;
    int puntaje, cont;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/M/yyyy", Locale.getDefault());
    String fecha = simpleDateFormat.format(new Date());
    FirebaseDatabase database;
    DatabaseReference myRef;
    String codigo="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego_simplificar);
        database = FirebaseDatabase.getInstance();
        myRef=database.getReference();
        conectar();
        generarEjercicio();
        generarRespuestaCorrecta();
        Bundle datos = this.getIntent().getExtras();
        generarRespuestasIncorrectas();
        puntaje = datos.getInt("Puntaje");
        cont = datos.getInt("Contador");
        final String idEstudiante = datos.getString("IdEstudiante");
        if (cont == 10) {
            ObtenerGrupo(idEstudiante);
            AgregarPuntaje(codigo,idEstudiante,puntaje,"Simplificar",fecha);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("JUEGO TERMINADO");
            builder.setMessage("Puntaje total" + "\n" + "Correctas: " + puntaje + "\n" + "Incorrectas: " + (10 - puntaje));
            builder.setPositiveButton("Volver a jugar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ReiniciarActivity(0, 0);
                }
            });
            builder.setNegativeButton("Volver al menú", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(getApplicationContext(), MenuEstudianteActivity.class);
                    intent.putExtra("IdEstudiante", idEstudiante);
                    startActivity(intent);
                    finish();

                }
            });
            builder.setCancelable(false);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ubicacionCorrecta == 1) {
                    Toast.makeText(getApplicationContext(), "Respuesta Correcta", Toast.LENGTH_SHORT).show();
                    puntaje++;
                } else {
                    Toast.makeText(getApplicationContext(), "Respuesta Incorrecta", Toast.LENGTH_SHORT).show();
                }
                cont++;
                ReiniciarActivity(puntaje, cont);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ubicacionCorrecta == 2) {
                    Toast.makeText(getApplicationContext(), "Respuesta Correcta", Toast.LENGTH_SHORT).show();
                    puntaje++;
                } else {
                    Toast.makeText(getApplicationContext(), "Respuesta Incorrecta", Toast.LENGTH_SHORT).show();
                }
                cont++;
                ReiniciarActivity(puntaje, cont);
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ubicacionCorrecta == 3) {
                    Toast.makeText(getApplicationContext(), "Respuesta Correcta", Toast.LENGTH_SHORT).show();
                    puntaje++;
                } else {
                    Toast.makeText(getApplicationContext(), "Respuesta Incorrecta", Toast.LENGTH_SHORT).show();
                }
                cont++;
                ReiniciarActivity(puntaje, cont);
            }
        });
    }

    private void ReiniciarActivity(int puntaje, int cont) {
        Intent intent = getIntent();
        intent.putExtra("Puntaje", puntaje);
        intent.putExtra("Contador", cont);
        finish();
        startActivity(intent);
    }

    private void generarEjercicio() {
        Random r = new Random();
        int multiplo1 = r.nextInt((9 - 1) + 1) + 1;
        int multiplo2 = r.nextInt((9 - 1) + 1) + 1;
        int cantidad1 = r.nextInt((10 - 1) + 1) + 1;
        int cantidad2 = r.nextInt((10 - 1) + 1) + 1;

        NumeroA = multiplo1 * cantidad1;
        NumeroB = multiplo2 * cantidad2;
        tvEjercicioA.setText(NumeroA + "");
        tvEjercicioB.setText(NumeroB + "");
    }

    private void generarRespuestaCorrecta() {
        int respuestaA = NumeroA;
        int respuestaB = NumeroB;
        while (respuestaA % 2 == 0 && respuestaB % 2 == 0) {
            respuestaA = respuestaA / 2;
            respuestaB = respuestaB / 2;
        }
        while (respuestaA % 3 == 0 && respuestaB % 3 == 0) {
            respuestaA = respuestaA / 3;
            respuestaB = respuestaB / 3;
        }
        while (respuestaA % 5 == 0 && respuestaB % 5 == 0) {
            respuestaA = respuestaA / 5;
            respuestaB = respuestaB / 5;
        }
        while (respuestaA % 7 == 0 && respuestaB % 7 == 0) {
            respuestaA = respuestaA / 7;
            respuestaB = respuestaB / 7;
        }
        RespCorrectaA = respuestaA;
        RespCorrectaB = respuestaB;
        Random r = new Random();
        int ubicacion = r.nextInt((3 - 1) + 1) + 1;
        ubicacionCorrecta = ubicacion;
        String Respuesta = "." + RespCorrectaA + "\n" + "____" + "\n" + RespCorrectaB;

        if (ubicacion == 1) {
            btn1.setText(Respuesta);
        } else if (ubicacion == 2) {
            btn2.setText(Respuesta);
        } else {
            btn3.setText(Respuesta);
        }
    }

    private void generarRespuestasIncorrectas() {
        Random r = new Random();
        int a = 0, b = 0, c = 0, d = 0;

        do {
            a = r.nextInt((15 - 1) + 1) + 1;
            b = r.nextInt((9 - 1) + 1) + 1;
            c = r.nextInt((2 - 1) + 1) + 1;
            d = r.nextInt((12 - 1) + 1) + 1;
        } while ((a == RespCorrectaA && c == RespCorrectaB) || (b == RespCorrectaA && d == RespCorrectaB));

        if (!btn1.getText().equals("")) {
            btn2.setText(a + "\n" + "____" + "\n" + c);
            btn3.setText(b + "\n" + "____" + "\n" + d);
        } else if (!btn2.getText().equals("")) {
            btn1.setText(a + "\n" + "____" + "\n" + c);
            btn3.setText(b + "\n" + "____" + "\n" + d);
        } else {
            btn2.setText(a + "\n" + "____" + "\n" + c);
            btn1.setText(b + "\n" + "____" + "\n" + d);
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
                                AgregarPuntaje(codigo,email,puntaje,"Simplificar",fecha);
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
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        tvEjercicioA = findViewById(R.id.tvNumero1);
        tvEjercicioB = findViewById(R.id.tvNumero2);
    }
}
