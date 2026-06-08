package com.is1.proyecto.validators;

import com.is1.proyecto.exceptions.ValidationException;

/**
 * Valida los datos de entrada del formulario de alta/edición de carrera.
 * Solo verifica formato y presencia: NO accede a la base de datos.
 * Las validaciones de unicidad son responsabilidad de CarreraService.
 */
public class CarreraValidator {

    private CarreraValidator() {}

    /**
     * Valida los campos del formulario de alta/edición de carrera.
     *
     * @param nombre       nombre de la carrera
     * @param descripcion  descripción de la carrera
     * @param codigoStr    código como string
     * @throws ValidationException si algún campo no cumple las reglas.
     */
    public static void validate(String nombre, String descripcion, String codigoStr) {
        if (isBlank(nombre)) {
            throw new ValidationException("El nombre es requerido.");
        }
        if (isBlank(descripcion)) {
            throw new ValidationException("La descripción es requerida.");
        }
        if (isBlank(codigoStr)) {
            throw new ValidationException("El código es requerido.");
        }
        if (nombre.trim().matches(".*[0-9].*")) {
            throw new ValidationException("El nombre no puede contener números.");
        }
        if (!codigoStr.trim().matches("^[0-9]+$")) {
            throw new ValidationException("El código debe contener solo números.");
        }

        try {
            int codigo = Integer.parseInt(codigoStr.trim());
            if (codigo <= 0) {
                throw new ValidationException("El código debe ser un número positivo.");
            }
        } catch (NumberFormatException e) {
            throw new ValidationException("El código debe ser un número válido.");
        }
    }

    static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}