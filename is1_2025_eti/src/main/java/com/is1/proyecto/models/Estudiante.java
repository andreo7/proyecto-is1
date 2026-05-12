package com.is1.proyecto.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

@Table("estudiante")
public class Estudiante extends Model {

    public Persona getPerson() {
        return parent(Persona.class);
    }

    public void setPerson(Persona persona) {
        set("id_person", persona.getId());
    }

    public int getNroAlumno() {
        return getInteger("nro_alumno");
    }

    public void setNroAlumno(int nroAlumno) {
        set("nro_alumno", nroAlumno);
    }

    public String getEstadoCarrera() {
        return getString("estado_carrera");
    }

    public void setEstadoCarrera(String estadoCarrera) {
        set("estado_carrera", estadoCarrera);
    }

    // Métodos puente
    public String getNombre() {
        return getPerson().getNombre();
    }

    public String getApellido() {
        return getPerson().getApellido();
    }

    public String getDni() {
        return getPerson().getDni();
    }

    public String getContacto() {
        return getPerson().getContacto();
    }

    public String getDireccion() {
        return getPerson().getDireccion();
    }
}