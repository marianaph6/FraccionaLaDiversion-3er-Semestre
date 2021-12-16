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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MostrarEstudiantesActivity extends AppCompatActivity {

    ListView lvEstudiates;
    Button btnInfo;
    TextView tvInfo;
    ArrayList<String> listado;
    String Id, linea = "";
    FirebaseDatabase database;
    DatabaseReference myRef;
    ArrayList<String> estudiantes=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_estudiantes);

        database = FirebaseDatabase.getInstance();
        myRef=database.getReference();
        lvEstudiates=findViewById(R.id.lvProgreso);
        final String Id=getIntent().getStringExtra("IdGrupo");
        final String IdProfesor = getIntent().getStringExtra("IdProfesor");
        //Toast.makeText(getApplicationContext(), Id, Toast.LENGTH_SHORT).show();
        cargar();
        llenarTextView(Id);
        ListaEstudiantes(Id);
        lvEstudiates.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item=lvEstudiates.getItemAtPosition(position).toString();
                String [] est = item.split("\n");
                String estudiante = "";
                    estudiante += est[0].trim();
                //Toast.makeText(getApplicationContext(), estudiante, Toast.LENGTH_LONG).show();
                Intent I = new Intent(getApplicationContext(), MostrarProgresoActivity.class);
                I.putExtra("IdEstudiante", estudiante);
                startActivity(I);
            }
        });
        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent I = new Intent(getApplicationContext(), InfoGrupoActivity.class);
                I.putExtra("Id", Id);
                I.putExtra("Info", linea);
                I.putExtra("IdProfesor", IdProfesor);
                startActivity(I);
            }
        });
    }

    private void cargar() {
        lvEstudiates = findViewById(R.id.lvEstudiantes);
        btnInfo = findViewById(R.id.btnInfoGrupo);
    }

    private void llenarTextView(final String idProfe){
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
                                tvInfo.setText(line);
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

    private void ListaEstudiantes(final String idGrupo){
        final String line;
        myRef.child("Estudiante").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(final DataSnapshot snapshot: dataSnapshot.getChildren()){
                    myRef.child("Estudiante").child(snapshot.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Estudiante estudiante=snapshot.getValue(Estudiante.class);
                            String documento=estudiante.getDocumento();
                            String nombre=estudiante.getPrimerNombre();
                            String apellido=estudiante.getPrimerApellido();
                            String email=estudiante.getEmail();
                            String idgrupo=estudiante.getIdGrupo();

                            if(idGrupo.equals(idgrupo)){
                                String line= email + "\n Documento: " + documento + "\n Nombre: " + nombre
                                        + "\n Apellido: " + apellido;
                                //Toast.makeText(getApplicationContext(), ""+line, Toast.LENGTH_LONG).show();
                                estudiantes.add(line);
                                ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(),
                                        android.R.layout.simple_list_item_1,estudiantes);
                                lvEstudiates.setAdapter(adapter);
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
