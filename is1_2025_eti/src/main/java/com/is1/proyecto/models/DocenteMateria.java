package com.is1.proyecto.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

/**
 * Representa la asociación N a N entre Docente y Materia.
 * La tabla docente_materia actúa como tabla intermedia:
 *   docente_materia(id_docente, id_materia)
 *
 * Se usa directamente para consultar si un docente tiene materias
 * asociadas antes de permitir su baja.
 */
@Table("docente_materia")
public class DocenteMateria extends Model {

    public int getIdDocente() {
        return getInteger("id_docente");
    }

    public void setIdDocente(int idDocente) {
        set("id_docente", idDocente);
    }

    public int getIdMateria() {
        return getInteger("id_materia");
    }

    public void setIdMateria(int idMateria) {
        set("id_materia", idMateria);
    }
}
