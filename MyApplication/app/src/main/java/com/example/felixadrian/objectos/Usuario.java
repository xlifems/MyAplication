package com.example.felixadrian.objectos;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Felix Adrian on 22/05/2017.
 */

public class Usuario implements Parcelable {

    private Long id;
    private String nickname;
    private String tidentificacion;
    private String identificacion;
    private String nombres;
    private String apellidos;
    private String departamento;
    private String ciudad;
    private String direccion;
    private String barrio;
    private String telefono;
    private String correo;
    private String password;
    private String tipo;

    public Usuario() {

    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getTidentificacion() {
        return tidentificacion;
    }

    public void setTidentificacion(String tidentificacion) {
        this.tidentificacion = tidentificacion;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getBarrio() {
        return barrio;
    }

    public void setBarrio(String barrio) {
        this.barrio = barrio;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    protected Usuario(Parcel in) {
        id = in.readByte() == 0x00 ? null : in.readLong();
        nickname = in.readString();
        tidentificacion = in.readString();
        identificacion = in.readString();
        nombres = in.readString();
        apellidos = in.readString();
        departamento = in.readString();
        ciudad = in.readString();
        direccion = in.readString();
        barrio = in.readString();
        telefono = in.readString();
        correo = in.readString();
        password = in.readString();
        tipo = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeLong(id);
        }
        dest.writeString(nickname);
        dest.writeString(tidentificacion);
        dest.writeString(identificacion);
        dest.writeString(nombres);
        dest.writeString(apellidos);
        dest.writeString(departamento);
        dest.writeString(ciudad);
        dest.writeString(direccion);
        dest.writeString(barrio);
        dest.writeString(telefono);
        dest.writeString(correo);
        dest.writeString(password);
        dest.writeString(tipo);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Usuario> CREATOR = new Parcelable.Creator<Usuario>() {
        @Override
        public Usuario createFromParcel(Parcel in) {
            return new Usuario(in);
        }

        @Override
        public Usuario[] newArray(int size) {
            return new Usuario[size];
        }
    };
}