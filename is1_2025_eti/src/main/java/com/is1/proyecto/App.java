package com.is1.proyecto;

import com.is1.proyecto.config.DBConfigSingleton;
import com.is1.proyecto.controller.DashboardController;
import com.is1.proyecto.controller.DocenteController;
import com.is1.proyecto.controller.UserController;
import org.javalite.activejdbc.Base;

import static spark.Spark.*;

/**
 * Punto de entrada de la aplicación.
 *
 * Responsabilidades (y SOLO estas):
 *   1. Iniciar Spark en el puerto configurado.
 *   2. Registrar los filtros before/after para la conexión a la base de datos.
 *   3. Delegar el registro de rutas a cada Controller.
 *
 * Regla: cada vez que se agrega una nueva entidad al sistema, se agrega
 * UNA línea aquí (NuevoController.registerRoutes()) y el resto del trabajo
 * ocurre en los archivos del nuevo módulo, sin conflictos con el resto del equipo.
 */
public class App {

    public static void main(String[] args) {
        port(8080);

        DBConfigSingleton dbConfig = DBConfigSingleton.getInstance();

        // --- Filtros de conexión a la base de datos (se ejecutan en cada request) ---
        before((req, res) -> {
            try {
                Base.open(dbConfig.getDriver(), dbConfig.getDbUrl(), dbConfig.getUser(), dbConfig.getPass());
            } catch (Exception e) {
                System.err.println("Error al abrir conexión con la BD: " + e.getMessage());
                halt(500, "{\"error\": \"Error interno del servidor: fallo al conectar a la base de datos.\"}");
            }
        });

        after((req, res) -> {
            try {
                Base.close();
            } catch (Exception e) {
                System.err.println("Error al cerrar conexión con la BD: " + e.getMessage());
            }
        });

        // --- Registro de rutas por módulo ---
        // Agregar aquí una línea por cada nuevo Controller que se cree.
        DocenteController.registerRoutes();
        DashboardController.registerRoutes();
        UserController.registerRoutes();
    }
}
