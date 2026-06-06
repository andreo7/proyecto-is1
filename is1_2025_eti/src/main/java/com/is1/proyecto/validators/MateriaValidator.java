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