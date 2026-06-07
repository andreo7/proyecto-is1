package com.is1.proyecto.service;

import com.is1.proyecto.exceptions.ServiceException;
import com.is1.proyecto.models.Docente;
import com.is1.proyecto.models.DocenteMateria;
import com.is1.proyecto.models.Persona;

import java.util.List;

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
     * Retorna la lista completa de docentes registrados.
     *
     * @return lista de Docente (puede estar vacía, nunca null)
     */
    public List<Docente> getAllDocentes() {
        return Docente.findAll().include(Persona.class).load();
    }

    /**
     * Elimina un docente y su Persona asociada dado el ID del docente.
     *
     * Regla de negocio: no se puede eliminar un docente que tenga
     * materias asociadas en la tabla intermedia docente_materia.
     *
     * @param docenteId ID del docente a eliminar
     * @throws ServiceException si el docente no existe o tiene materias asociadas
     */
    public void deleteDocente(int docenteId) {
        Docente docente = Docente.findById(docenteId);
        if (docente == null) {
            throw new ServiceException("El docente con ID " + docenteId + " no existe.");
        }

        long materiasAsociadas = DocenteMateria.count("id_docente = ?", docenteId);
        if (materiasAsociadas > 0) {
            throw new ServiceException(
                    "No se puede eliminar el docente porque tiene " + materiasAsociadas +
                            " materia(s) asociada(s). Desasócielas primero."
            );
        }

        Persona persona = docente.getPerson();

        docente.delete();

        if (persona != null) {
            persona.delete();
        }
    }
}