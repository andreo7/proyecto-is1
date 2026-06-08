package com.is1.proyecto.service;

import com.is1.proyecto.exceptions.ServiceException;
import com.is1.proyecto.models.Docente;
import com.is1.proyecto.models.Persona;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Contiene la lógica de negocio relacionada a los docentes.
 * Esta clase NO sabe nada de HTTP (no recibe Request ni Response de Spark).
 * Lanza ServiceException ante violaciones de reglas de dominio.
 */
public class DocenteService {

    /**
     * Crea y persiste un nuevo docente junto con su Persona asociada.
     * Verifica unicidad de DNI, contacto y matrícula antes de persistir.
     *
     * @throws ServiceException si algún campo único ya está registrado en la base de datos
     */
    public Docente createDocente(String nombre, String apellido, String dniStr,
                                 String contacto, String direccion, String matriculaStr) {

        if (Persona.findFirst("dni = ?", dniStr.trim()) != null) {
            throw new ServiceException("El DNI " + dniStr.trim() + " ya está registrado.");
        }
        if (Persona.findFirst("contacto = ?", contacto.trim()) != null) {
            throw new ServiceException("El contacto " + contacto.trim() + " ya está registrado.");
        }
        if (Docente.findFirst("matricula = ?", matriculaStr.trim()) != null) {
            throw new ServiceException("La matrícula " + matriculaStr.trim() + " ya está registrada.");
        }

        Persona persona = new Persona();
        persona.set("nombre",    nombre.trim());
        persona.set("apellido",  apellido.trim());
        persona.set("dni",       dniStr.trim());
        persona.set("contacto",  contacto.trim());
        persona.set("direccion", direccion.trim());
        persona.saveIt();

        Docente docente = new Docente();
        docente.set("matricula", Integer.parseInt(matriculaStr.trim()));
        docente.setPerson(persona);
        docente.saveIt();

        return docente;
    }

    /**
     * Retorna la lista completa de docentes como mapas listos para Mustache.
     */
    public List<Map<String, Object>> getAllDocentes() {
        List<Docente> docentes = Docente.findAll().include(Persona.class).load();
        List<Map<String, Object>> result = new ArrayList<>();

        for (Docente d : docentes) {
            Persona p = d.getPerson();
            Map<String, Object> row = new HashMap<>();
            row.put("id",        d.getId());
            row.put("nombre",    p.getNombre());
            row.put("apellido",  p.getApellido());
            row.put("dni",       p.getDni());
            row.put("matricula", d.getMatricula());
            row.put("contacto",  p.getContacto());
            row.put("direccion", p.getDireccion());
            result.add(row);
        }

        return result;
    }

    /**
     * Busca un docente por ID y retorna sus datos como mapa para Mustache.
     *
     * @throws ServiceException si el ID no corresponde a ningún docente.
     */
    public Map<String, Object> findById(int id) {
        Docente docente = Docente.findById(id);
        if (docente == null) {
            throw new ServiceException("El docente con ID " + id + " no existe.");
        }

        Persona persona = docente.getPerson();
        if (persona == null) {
            throw new ServiceException("Error interno: no se encontró la persona asociada al docente.");
        }

        Map<String, Object> data = new HashMap<>();
        data.put("id",        docente.getId());
        data.put("nombre",    persona.getNombre());
        data.put("apellido",  persona.getApellido());
        data.put("dni",       persona.getDni());
        data.put("contacto",  persona.getContacto());
        data.put("direccion", persona.getDireccion());
        data.put("matricula", docente.getMatricula());
        return data;
    }

    /**
     * Actualiza los datos de un docente y su Persona asociada.
     *
     * @throws ServiceException si el ID no existe o un campo único ya está en uso por otro registro.
     */
    public Docente update(int id, String nombre, String apellido, String dniStr,
                          String contacto, String direccion, String matriculaStr) {

        Docente docente = Docente.findById(id);
        if (docente == null) {
            throw new ServiceException("El docente con ID " + id + " no existe.");
        }

        Persona persona = docente.getPerson();
        if (persona == null) {
            throw new ServiceException("Error interno: no se encontró la persona asociada al docente.");
        }

        Persona existingDni = Persona.findFirst("dni = ?", dniStr.trim());
        if (existingDni != null && !existingDni.getId().equals(persona.getId())) {
            throw new ServiceException("El DNI " + dniStr.trim() + " ya está registrado.");
        }

        Persona existingContacto = Persona.findFirst("contacto = ?", contacto.trim());
        if (existingContacto != null && !existingContacto.getId().equals(persona.getId())) {
            throw new ServiceException("El contacto " + contacto.trim() + " ya está registrado.");
        }

        Docente existingMatricula = Docente.findFirst("matricula = ?", matriculaStr.trim());
        if (existingMatricula != null && !existingMatricula.getId().equals(docente.getId())) {
            throw new ServiceException("La matrícula " + matriculaStr.trim() + " ya está registrada.");
        }

        persona.set("nombre",    nombre.trim());
        persona.set("apellido",  apellido.trim());
        persona.set("dni",       dniStr.trim());
        persona.set("contacto",  contacto.trim());
        persona.set("direccion", direccion.trim());
        persona.saveIt();

        docente.set("matricula", Integer.parseInt(matriculaStr.trim()));
        docente.saveIt();

        return docente;
    }

    /**
     * Elimina un docente y su Persona asociada.
     * El orden de borrado (Docente primero, luego Persona) es obligatorio
     * para respetar la restricción de clave foránea.
     *
     * @throws ServiceException si el docente no existe.
     */
    public void deleteDocente(int docenteId) {
        Docente docente = Docente.findById(docenteId);
        if (docente == null) {
            throw new ServiceException("El docente con ID " + docenteId + " no existe.");
        }

        Persona persona = docente.getPerson();
        docente.delete();
        if (persona != null) {
            persona.delete();
        }
    }
}