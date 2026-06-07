package com.is1.proyecto.validators;

import com.is1.proyecto.exceptions.ValidationException;

/**
 * Valida los datos de entrada del formulario de alta/edición de estudiante.
 * Solo verifica formato y presencia: NO accede a la base de datos.
 * Las validaciones de unicidad (DNI, contacto y nroAlumno ya registrados)
 * son responsabilidad de EstudianteService.
 */
public class EstudianteValidator {

    private static final String EMAIL_REGEX =
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    private EstudianteValidator() {}

    /**
     * Valida los campos del formulario de alta/edición de estudiante.
     *
     * @param nombre         nombre del estudiante
     * @param apellido       apellido del estudiante
     * @param dniStr         DNI como string (puede llegar vacío o no numérico desde el form)
     * @param contacto       email de contacto
     * @param direccion      dirección postal
     * @param nroAlumnoStr   número de alumno como string
     * @param estadoCarrera  estado de la carrera ('Ingresante' o 'Avanzado')
     * @throws ValidationException si algún campo no cumple las reglas.
     */
    public static void validate(String nombre, String apellido, String dniStr,
                                String contacto, String direccion,
                                String nroAlumnoStr, String estadoCarrera) {
        if (isBlank(nombre)) {
            throw new ValidationException("El nombre es requerido.");
        }
        if (isBlank(apellido)) {
            throw new ValidationException("El apellido es requerido.");
        }
        if (isBlank(dniStr)) {
            throw new ValidationException("El DNI es requerido.");
        }
        if (isBlank(contacto)) {
            throw new ValidationException("El contacto es requerido.");
        }
        if (isBlank(direccion)) {
            throw new ValidationException("La direccion es requerida.");
        }
        if (isBlank(nroAlumnoStr)) {
            throw new ValidationException("El numero de alumno es requerido.");
        }
        if (isBlank(estadoCarrera)) {
            throw new ValidationException("El estado de carrera es requerido.");
        }

        try {
            int dni = Integer.parseInt(dniStr.trim());
            if (dni <= 0) {
                throw new ValidationException("El DNI debe ser un numero positivo.");
            }
        } catch (NumberFormatException e) {
            throw new ValidationException("El DNI debe ser un numero valido (sin letras ni simbolos).");
        }

        try {
            int nroAlumno = Integer.parseInt(nroAlumnoStr.trim());
            if (nroAlumno <= 0) {
                throw new ValidationException("El numero de alumno debe ser un numero positivo.");
            }
        } catch (NumberFormatException e) {
            throw new ValidationException("El numero de alumno debe ser un numero valido.");
        }

        if (!contacto.trim().matches(EMAIL_REGEX)) {
            throw new ValidationException("El contacto debe ser un email valido (ej: nombre@dominio.com).");
        }

        if (!"Ingresante".equals(estadoCarrera) && !"Avanzado".equals(estadoCarrera)) {
            throw new ValidationException("El estado de carrera debe ser 'Ingresante' o 'Avanzado'.");
        }
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}