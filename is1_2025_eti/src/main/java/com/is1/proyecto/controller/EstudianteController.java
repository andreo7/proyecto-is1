package com.is1.proyecto.controller;

import com.is1.proyecto.exceptions.ServiceException;
import com.is1.proyecto.exceptions.ValidationException;
import com.is1.proyecto.service.EstudianteService;
import com.is1.proyecto.validators.EstudianteValidator;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

/**
 * Maneja todas las rutas HTTP relacionadas a estudiantes.
 *
 * Responsabilidades:
 *   - Leer parámetros del Request de Spark
 *   - Delegar validación a EstudianteValidator
 *   - Delegar lógica de negocio a EstudianteService
 *   - Construir el modelo para las vistas Mustache
 *   - Manejar redirects y códigos HTTP
 *
 * NO contiene lógica de negocio ni accesos directos a la base de datos.
 */
public class EstudianteController {

    private static final EstudianteService estudianteService = new EstudianteService();
    private static final MustacheTemplateEngine engine = new MustacheTemplateEngine();

    private EstudianteController() {}

    /** Registra todas las rutas de este controller en Spark. */
    public static void registerRoutes() {
        get("/students",              EstudianteController::showList,     engine);
        get("/student/new",           EstudianteController::showForm,     engine);
        post("/student/new",          EstudianteController::handleCreate);
    }

    // -------------------------------------------------------------------------
    // Handlers GET
    // -------------------------------------------------------------------------

    /** Lista todos los estudiantes registrados. */
    private static ModelAndView showList(Request req, Response res) {
        Map<String, Object> model = new HashMap<>();
        addQueryMessages(req, model);
        model.put("estudiantes", estudianteService.getAllEstudiantes());
        return new ModelAndView(model, "estudiante_list.mustache");
    }

    /** Muestra el formulario de alta de estudiante. */
    private static ModelAndView showForm(Request req, Response res) {
        Map<String, Object> model = new HashMap<>();
        addQueryMessages(req, model);
        return new ModelAndView(model, "estudiante_form.mustache");
    }


    // -------------------------------------------------------------------------
    // Handlers POST
    // -------------------------------------------------------------------------

    /** Procesa el formulario de alta. */
    private static Object handleCreate(Request req, Response res) {
        String nombre        = req.queryParams("nombre");
        String apellido      = req.queryParams("apellido");
        String dni           = req.queryParams("dni");
        String contacto      = req.queryParams("contacto");
        String direccion     = req.queryParams("direccion");
        String nroAlumno     = req.queryParams("nroAlumno");
        String estadoCarrera = req.queryParams("estadoCarrera");

        try {
            EstudianteValidator.validate(nombre, apellido, dni, contacto,
                    direccion, nroAlumno, estadoCarrera);
            estudianteService.createEstudiante(nombre, apellido, dni, contacto,
                    direccion, nroAlumno, estadoCarrera);

            String nombreCapitalizado = capitalize(nombre);
            res.redirect("/students?success=Alta realizada exitosamente para " + nombreCapitalizado + "!");

        } catch (ValidationException | ServiceException e) {
            res.redirect("/student/new?error=" + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error inesperado en handleCreate (Estudiante): " + e.getMessage());
            res.redirect("/student/new?error=Error interno al dar de alta al estudiante. Intente de nuevo.");
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

    private static void addQueryMessages(Request req, Map<String, Object> model) {
        String success = req.queryParams("success");
        String error   = req.queryParams("error");
        if (success != null && !success.isEmpty()) model.put("successMessage", success);
        if (error   != null && !error.isEmpty())   model.put("errorMessage",   error);
    }
}