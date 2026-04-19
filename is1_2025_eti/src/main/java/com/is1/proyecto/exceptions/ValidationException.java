package com.is1.proyecto.exceptions;

/**
 * Excepción lanzada por la capa de validadores cuando los datos
 * de entrada de un formulario no cumplen las reglas de formato o presencia.
 * Se diferencia de ServiceException en que ocurre ANTES de tocar la base de datos.
 */
public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }
}
