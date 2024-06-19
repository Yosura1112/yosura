package com.example.proyectoufc.clases;

import java.io.Serializable;
import java.util.Date;

public class Paciente implements Serializable {
    private int id;

    private String dni_paciente;
    private String nombres;
    private String apellidos;
    private String correo_electronico;
    private String contrasena;
    private int id_distrito;
    private String direcion;
    private Date fecha_nac;

    public Paciente() {
    }

    public Paciente(int id, String dni_paciente, String nombres, String apellidos, String correo_electronico, String contrasena, int id_distrito, String direcion, Date fecha_nac) {
        this.id = id;
        this.dni_paciente = dni_paciente;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.correo_electronico = correo_electronico;
        this.contrasena = contrasena;
        this.id_distrito = id_distrito;
        this.direcion = direcion;
        this.fecha_nac = fecha_nac;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDni_paciente() {
        return dni_paciente;
    }

    public void setDni_paciente(String dni_paciente) {
        this.dni_paciente = dni_paciente;
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

    public String getCorreo_electronico() {
        return correo_electronico;
    }

    public void setCorreo_electronico(String correo_electronico) {
        this.correo_electronico = correo_electronico;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public int getId_distrito() {
        return id_distrito;
    }

    public void setId_distrito(int id_distrito) {
        this.id_distrito = id_distrito;
    }

    public String getDirecion() {
        return direcion;
    }

    public void setDirecion(String direcion) {
        this.direcion = direcion;
    }

    public Date getFecha_nac() {
        return fecha_nac;
    }

    public void setFecha_nac(Date fecha_nac) {
        this.fecha_nac = fecha_nac;
    }
}
