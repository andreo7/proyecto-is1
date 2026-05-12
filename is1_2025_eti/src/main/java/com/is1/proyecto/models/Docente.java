package com.is1.proyecto.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.Table;

@Table("docente")
@BelongsTo(parent = Persona.class, foreignKeyName = "id_person")
public class Docente extends Model {

    public Persona getPerson() {
        return parent(Persona.class);
    }

    public void setPerson(Persona persona) {
        set("id_person", persona.getId());
    }

    public void setMatricula(int matricula) {
        set("matricula", matricula);
    }

    public int getMatricula() {
        return getInteger("matricula");
    }

    // Métodos puente //

    public String getNombre() {
        return getPerson().getNombre();
    }

    public String getApellido() {
        return getPerson().getApellido();
    }

    public String getDireccion() {
        return getPerson().getDireccion();
    }

    public String getContacto() {
        return getPerson().getContacto();
    }
}