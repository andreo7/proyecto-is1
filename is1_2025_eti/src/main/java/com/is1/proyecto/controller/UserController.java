package com.is1.proyecto.controller;

import com.is1.proyecto.exceptions.ServiceException;
import com.is1.proyecto.exceptions.ValidationException;
import com.is1.proyecto.models.User;
import com.is1.proyecto.service.UserService;
import com.is1.proyecto.validators.UserValidator;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

/**
 * Maneja todas las rutas HTTP relacionadas a usuarios:
 * registro, login, logout y la landing page.
 *
 * Responsabilidades:
 *   - Leer parámetros del Request de Spark
 *   - Delegar validación a UserValidator
 *   - Delegar lógica de negocio a UserService
 *   - Construir el modelo para las vistas Mustache
 *   - Manejar redirects y códigos HTTP
 *
 * NO contiene lógica de negocio ni accesos directos a la base de datos.
 */
public class UserController {

    private static final UserService userService = new UserService();
    private static final MustacheTemplateEngine engine = new MustacheTemplateEngine();

    private UserController() {}

    /** Registra todas las rutas de este controller en Spark. */
    public static void registerRoutes() {
        get("/",             UserController::showLoginPage,   engine);
        get("/login",        UserController::showLoginPage,   engine);
        get("/user/new",     UserController::showRegisterForm, engine);
        get("/user/create",  UserController::showRegisterFormWithMessages, engine);
        post("/user/new",    UserController::handleRegister);
        post("/login",       UserController::handleLogin, engine);
        get("/logout",       UserController::handleLogout);
    }

    // -------------------------------------------------------------------------
    // Handlers GET
    // -------------------------------------------------------------------------

    private static ModelAndView showLoginPage(Request req, Response res) {
        Map<String, Object> model = new HashMap<>();
        addQueryMessages(req, model);
        return new ModelAndView(model, "login.mustache");
    }

    private static ModelAndView showRegisterForm(Request req, Response res) {
        return new ModelAndView(new HashMap<>(), "user_form.mustache");
    }

    private static ModelAndView showRegisterFormWithMessages(Request req, Response res) {
        Map<String, Object> model = new HashMap<>();
        addQueryMessages(req, model);
        return new ModelAndView(model, "user_form.mustache");
    }

    // -------------------------------------------------------------------------
    // Handlers POST
    // -------------------------------------------------------------------------

    private static Object handleRegister(Request req, Response res) {
        String name     = req.queryParams("name");
        String password = req.queryParams("password");

        try {
            UserValidator.validateRegistration(name, password);
            userService.createUser(name, password);
            res.redirect("/user/create?message=Alta realizada exitosamente para " + name + "!");
        } catch (ValidationException | ServiceException e) {
            res.redirect("/user/create?error=" + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error inesperado en handleRegister: " + e.getMessage());
            res.redirect("/user/create?error=Error interno al crear la cuenta. Intente de nuevo.");
        }
        return "";
    }

    private static ModelAndView handleLogin(Request req, Response res) {
        Map<String, Object> model = new HashMap<>();
        String username = req.queryParams("username");
        String password = req.queryParams("password");

        try {
            UserValidator.validateLogin(username, password);
            User user = userService.login(username, password);

            req.session(true).attribute("currentUserUsername", username);
            req.session().attribute("userId",    user.getId());
            req.session().attribute("loggedIn",  true);

            model.put("username", username);
            return new ModelAndView(model, "dashboard.mustache");

        } catch (ValidationException | ServiceException e) {
            res.status(401);
            model.put("errorMessage", e.getMessage());
            return new ModelAndView(model, "login.mustache");
        } catch (Exception e) {
            System.err.println("Error inesperado en handleLogin: " + e.getMessage());
            res.status(500);
            model.put("errorMessage", "Error interno. Intente de nuevo.");
            return new ModelAndView(model, "login.mustache");
        }
    }

    private static Object handleLogout(Request req, Response res) {
        req.session().invalidate();
        res.redirect("/");
        return null;
    }

    // -------------------------------------------------------------------------
    // Utilidades privadas
    // -------------------------------------------------------------------------

    /** Lee los query params 'error' y 'message' y los agrega al modelo. */
    private static void addQueryMessages(Request req, Map<String, Object> model) {
        String error   = req.queryParams("error");
        String message = req.queryParams("message");
        if (error   != null && !error.isEmpty())   model.put("errorMessage",   error);
        if (message != null && !message.isEmpty()) model.put("successMessage", message);
    }
}
