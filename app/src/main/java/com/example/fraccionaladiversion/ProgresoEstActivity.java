package com.example.fraccionaladiversion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProgresoEstActivity extends AppCompatActivity {

    ListView lvProgresoEst;
    String Id;
    ArrayList<String> listado=new ArrayList<>();
    TextView tvRacha;
    FirebaseDatabase database;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progreso_est);
        database = FirebaseDatabase.getInstance();
        myRef=database.getReference();
        lvProgresoEst = findViewById(R.id.lvProgresoEstudiante);
        Id = getIntent().getStringExtra("IdEstudiante");
        Cargar(Id);
    }

    private void Cargar(final String idest){
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
                            int puntaje=h.getPuntaje();
                            String tipo=h.getTipoJuego();
                            String id=h.getIdEstudiante();
                            if(idest.equals(id)){
                                String line= "Tipo de juego: "+tipo + "\nFecha: " + fecha + "\nPuntaje: " + puntaje;
                                listado.add(line);
                                ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(),
                                        android.R.layout.simple_list_item_1,listado);
                                lvProgresoEst.setAdapter(adapter);
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
