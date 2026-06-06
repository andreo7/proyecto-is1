package com.is1.proyecto.service;

import com.is1.proyecto.exceptions.ServiceException;
import com.is1.proyecto.models.Materia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public List<Map<String, Object>> getAllMaterias() {
        List<Materia> materias = Materia.findAll().load();
        List<Map<String, Object>> result = new ArrayList<>();

        for (Materia m : materias) {
            Map<String, Object> row = new HashMap<>();
            row.put("id",          m.getId());
            row.put("nombre",      m.getNombre());
            row.put("descripcion", m.getDescripcion());
            row.put("codigo",      m.getCodigo());
            result.add(row);
        }

        return result;
    }
    public void delete(int id) {
        Materia materia = Materia.findById(id);
        if (materia == null) {
            throw new ServiceException("La materia con id " + id + " no existe.");
        }
        materia.delete();
    }

    public Materia update(int id, String nombre, String descripcion, int codigo) {
        Materia materia = Materia.findById(id);
        if (materia == null) {
            throw new ServiceException("La materia con id " + id + " no existe.");
        }

        Materia existing = Materia.findFirst("codigo = ?", codigo);
        if (existing != null && !existing.getId().equals(materia.getId())) {
            throw new ServiceException("El código " + codigo + " ya está registrado.");
        }

        materia.set("nombre",      nombre.trim());
        materia.set("descripcion", descripcion.trim());
        materia.set("codigo",      codigo);
        materia.saveIt();

        return materia;
    }
    public Materia findById(int id) {
        Materia materia = Materia.findById(id);
        if (materia == null) {
            throw new ServiceException("La materia con id " + id + " no existe.");
        }
        return materia;
    }
}