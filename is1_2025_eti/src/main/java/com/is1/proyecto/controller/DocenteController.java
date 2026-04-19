package com.is1.proyecto.controller;

import com.is1.proyecto.exceptions.ServiceException;
import com.is1.proyecto.exceptions.ValidationException;
import com.is1.proyecto.service.DocenteService;
import com.is1.proyecto.validators.DocenteValidator;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

/**
 * Maneja todas las rutas HTTP relacionadas a docentes.
 *
 * Responsabilidades:
 *   - Leer parámetros del Request de Spark
 *   - Delegar validación a DocenteValidator
 *   - Delegar lógica de negocio a DocenteService
 *   - Construir el modelo para las vistas Mustache
 *   - Manejar redirects y códigos HTTP
 *
 * NO contiene lógica de negocio ni accesos directos a la base de datos.
 */
public class DocenteController {

    private static final DocenteService docenteService = new DocenteService();
    private static final MustacheTemplateEngine engine = new MustacheTemplateEngine();

    private DocenteController() {}

    /** Registra todas las rutas de este controller en Spark. */
    public static void registerRoutes() {
        get("/teacher/new",  DocenteController::showForm, engine);
        post("/teacher/new", DocenteController::handleCreate);
    }

    // -------------------------------------------------------------------------
    // Handlers GET
    // -------------------------------------------------------------------------

    private static ModelAndView showForm(Request req, Response res) {
        Map<String, Object> model = new HashMap<>();
        String success = req.queryParams("success");
        String error   = req.queryParams("error");
        if (success != null && !success.isEmpty()) model.put("successMessage", success);
        if (error   != null && !error.isEmpty())   model.put("errorMessage",   error);
        return new ModelAndView(model, "docente_form.mustache");
    }

    // -------------------------------------------------------------------------
    // Handlers POST
    // -------------------------------------------------------------------------

    private static Object handleCreate(Request req, Response res) {
        String nombre     = req.queryParams("nombre");
        String apellido   = req.queryParams("apellido");
        String dni        = req.queryParams("dni");
        String contacto   = req.queryParams("contacto");
        String direccion  = req.queryParams("direccion");
        String matricula  = req.queryParams("matricula");

        try {
            DocenteValidator.validate(nombre, apellido, dni, contacto, direccion, matricula);
            docenteService.createDocente(nombre, apellido, dni, contacto, direccion, matricula);

            String nombreCapitalizado = capitalize(nombre);
            res.redirect("/teacher/new?success=Alta realizada exitosamente para " + nombreCapitalizado + "!");

        } catch (ValidationException | ServiceException e) {
            res.redirect("/teacher/new?error=" + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error inesperado en handleCreate (Docente): " + e.getMessage());
            res.redirect("/teacher/new?error=Error interno al dar de alta al docente. Intente de nuevo.");
        }
        return "";
    }

    // -------------------------------------------------------------------------
    // Utilidades privadas
    // -------------------------------------------------------------------------

    private static String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }
}