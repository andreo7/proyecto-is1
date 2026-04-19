package com.is1.proyecto.controller;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

/**
 * Maneja la ruta del panel de control (/dashboard).
 * Verifica que el usuario tenga sesión activa antes de renderizar la vista.
 *
 * A medida que se agreguen más secciones al dashboard (perfil, configuración),
 * los handlers correspondientes se incorporan aquí sin tocar otros controllers.
 */
public class DashboardController {

    private static final MustacheTemplateEngine engine = new MustacheTemplateEngine();

    private DashboardController() {}

    /** Registra todas las rutas de este controller en Spark. */
    public static void registerRoutes() {
        get("/dashboard", DashboardController::showDashboard, engine);
    }

    // -------------------------------------------------------------------------
    // Handlers GET
    // -------------------------------------------------------------------------

    private static ModelAndView showDashboard(Request req, Response res) {
        String  currentUsername = req.session().attribute("currentUserUsername");
        Boolean loggedIn        = req.session().attribute("loggedIn");

        if (currentUsername == null || loggedIn == null || !loggedIn) {
            res.redirect("/login?error=Debés iniciar sesión para acceder a esta página.");
            return null;
        }

        Map<String, Object> model = new HashMap<>();
        model.put("username", currentUsername);
        return new ModelAndView(model, "dashboard.mustache");
    }
}
