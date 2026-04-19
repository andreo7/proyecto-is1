package com.is1.proyecto.validators;

import com.is1.proyecto.exceptions.ValidationException;

/**
 * Valida los datos de entrada de los formularios relacionados a usuarios.
 * Solo verifica formato y presencia: NO accede a la base de datos.
 * Las validaciones de unicidad (nombre ya registrado) son responsabilidad de UserService.
 */
public class UserValidator {

    private UserValidator() {}

    /**
     * Valida los datos para registrar un nuevo usuario.
     *
     * @throws ValidationException si algún campo no cumple las reglas de formato.
     */
    public static void validateRegistration(String name, String password) {
        if (isBlank(name)) {
            throw new ValidationException("El nombre de usuario es requerido.");
        }
        if (name.length() < 3) {
            throw new ValidationException("El nombre de usuario debe tener al menos 3 caracteres.");
        }
        if (isBlank(password)) {
            throw new ValidationException("La contraseña es requerida.");
        }
        if (password.length() < 6) {
            throw new ValidationException("La contraseña debe tener al menos 6 caracteres.");
        }
    }

    /**
     * Valida los datos para iniciar sesión.
     *
     * @throws ValidationException si algún campo está vacío.
     */
    public static void validateLogin(String username, String password) {
        if (isBlank(username)) {
            throw new ValidationException("El nombre de usuario es requerido.");
        }
        if (isBlank(password)) {
            throw new ValidationException("La contraseña es requerida.");
        }
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
