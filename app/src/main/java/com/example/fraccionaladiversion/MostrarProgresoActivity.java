package com.example.fraccionaladiversion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MostrarProgresoActivity extends AppCompatActivity {

    ListView lvProgreso;
    ArrayList<String> listado;
    FirebaseDatabase database;
    DatabaseReference myRef;
    ArrayList<String> progreso=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_progreso);
        database = FirebaseDatabase.getInstance();
        myRef=database.getReference();
        lvProgreso=findViewById(R.id.lvProgreso);
        final String Id=getIntent().getStringExtra("IdEstudiante");
        CargarGrupos(Id);

    }

    private void CargarGrupos(final String idg){
        final String line;
        myRef.child("Progreso").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(final DataSnapshot snapshot: dataSnapshot.getChildren()){
                    myRef.child("Progreso").child(snapshot.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Historial historial=snapshot.getValue(Historial.class);

                            String idEstudiante=historial.getIdEstudiante();
                            int Puntaje=historial.getPuntaje();
                            String idGrupo=historial.getIdGrupo();
                            String TipoJuego=historial.getTipoJuego();
                            String Fecha=historial.getFecha();

                            if(idEstudiante.equals(idg)){
                                String line= " IdEstudiante: " + idEstudiante + "\n Puntaje: " +Puntaje + "\n Tipo de Juego: " + TipoJuego
                                        + "\n Fecha: " + Fecha+"\n IdGrupo: " +idGrupo;
                                //Toast.makeText(getApplicationContext(), ""+line, Toast.LENGTH_LONG).show();
                                progreso.add(line);
                                ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(),
                                        android.R.layout.simple_list_item_1,progreso);
                                lvProgreso.setAdapter(adapter);
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
