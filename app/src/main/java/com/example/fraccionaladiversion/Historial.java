package com.example.fraccionaladiversion;

public class Historial {
    String IdGrupo;
    String IdEstudiante;
    int Puntaje;
    String TipoJuego;
    String Fecha;

    public Historial() {
    }

    public Historial(String idGrupo, String idEstudiante, int puntaje, String tipoJuego, String fecha) {
        IdGrupo = idGrupo;
        IdEstudiante = idEstudiante;
        Puntaje = puntaje;
        TipoJuego = tipoJuego;
        Fecha = fecha;
    }

    public String getIdGrupo() {
        return IdGrupo;
    }

    public void setIdGrupo(String idGrupo) {
        IdGrupo = idGrupo;
    }

    public String getIdEstudiante() {
        return IdEstudiante;
    }

    public void setIdEstudiante(String idEstudiante) {
        IdEstudiante = idEstudiante;
    }

    public int getPuntaje() {
        return Puntaje;
    }

    public void setPuntaje(int puntaje) {
        Puntaje = puntaje;
    }

    public String getTipoJuego() {
        return TipoJuego;
    }

    public void setTipoJuego(String tipoJuego) {
        TipoJuego = tipoJuego;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }
}



