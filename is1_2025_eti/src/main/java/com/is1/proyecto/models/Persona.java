package com.is1.proyecto.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

@Table("persona")
public class Persona extends Model{

    public String getNombre() {
        return getString("nombre"); 
    }

    public void setNombre(String name) {
        set("nombre", name);
    }

    public String getDni() {
        return getString("dni"); 
    }

    public void setDni(String dni) {
        set("dni", dni);
    }

    public String getApellido() {
        return getString("apellido"); 
    }

    public void setApellido(String apellido) {
        set("apellido", apellido); 
    }

    public void setDireccion(String direccion){
        set("direccion", direccion);
    }

    public String getDireccion(){
        return getString("direccion");
    }

    public void setContacto(String contacto){
        set("contacto", contacto);
    }

    public String getContacto(){
        return getString("contacto");
    }
}
