package com.example.felixadrian.objectos;

/**
 * Created by Felix Adrian on 31/05/2017.
 */

public class Nivel {
    private int idNivel;
    private String nombreNivel ;
    private int docentesId;

    public Nivel() {
        setIdNivel(0);
        setDocentesId(0);
        setNombreNivel("");
    }


    public int getIdNivel() {
        return idNivel;
    }

    public void setIdNivel(int idNivel) {
        this.idNivel = idNivel;
    }

    public String getNombreNivel() {
        return nombreNivel;
    }

    public void setNombreNivel(String nombreNivel) {
        this.nombreNivel = nombreNivel;
    }

    public int getDocentesId() {
        return docentesId;
    }

    public void setDocentesId(int docentesId) {
        this.docentesId = docentesId;
    }
}
