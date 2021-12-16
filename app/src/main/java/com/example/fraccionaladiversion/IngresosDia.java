package com.example.fraccionaladiversion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class IngresosDia extends AppCompatActivity {

    String Id, Fecha;
    FirebaseDatabase database;
    DatabaseReference myRef;
    ArrayList<String> estudiantes=new ArrayList<>();
    ListView lvIngresos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingresos_dia);
        database = FirebaseDatabase.getInstance();
        myRef=database.getReference();
        Id = getIntent().getStringExtra("Id");
        Fecha = getIntent().getStringExtra("Fecha");
        lvIngresos=findViewById(R.id.lvIngresos);
        Cargar(Id,Fecha);
    }

    private void Cargar(final String idgrupo,final String date){
        final String line;
        myRef.child("Progreso").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(final DataSnapshot snapshot: dataSnapshot.getChildren()){
                    myRef.child("Progreso").child(snapshot.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Historial h=snapshot.getValue(Historial.class);
                            String fecha=h.getFecha();
                            String grupo=h.getIdGrupo();
                            String tipo=h.getTipoJuego();
                            int puntaje=h.getPuntaje();
                            String estudiante=h.getIdEstudiante();

                            if(idgrupo.equals(grupo) && date.equals(fecha)){
                                String line="Estudiante: " +estudiante + "\nFecha: " + fecha + "\nTipo de juego: " + tipo
                                        + "\nPuntaje: " + puntaje;
                                //Toast.makeText(getApplicationContext(), ""+line, Toast.LENGTH_LONG).show();
                                estudiantes.add(line);
                                ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(),
                                        android.R.layout.simple_list_item_1,estudiantes);
                                lvIngresos.setAdapter(adapter);
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
