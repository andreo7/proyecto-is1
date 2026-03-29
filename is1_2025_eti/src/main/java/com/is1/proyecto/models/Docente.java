package com.is1.proyecto.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

@Table("docente")
public class Docente extends Model{
    public Persona getPerson(){
        return parent(Persona.class);
    }

    public void setPerson(Persona persona){
        set("id_person", persona.getId());
    }

    public void setMatricula(int matricula){
        set("matricula",matricula);
    }

    public int getMatricula(){
        return getInteger("matricula");
    }

    //Metodos Puentes//

    public String getNombre(){
        return getPerson().getNombre();
    }

    public String getApellido(){
        return getPerson().getApellido();
    }

    public String getDireccion(){
        return getPerson().getDireccion();
    }

    public String getContacto(){
        return getPerson().getContacto();
    }
}
