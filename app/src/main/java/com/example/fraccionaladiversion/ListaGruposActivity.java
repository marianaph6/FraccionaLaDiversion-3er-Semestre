package com.example.fraccionaladiversion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListaGruposActivity extends AppCompatActivity {

    ListView lvGrupos;
    FirebaseDatabase database;
    DatabaseReference myRef;
    String linea="";
    ArrayList<String> grupos=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_grupos);
        database = FirebaseDatabase.getInstance();
        myRef=database.getReference();
        lvGrupos=findViewById(R.id.lvGrupos);
        final String Id=getIntent().getStringExtra("IdProfesor");
        CargarGrupos(Id);
       // Cargar();
        lvGrupos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item=lvGrupos.getItemAtPosition(position).toString();
                String [] est = item.split("\n");
                String grupo = "";
                grupo += est[0].trim();
                //Toast.makeText(getApplicationContext(), grupo, Toast.LENGTH_LONG).show();
                Intent I = new Intent(getApplicationContext(), MostrarEstudiantesActivity.class);
                I.putExtra("IdGrupo", grupo);
                I.putExtra("IdProfesor", Id);
                startActivity(I);
            }
        });
    }


    private void CargarGrupos(final String idProfe){
        final String line;
        myRef.child("Grupo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(final DataSnapshot snapshot: dataSnapshot.getChildren()){
                    myRef.child("Grupo").child(snapshot.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Grupo grupo=snapshot.getValue(Grupo.class);
                            String codigo=grupo.getCodigo();
                            String colegio=grupo.getColegio();
                            String grado=grupo.getGrado();
                            String idprofe=grupo.getIdProfesor();

                            if(idProfe.equals(idprofe)){
                                String line= codigo + "\n Colegio: " + colegio + "\n Grado: " + grado
                                        + "\n Idprofesor: " + idprofe;
                                //Toast.makeText(getApplicationContext(), ""+line, Toast.LENGTH_LONG).show();
                                grupos.add(line);
                                ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(),
                                        android.R.layout.simple_list_item_1,grupos);
                                lvGrupos.setAdapter(adapter);
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
