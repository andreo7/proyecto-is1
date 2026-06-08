package com.is1.proyecto.validators;

import com.is1.proyecto.exceptions.ValidationException;

import java.util.Set;

public class DocenteMateriaValidator {

    private static final Set<String> CARGOS_VALIDOS =
            Set.of("Titular", "Adjunto", "JTP", "Ayudante");

    private DocenteMateriaValidator() {}

    /**
     * Valida los campos del formulario de alta de asociación.
     *
     * @param idDocenteStr ID del docente como string
     * @param idMateriaStr ID de la materia como string
     * @param cargo        cargo del docente en la materia
     * @throws ValidationException si algún campo no cumple las reglas.
     */
    public static void validate(String idDocenteStr, String idMateriaStr, String cargo) {
        validateId(idDocenteStr, "docente");
        validateId(idMateriaStr, "materia");

        if (isBlank(cargo)) {
            throw new ValidationException("El cargo es requerido.");
        }
        if (!CARGOS_VALIDOS.contains(cargo.trim())) {
            throw new ValidationException(
                    "El cargo debe ser uno de: Titular, Adjunto, JTP, Ayudante.");
        }
    }

    /**
     * Valida que un ID de path param sea un entero positivo válido.
     *
     * @param idStr  valor del path param
     * @param entity nombre de la entidad (para el mensaje de error)
     * @throws ValidationException si el ID no es válido.
     */
    public static void validateId(String idStr, String entity) {
        if (isBlank(idStr)) {
            throw new ValidationException("El ID de " + entity + " es requerido.");
        }
        try {
            int id = Integer.parseInt(idStr.trim());
            if (id <= 0) {
                throw new ValidationException(
                        "El ID de " + entity + " debe ser un número positivo.");
            }
        } catch (NumberFormatException e) {
            throw new ValidationException("El ID de " + entity + " no es válido.");
        }
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}