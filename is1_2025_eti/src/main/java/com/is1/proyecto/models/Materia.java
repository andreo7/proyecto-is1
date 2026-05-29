package com.is1.proyecto.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

@Table("materia")
public class Materia extends Model {

    public String getNombre() {
        return getString("nombre");
    }

    public void setNombre(String nombre) {
        set("nombre", nombre);
    }

    public String getDescripcion() {
        return getString("descripcion");
    }

    public void setDescripcion(String descripcion) {
        set("descripcion", descripcion);
    }

    public int getCodigo() {
        return getInteger("codigo");
    }

    public void setCodigo(int codigo) {
        set("codigo", codigo);
    }
}