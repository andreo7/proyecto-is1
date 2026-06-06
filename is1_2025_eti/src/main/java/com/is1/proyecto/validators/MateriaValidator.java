package com.is1.proyecto.validators;

import com.is1.proyecto.exceptions.ValidationException;

public class MateriaValidator {

    private MateriaValidator() {}

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
            throw new ValidationException("El nombre no puede contener numeros.");
        }
        if (!codigoStr.trim().matches("^[0-9]+$")) {
            throw new ValidationException("El código debe contener solo numeros.");
        }

        try {
            int codigo = Integer.parseInt(codigoStr.trim());
            if (codigo <= 0) {
                throw new ValidationException("El código debe ser un numero positivo.");
            }
        } catch (NumberFormatException e) {
            throw new ValidationException("El código debe ser un número valido.");
        }
    }


    static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}