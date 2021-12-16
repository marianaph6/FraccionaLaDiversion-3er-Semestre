package com.example.fraccionaladiversion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class Juego_Graficas extends AppCompatActivity {

    ImageView ivOpciones;
    Button btnRestar,btnSumar,btnResponder;
    TextView tvGnum1,tvGnum2;
    Integer[] images2=new Integer[3];
    Integer[] images3=new Integer[4];
    Integer[] images4=new Integer[5];
    Integer[] images5=new Integer[6];
    Integer[] images6=new Integer[7];
    Integer[] images7=new Integer[8];
    Integer[] images8=new Integer[9];
    Integer[] images9=new Integer[10];
    Integer[] images10=new Integer[11];
    FirebaseDatabase database;
    DatabaseReference myRef;

    int cont=0,puntaje,contador;
    int Numero1=0,Numero2=0;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/M/yyyy", Locale.getDefault());
    String fecha = simpleDateFormat.format(new Date());
    String codigo="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego__graficas);
        database = FirebaseDatabase.getInstance();
        myRef=database.getReference();
        Conectar();
        Imagenes();
        generarEjercicio();
        Bundle datos = this.getIntent().getExtras();
        puntaje=datos.getInt("Puntaje");
        contador=datos.getInt("Contador");
        final String idEstudiante=datos.getString("IdEstudiante");

        if(contador==10){
            ObtenerGrupo(idEstudiante);
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

        final Integer[] images;

        switch (Numero2){
            case 2:
                images=images2;
                break;
            case 3:
                images=images3;
                break;
            case 4:
                images=images4;
                break;
            case 5:
                images=images5;
                break;
            case 6:
                images=images6;
                break;
            case 7:
                images=images7;
                break;
            case 8:
                images=images8;
                break;
            case 9:
                images=images9;
                break;
            case 10:
                images=images10;
                break;
            default:
                throw new IllegalStateException("Valor: " + (Numero2));
        }



        ivOpciones.setImageDrawable(getResources().getDrawable(images[cont]));
        ivOpciones.setTag("res/drawable/img"+Numero1+"_"+cont+".png");

        btnSumar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cont++;
                if(cont==Numero2)btnSumar.setVisibility(View.INVISIBLE);
                if(cont==Numero2-1)btnSumar.setVisibility(View.VISIBLE);
                if (cont == 1) btnRestar.setVisibility(View.VISIBLE);
                if (cont == 0) btnRestar.setVisibility(View.INVISIBLE);
                ivOpciones.setImageDrawable(getResources().getDrawable(images[cont]));
                ivOpciones.setTag("res/drawable/img"+cont+"_"+Numero2+".png");
                //Toast.makeText(getApplicationContext(), "Cont"+cont, Toast.LENGTH_LONG).show();
            }
        });


        btnRestar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cont--;
                if (cont == 1) btnRestar.setVisibility(View.VISIBLE);
                if (cont == 0) btnRestar.setVisibility(View.INVISIBLE);
                if(cont==Numero2)btnSumar.setVisibility(View.INVISIBLE);
                if(cont==Numero2-1)btnSumar.setVisibility(View.VISIBLE);
                ivOpciones.setImageDrawable(getResources().getDrawable(images[cont]));
                ivOpciones.setTag("res/drawable/img"+Numero1+"_"+cont+".png");
                //Toast.makeText(getApplicationContext(), cont, Toast.LENGTH_LONG).show();
            }
        });

        btnResponder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String correcta="res/drawable/img"+Numero1+"_"+Numero2+".png";
                if(correcta.equals(ivOpciones.getTag().toString())){
                    Toast.makeText(getApplicationContext(), "Correcta", Toast.LENGTH_LONG).show();
                    puntaje++;
                }else{
                    Toast.makeText(getApplicationContext(), "InCorrecta", Toast.LENGTH_LONG).show();
                }
                contador++;
                ReiniciarActivity(puntaje,contador);
            }
        });

    }

    private void generarEjercicio() {
        Random r = new Random();
        Numero2 = r.nextInt(10 - 2 + 1) + 2;
        Numero1 = r.nextInt(Numero2- 1 + 1) + 1;
        tvGnum1.setText(Numero1 + "");
        tvGnum2.setText(Numero2 + "");
    }

    private void Conectar(){
        ivOpciones=findViewById(R.id.ivOpciones);
        btnRestar=findViewById(R.id.btnRestar);
        btnSumar=findViewById(R.id.btnSumar);
        tvGnum1=findViewById(R.id.tvGnum1);
        tvGnum2=findViewById(R.id.tvGnum2);
        btnResponder=findViewById(R.id.btnResponder);
    }

    private void Imagenes() {
        images2= new Integer[]{R.drawable.img0_2,R.drawable.img1_2,R.drawable.img2_2};
        images3= new Integer[]{R.drawable.img0_3,R.drawable.img1_3,R.drawable.img2_3,R.drawable.img3_3};
        images4= new Integer[]{R.drawable.img0_4,R.drawable.img1_4,R.drawable.img2_4,R.drawable.img3_4,R.drawable.img4_4};
        images5= new Integer[]{R.drawable.img0_5,R.drawable.img1_5,R.drawable.img2_5,R.drawable.img3_5,R.drawable.img4_5,R.drawable.img5_5};
        images6= new Integer[]{R.drawable.img0_6,R.drawable.img1_6,R.drawable.img2_6,R.drawable.img3_6,R.drawable.img4_6,R.drawable.img5_6,R.drawable.img6_6};
        images7= new Integer[]{R.drawable.img0_7,R.drawable.img1_7,R.drawable.img2_7,R.drawable.img3_7,R.drawable.img4_7,R.drawable.img5_7,R.drawable.img6_7,R.drawable.img7_7};
        images8= new Integer[]{R.drawable.img0_8,R.drawable.img1_8,R.drawable.img2_8,R.drawable.img3_8,R.drawable.img4_8,R.drawable.img5_8,R.drawable.img6_8,R.drawable.img7_8,R.drawable.img8_8};
        images9= new Integer[]{R.drawable.img0_9,R.drawable.img1_9,R.drawable.img2_9,R.drawable.img3_9,R.drawable.img4_9,R.drawable.img5_9,R.drawable.img6_9,R.drawable.img7_9,R.drawable.img8_9,R.drawable.img9_9};
        images10= new Integer[]{R.drawable.img0_10,R.drawable.img1_10,R.drawable.img2_10,R.drawable.img3_10,R.drawable.img4_10,R.drawable.img5_10,R.drawable.img6_10,R.drawable.img7_10,R.drawable.img8_10,R.drawable.img9_10,R.drawable.img10_10};
    }
    private void ReiniciarActivity(int puntaje,int cont){
        Intent intent = getIntent();
        intent.putExtra("Puntaje",puntaje);
        intent.putExtra("Contador",cont);
        finish();
        startActivity(intent);
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
                                AgregarPuntaje(codigo,email,puntaje,"Graficos",fecha);
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
}
