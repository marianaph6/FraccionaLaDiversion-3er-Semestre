package com.example.fraccionaladiversion;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    String Profesor="Create Table Profesor (IdProfesor Text primary key,PrimerNombre Text,SegundoNombre Text,PrimerApellido Text," +
            "SegundoApellido Text,Email Text)";

    String Estudiante="Create Table Estudiante (IdEstudiante Text primary key,PrimerNombre Text,SegundoNombre Text,PrimerApellido Text,SegundoApellido " +
            "Text,Email Text,IdGrupo Text)";

    String Grupo="Create Table Grupo (IdGrupo Text primary key,IdProfesor Text, Colegio Text, Grado Text)";

    String LoginProfesor="Create Table LoginProfesor (Id Integer primary key ,IdProfesor Text,Password Text)";

    String LoginEstudiante="Create Table LoginEstudiante (Id Integer primary key ,IdEstudiante Text,Password Text)";

    String Progreso="Create Table Progreso (IdProgreso Text primary key ,IdEstudiante Text, Puntaje Integer, Fecha Text,TipoJuego Text)";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Profesor);
        db.execSQL(Estudiante);
        db.execSQL(Grupo);
        db.execSQL(LoginProfesor);
        db.execSQL(LoginEstudiante);
        db.execSQL(Progreso);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table Profesor");
        db.execSQL(Profesor);
        db.execSQL("drop table Estudiante");
        db.execSQL(Estudiante);
        db.execSQL("drop table Grupo");
        db.execSQL(Grupo);
        db.execSQL("drop table LoginProfesor");
        db.execSQL(LoginProfesor);
        db.execSQL("drop table LoginEstudiante");
        db.execSQL(LoginEstudiante);
        db.execSQL("drop table Progreso");
        db.execSQL(Progreso);
    }

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
}
