package com.example.felixadrian.objectos;

import java.util.Date;

/**
 * Created by Felix Adrian on 27/05/2017.
 */

public class Desertor {

    private int idUsuario;
    private String motivoDertor;
    private String observacionDesrtor;
    private Date fechaDesertor;

    public Desertor() {

    }


    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getMotivoDertor() {
        return motivoDertor;
    }

    public void setMotivoDertor(String motivoDertor) {
        this.motivoDertor = motivoDertor;
    }

    public String getObservacionDesrtor() {
        return observacionDesrtor;
    }

    public void setObservacionDesrtor(String observacionDesrtor) {
        this.observacionDesrtor = observacionDesrtor;
    }

    public Date getFechaDesertor() {
        return fechaDesertor;
    }

    public void setFechaDesertor(Date fechaDesertor) {
        this.fechaDesertor = fechaDesertor;
    }
}
