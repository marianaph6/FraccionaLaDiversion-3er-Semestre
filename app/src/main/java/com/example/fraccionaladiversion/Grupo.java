package com.example.fraccionaladiversion;

public class Grupo {
    String Colegio;
    String Grado;
    String Codigo;
    String Idprofesor;
    String Uid;

    public Grupo() {
    }

    public Grupo(String colegio, String grado, String codigo, String idprofesor, String uid) {
        Colegio = colegio;
        Grado = grado;
        Codigo = codigo;
        Idprofesor = idprofesor;
        Uid = uid;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getColegio() {
        return Colegio;
    }

    public void setColegio(String colegio) {
        Colegio = colegio;
    }

    public String getGrado() {
        return Grado;
    }

    public void setGrado(String grado) {
        Grado = grado;
    }

    public String getCodigo() {
        return Codigo;
    }

    public void setCodigo(String codigo) {
        Codigo = codigo;
    }

    public String getIdProfesor() {
        return Idprofesor;
    }

    public void setIdProfesor(String idProfesor) {
        Idprofesor = idProfesor;
    }
}
