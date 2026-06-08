package com.is1.proyecto.service;

import com.is1.proyecto.exceptions.ServiceException;
import com.is1.proyecto.models.Carrera;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Contiene la lógica de negocio relacionada a las carreras del sistema.
 * Esta clase NO sabe nada de HTTP (no recibe Request ni Response de Spark).
 * Lanza ServiceException ante violaciones de reglas de dominio.
 */
public class CarreraService {

    /**
     * Crea y persiste una nueva carrera.
     * Verifica unicidad de código y nombre antes de persistir.
     *
     * @throws ServiceException si el código o nombre ya están registrados.
     */
    public Carrera createCarrera(int codigo, String nombre, String descripcion) {
        if (Carrera.findFirst("codigo = ?", codigo) != null) {
            throw new ServiceException("El código " + codigo + " ya está registrado.");
        }
        if (Carrera.findFirst("nombre = ?", nombre.trim()) != null) {
            throw new ServiceException("La carrera '" + nombre.trim() + "' ya está registrada.");
        }

        Carrera carrera = new Carrera();
        carrera.set("codigo",      codigo);
        carrera.set("nombre",      nombre.trim());
        carrera.set("descripcion", descripcion.trim());
        carrera.saveIt();

        return carrera;
    }

    /**
     * Retorna todas las carreras como lista de mapas lista para Mustache.
     */
    public List<Map<String, Object>> getAllCarreras() {
        List<Carrera> carreras = Carrera.findAll().load();
        List<Map<String, Object>> result = new ArrayList<>();

        for (Carrera c : carreras) {
            Map<String, Object> row = new HashMap<>();
            row.put("id",          c.getId());
            row.put("codigo",      c.getCodigo());
            row.put("nombre",      c.getNombre());
            row.put("descripcion", c.getDescripcion());
            result.add(row);
        }

        return result;
    }

    /**
     * Busca una carrera por ID y retorna sus datos como mapa para Mustache.
     *
     * @throws ServiceException si el ID no corresponde a ninguna carrera.
     */
    public Map<String, Object> findById(int id) {
        Carrera carrera = Carrera.findById(id);
        if (carrera == null) {
            throw new ServiceException("La carrera con id " + id + " no existe.");
        }

        Map<String, Object> data = new HashMap<>();
        data.put("id",          carrera.getId());
        data.put("codigo",      carrera.getCodigo());
        data.put("nombre",      carrera.getNombre());
        data.put("descripcion", carrera.getDescripcion());
        return data;
    }

    /**
     * Actualiza los datos de una carrera.
     * Verifica unicidad de código y nombre excluyendo el registro actual.
     *
     * @throws ServiceException si el ID no existe o un campo único ya está en uso por otro registro.
     */
    public Carrera update(int id, int codigo, String nombre, String descripcion) {
        Carrera carrera = Carrera.findById(id);
        if (carrera == null) {
            throw new ServiceException("La carrera con id " + id + " no existe.");
        }

        Carrera existingCodigo = Carrera.findFirst("codigo = ?", codigo);
        if (existingCodigo != null && !existingCodigo.getId().equals(carrera.getId())) {
            throw new ServiceException("El código " + codigo + " ya está registrado.");
        }

        Carrera existingNombre = Carrera.findFirst("nombre = ?", nombre.trim());
        if (existingNombre != null && !existingNombre.getId().equals(carrera.getId())) {
            throw new ServiceException("La carrera '" + nombre.trim() + "' ya está registrada.");
        }

        carrera.set("codigo",      codigo);
        carrera.set("nombre",      nombre.trim());
        carrera.set("descripcion", descripcion.trim());
        carrera.saveIt();

        return carrera;
    }

    /**
     * Elimina una carrera por ID.
     *
     * @throws ServiceException si la carrera no existe.
     */
    public void delete(int id) {
        Carrera carrera = Carrera.findById(id);
        if (carrera == null) {
            throw new ServiceException("La carrera con id " + id + " no existe.");
        }
        carrera.delete();
    }
}