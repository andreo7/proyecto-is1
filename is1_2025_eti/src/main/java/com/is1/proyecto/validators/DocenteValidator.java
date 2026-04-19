package com.is1.proyecto.validators;

import com.is1.proyecto.exceptions.ValidationException;

/**
 * Valida los datos de entrada del formulario de alta/edición de docente.
 * Solo verifica formato y presencia: NO accede a la base de datos.
 * Las validaciones de unicidad (DNI, contacto, matrícula ya registrados)
 * son responsabilidad de DocenteService.
 */
public class DocenteValidator {

    private static final String EMAIL_REGEX =
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    private DocenteValidator() {}

    /**
     * Valida los campos del formulario de alta de docente.
     *
     * @param nombre     nombre del docente
     * @param apellido   apellido del docente
     * @param dniStr     DNI como string (puede llegar vacío o no numérico desde el form)
     * @param contacto   email de contacto
     * @param direccion  dirección postal
     * @param matriculaStr número de matrícula como string
     * @throws ValidationException si algún campo no cumple las reglas.
     */
    public static void validate(String nombre, String apellido, String dniStr,
                                String contacto, String direccion, String matriculaStr) {
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
            throw new ValidationException("La dirección es requerida.");
        }
        if (isBlank(matriculaStr)) {
            throw new ValidationException("La matrícula es requerida.");
        }

        try {
            int dni = Integer.parseInt(dniStr.trim());
            if (dni <= 0) {
                throw new ValidationException("El DNI debe ser un número positivo.");
            }
        } catch (NumberFormatException e) {
            throw new ValidationException("El DNI debe ser un número válido (sin letras ni símbolos).");
        }

        try {
            int matricula = Integer.parseInt(matriculaStr.trim());
            if (matricula <= 0) {
                throw new ValidationException("La matrícula debe ser un número positivo.");
            }
        } catch (NumberFormatException e) {
            throw new ValidationException("La matrícula debe ser un número válido.");
        }

        if (!contacto.trim().matches(EMAIL_REGEX)) {
            throw new ValidationException("El contacto debe ser un email válido (ej: nombre@dominio.com).");
        }
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
