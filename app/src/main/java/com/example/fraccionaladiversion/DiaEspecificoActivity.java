package com.example.fraccionaladiversion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class DiaEspecificoActivity extends AppCompatActivity {

    String Id;
    Button btnRevisar;
    ListView lvMostrar;
    DatePicker dpFecha;
    ArrayList<String> listado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dia_especifico);
        btnRevisar = findViewById(R.id.btnRevisar);
        lvMostrar = findViewById(R.id.lvIngresos);
        dpFecha = findViewById(R.id.dpCalendario);
        Id = getIntent().getStringExtra("Id");
        btnRevisar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fecha = dpFecha.getDayOfMonth() + "/" + (dpFecha.getMonth()+1) + "/" + dpFecha.getYear();
                Intent I = new Intent(getApplicationContext(), IngresosDia.class);
                I.putExtra("Id", Id);
                I.putExtra("Fecha", fecha);
                startActivity(I);
            }
        });

    }

}
