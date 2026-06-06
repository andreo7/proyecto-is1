package com.is1.proyecto.service;

import com.is1.proyecto.exceptions.ServiceException;
import com.is1.proyecto.models.Materia;

public class MateriaService {

    public Materia createMateria(int codigo, String nombre, String descripcion) {
        if (Materia.findFirst("codigo = ?", codigo) != null) {
            throw new ServiceException("El código " + codigo + " ya está registrado.");
        }

        Materia materia = new Materia();
        materia.set("nombre",      nombre.trim());
        materia.set("descripcion", descripcion.trim());
        materia.set("codigo",      codigo);
        materia.saveIt();

        return materia;
    }
}