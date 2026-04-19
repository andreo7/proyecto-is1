package com.is1.proyecto.service;

import com.is1.proyecto.exceptions.ServiceException;
import com.is1.proyecto.models.User;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Contiene la lógica de negocio relacionada a los usuarios del sistema.
 * Esta clase NO sabe nada de HTTP (no recibe Request ni Response de Spark).
 * Lanza ServiceException ante violaciones de reglas de dominio.
 */
public class UserService {

    /**
     * Crea y persiste un nuevo usuario con la contraseña hasheada.
     *
     * @param name     nombre de usuario (ya validado por UserValidator)
     * @param password contraseña en texto plano (ya validada por UserValidator)
     * @return el User recién creado
     * @throws ServiceException si el nombre de usuario ya está registrado
     */
    public User createUser(String name, String password) {
        if (User.findFirst("name = ?", name) != null) {
            throw new ServiceException("El nombre de usuario '" + name + "' ya está registrado.");
        }

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        User user = new User();
        user.set("name", name);
        user.set("password", hashedPassword);
        user.saveIt();

        return user;
    }

    /**
     * Autentica un usuario verificando nombre y contraseña.
     *
     * @param username nombre de usuario
     * @param password contraseña en texto plano
     * @return el User autenticado
     * @throws ServiceException si las credenciales son incorrectas
     */
    public User login(String username, String password) {
        User user = User.findFirst("name = ?", username);

        if (user == null || !BCrypt.checkpw(password, user.getString("password"))) {
            // Mensaje intencionalmente genérico para no revelar si el usuario existe
            throw new ServiceException("Usuario o contraseña incorrectos.");
        }

        return user;
    }
}
