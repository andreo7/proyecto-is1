package com.is1.proyecto.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToParents;
import org.javalite.activejdbc.annotations.Table;

@Table("docente_materia")
@BelongsToParents({
        @BelongsTo(parent = Docente.class, foreignKeyName = "id_docente"),
        @BelongsTo(parent = Materia.class, foreignKeyName = "id_materia")
})
public class DocenteMateria extends Model {

    public Docente getDocente() {
        return parent(Docente.class);
    }

    public void setDocente(Docente docente) {
        set("id_docente", docente.getId());
    }

    public Materia getMateria() {
        return parent(Materia.class);
    }

    public void setMateria(Materia materia) {
        set("id_materia", materia.getId());
    }

    public String getCargo() {
        return getString("cargo");
    }

    public void setCargo(String cargo) {
        set("cargo", cargo);
    }
}