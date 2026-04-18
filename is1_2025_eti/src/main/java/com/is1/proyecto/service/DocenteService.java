package com.is1.proyecto.service;

import com.is1.proyecto.exceptions.ServiceException;
import com.is1.proyecto.models.Docente;
import com.is1.proyecto.models.Persona;

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
     * @param nombre      nombre del docente (ya validado)
     * @param apellido    apellido del docente (ya validado)
     * @param dniStr      DNI como string (ya validado como numérico)
     * @param contacto    email de contacto (ya validado)
     * @param direccion   dirección postal (ya validada)
     * @param matriculaStr número de matrícula como string (ya validado como numérico)
     * @return el Docente recién creado
     * @throws ServiceException si algún campo único ya está registrado en la base de datos
     */
    public Docente createDocente(String nombre, String apellido, String dniStr,
                                 String contacto, String direccion, String matriculaStr) {

        // Verificar unicidad (reglas de negocio / integridad de datos)
        if (Persona.findFirst("dni = ?", dniStr.trim()) != null) {
            throw new ServiceException("El DNI " + dniStr.trim() + " ya está registrado.");
        }

        if (Persona.findFirst("contacto = ?", contacto.trim()) != null) {
            throw new ServiceException("El contacto " + contacto.trim() + " ya está registrado.");
        }

        if (Docente.findFirst("matricula = ?", matriculaStr.trim()) != null) {
            throw new ServiceException("La matrícula " + matriculaStr.trim() + " ya está registrada.");
        }

        // Persistir Persona
        Persona persona = new Persona();
        persona.set("nombre", nombre.trim());
        persona.set("apellido", apellido.trim());
        persona.set("dni", dniStr.trim());
        persona.set("contacto", contacto.trim());
        persona.set("direccion", direccion.trim());
        persona.saveIt();

        // Persistir Docente relacionado a la Persona
        Docente docente = new Docente();
        docente.set("matricula", Integer.parseInt(matriculaStr.trim()));
        docente.setPerson(persona);
        docente.saveIt();

        return docente;
    }
}
