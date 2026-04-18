package com.is1.proyecto.exceptions;

/**
 * Excepción lanzada por la capa de servicios cuando se viola
 * una regla de negocio (ej: DNI duplicado, usuario ya existe).
 * Los controllers la capturan y la convierten en un mensaje de error para el usuario.
 */
public class ServiceException extends RuntimeException {

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
