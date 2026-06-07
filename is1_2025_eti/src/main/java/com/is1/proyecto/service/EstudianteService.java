package com.is1.proyecto.service;

import com.is1.proyecto.exceptions.ServiceException;
import com.is1.proyecto.models.Estudiante;
import com.is1.proyecto.models.Persona;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Contiene la lógica de negocio relacionada a los estudiantes del sistema.
 * Esta clase NO sabe nada de HTTP (no recibe Request ni Response de Spark).
 * Lanza ServiceException ante violaciones de reglas de dominio.
 */
public class EstudianteService {

    /**
     * Retorna todos los estudiantes como lista de mapas lista para Mustache.
     * Usa .include(Persona.class) para cargar todas las personas en una sola
     * consulta adicional, evitando el problema N+1.
     * Requiere @BelongsTo(parent = Persona.class, foreignKeyName = "id_person")
     * en el modelo Estudiante.
     */
    public List<Map<String, Object>> getAllEstudiantes() {
        List<Estudiante> estudiantes = Estudiante.findAll().include(Persona.class).load();
        List<Map<String, Object>> result = new ArrayList<>();

        for (Estudiante e : estudiantes) {
            Persona p = e.getPerson();
            Map<String, Object> row = new HashMap<>();
            row.put("id",            e.getId());
            row.put("nombre",        p.getNombre());
            row.put("apellido",      p.getApellido());
            row.put("dni",           p.getDni());
            row.put("contacto",      p.getContacto());
            row.put("direccion",     p.getDireccion());
            row.put("nroAlumno",     e.getNroAlumno());
            row.put("estadoCarrera", e.getEstadoCarrera());
            result.add(row);
        }

        return result;
    }

    /**
     * Crea y persiste un nuevo estudiante junto con su Persona asociada.
     * Verifica unicidad de DNI, contacto y nroAlumno antes de persistir.
     *
     * @throws ServiceException si algún campo único ya está registrado.
     */
    public Estudiante createEstudiante(String nombre, String apellido, String dniStr,
                                       String contacto, String direccion,
                                       String nroAlumnoStr, String estadoCarrera) {

        if (Persona.findFirst("dni = ?", dniStr.trim()) != null) {
            throw new ServiceException("El DNI " + dniStr.trim() + " ya esta registrado.");
        }
        if (Persona.findFirst("contacto = ?", contacto.trim()) != null) {
            throw new ServiceException("El contacto " + contacto.trim() + " ya esta registrado.");
        }

        int nroAlumno = Integer.parseInt(nroAlumnoStr.trim());
        if (Estudiante.findFirst("nro_alumno = ?", nroAlumno) != null) {
            throw new ServiceException("El numero de alumno " + nroAlumno + " ya esta registrado.");
        }

        Persona persona = new Persona();
        persona.set("nombre",    nombre.trim());
        persona.set("apellido",  apellido.trim());
        persona.set("dni",       dniStr.trim());
        persona.set("contacto",  contacto.trim());
        persona.set("direccion", direccion.trim());
        persona.saveIt();

        Estudiante estudiante = new Estudiante();
        estudiante.set("nro_alumno",     nroAlumno);
        estudiante.set("estado_carrera", estadoCarrera.trim());
        estudiante.setPerson(persona);
        estudiante.saveIt();

        return estudiante;
    }

    /**
     * Busca un estudiante por ID y retorna sus datos como mapa para Mustache.
     *
     * @throws ServiceException si el ID no corresponde a ningún estudiante.
     */
    public Map<String, Object> findById(int id) {
        Estudiante estudiante = Estudiante.findById(id);
        if (estudiante == null) {
            throw new ServiceException("El estudiante con id " + id + " no existe.");
        }

        Persona persona = estudiante.getPerson();
        if (persona == null) {
            throw new ServiceException("Error interno: no se encontro la persona asociada al estudiante.");
        }

        Map<String, Object> data = new HashMap<>();
        data.put("id",            estudiante.getId());
        data.put("nombre",        persona.getNombre());
        data.put("apellido",      persona.getApellido());
        data.put("dni",           persona.getDni());
        data.put("contacto",      persona.getContacto());
        data.put("direccion",     persona.getDireccion());
        data.put("nroAlumno",     estudiante.getNroAlumno());
        data.put("estadoCarrera", estudiante.getEstadoCarrera());
        return data;
    }

    /**
     * Actualiza los datos de un estudiante y su Persona asociada.
     * Verifica unicidad excluyendo el registro actual para permitir
     * guardar sin cambiar campos únicos.
     *
     * @throws ServiceException si el ID no existe o un campo único ya está en uso por otro registro.
     */
    public Estudiante update(int id, String nombre, String apellido, String dniStr,
                             String contacto, String direccion,
                             String nroAlumnoStr, String estadoCarrera) {

        Estudiante estudiante = Estudiante.findById(id);
        if (estudiante == null) {
            throw new ServiceException("El estudiante con id " + id + " no existe.");
        }

        Persona persona = estudiante.getPerson();
        if (persona == null) {
            throw new ServiceException("Error interno: no se encontro la persona asociada al estudiante.");
        }

        // Unicidad de DNI excluyendo la persona actual
        Persona existingDni = Persona.findFirst("dni = ?", dniStr.trim());
        if (existingDni != null && !existingDni.getId().equals(persona.getId())) {
            throw new ServiceException("El DNI " + dniStr.trim() + " ya esta registrado.");
        }

        // Unicidad de contacto excluyendo la persona actual
        Persona existingContacto = Persona.findFirst("contacto = ?", contacto.trim());
        if (existingContacto != null && !existingContacto.getId().equals(persona.getId())) {
            throw new ServiceException("El contacto " + contacto.trim() + " ya esta registrado.");
        }

        // Unicidad de nroAlumno excluyendo el estudiante actual
        int nroAlumno = Integer.parseInt(nroAlumnoStr.trim());
        Estudiante existingNro = Estudiante.findFirst("nro_alumno = ?", nroAlumno);
        if (existingNro != null && !existingNro.getId().equals(estudiante.getId())) {
            throw new ServiceException("El numero de alumno " + nroAlumno + " ya esta registrado.");
        }

        persona.set("nombre",    nombre.trim());
        persona.set("apellido",  apellido.trim());
        persona.set("dni",       dniStr.trim());
        persona.set("contacto",  contacto.trim());
        persona.set("direccion", direccion.trim());
        persona.saveIt();

        estudiante.set("nro_alumno",     nroAlumno);
        estudiante.set("estado_carrera", estadoCarrera.trim());
        estudiante.saveIt();

        return estudiante;
    }

    /**
     * Elimina un estudiante y su Persona asociada.
     * El orden de borrado (Estudiante primero, luego Persona) es obligatorio
     * para respetar la restricción de clave foránea de la tabla estudiante.
     *
     * @throws ServiceException si el ID no corresponde a ningún estudiante.
     */
    public void delete(int id) {
        Estudiante estudiante = Estudiante.findById(id);
        if (estudiante == null) {
            throw new ServiceException("El estudiante con id " + id + " no existe.");
        }

        Persona persona = estudiante.getPerson();
        estudiante.delete();
        if (persona != null) {
            persona.delete();
        }
    }
}