package com.example.fraccionaladiversion;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class RatingActivity extends AppCompatActivity {

    ListView lvRating;
    String Id;
    FirebaseDatabase database;
    DatabaseReference myRef;
    ArrayList<Historial> rating=new ArrayList<>();
    ArrayList<String> puntajes=new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        database = FirebaseDatabase.getInstance();
        myRef=database.getReference();
        lvRating = findViewById(R.id.lvRating);
        Id = getIntent().getStringExtra("IdGrupo");
        rating=new ArrayList<>();
        Cargar(Id);



    }
    private void Cargar(final String idgrupo){
        final String line;
        myRef.child("Progreso").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(final DataSnapshot snapshot: dataSnapshot.getChildren()){
                    myRef.child("Progreso").child(snapshot.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Historial h=snapshot.getValue(Historial.class);
                            if(h.getIdGrupo().equals(idgrupo)){
                                int puntaje=h.getPuntaje();
                                String correo=h.getIdEstudiante();
                                rating.add(h);
                                String linea="Puntaje: " +h.getPuntaje()+ "\n Estudiante: " + h.getIdEstudiante();
                                puntajes.add(linea);
                                ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(),
                                        android.R.layout.simple_list_item_1,puntajes);
                                lvRating.setAdapter(adapter);
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
