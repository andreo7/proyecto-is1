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
     * Retorna todos los docentes registrados como lista de mapas
     * lista para ser consumida directamente por Mustache.
     *
     * Usa .include(Persona.class) para cargar todas las personas
     * en una sola consulta SQL adicional (evita el problema N+1).
     * Requiere que Docente tenga declarado @BelongsTo con foreignKeyName = "id_person".
     *
     * Cada mapa contiene: id, nombre, apellido, dni, matricula, contacto.
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
            result.add(row);
        }

        return result;
    }

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
}