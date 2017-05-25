package com.example.felixadrian.objectos;

/**
 * Created by Felix Adrian on 25/05/2017.
 */

public class Falta {
    private int idUsuario;
    private String motivoFalta;
    private String observacionFalta;

    public Falta(){

    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getMotivoFalta() {
        return motivoFalta;
    }

    public void setMotivoFalta(String motivoFalta) {
        this.motivoFalta = motivoFalta;
    }

    public String getObservacionFalta() {
        return observacionFalta;
    }

    public void setObservacionFalta(String observacionFalta) {
        this.observacionFalta = observacionFalta;
    }
}
