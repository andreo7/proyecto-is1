package com.is1.proyecto.controller;

import com.is1.proyecto.exceptions.ServiceException;
import com.is1.proyecto.exceptions.ValidationException;
import com.is1.proyecto.models.Docente;
import com.is1.proyecto.service.DocenteService;
import com.is1.proyecto.validators.DocenteValidator;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    /**
     * Registra todas las rutas de este controller en Spark.
     *
     * GET  /teachers          → listado de docentes
     * GET  /teacher/new       → formulario de alta
     * POST /teacher/new       → procesar alta
     * POST /teacher/:id/delete → procesar baja
     */
    public static void registerRoutes() {
        get("/teacher",            DocenteController::showList,   engine);
        get("/teacher/new",         DocenteController::showForm,   engine);
        post("/teacher/new",        DocenteController::handleCreate);
        post("/teacher/:id/delete", DocenteController::handleDelete);
    }

    // -------------------------------------------------------------------------
    // Handlers GET
    // -------------------------------------------------------------------------

    /** Lista todos los docentes registrados. */
    private static ModelAndView showList(Request req, Response res) {
        Map<String, Object> model = new HashMap<>();

        // Mensajes de feedback que vienen por query param tras un redirect
        String success = req.queryParams("success");
        String error   = req.queryParams("error");
        if (success != null && !success.isEmpty()) model.put("successMessage", success);
        if (error   != null && !error.isEmpty())   model.put("errorMessage",   error);

        List<Docente> docentes = docenteService.getAllDocentes();

        // Mustache no tiene lógica condicional nativa basada en tamaño de lista,
        // por eso usamos un flag booleano "hasDocentes" para mostrar/ocultar la tabla.
        model.put("hasDocentes", !docentes.isEmpty());

        // Convertimos cada Docente a un Map para que Mustache pueda iterar
        // sin necesidad de reflexión ni getters específicos del ORM.
        List<Map<String, Object>> docentesData = new ArrayList<>();
        for (Docente d : docentes) {
            Map<String, Object> row = new HashMap<>();
            row.put("id",        d.getId());
            row.put("nombre",    d.getNombre());
            row.put("apellido",  d.getApellido());
            row.put("dni",       d.getPerson().getDni());
            row.put("contacto",  d.getContacto());
            row.put("direccion", d.getDireccion());
            row.put("matricula", d.getMatricula());
            docentesData.add(row);
        }
        model.put("docentes", docentesData);

        return new ModelAndView(model, "docentes_list.mustache");
    }

    /** Muestra el formulario de alta de docente. */
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

    /** Procesa el formulario de alta de docente. */
    private static Object handleCreate(Request req, Response res) {
        String nombre    = req.queryParams("nombre");
        String apellido  = req.queryParams("apellido");
        String dni       = req.queryParams("dni");
        String contacto  = req.queryParams("contacto");
        String direccion = req.queryParams("direccion");
        String matricula = req.queryParams("matricula");

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

    /**
     * Procesa la baja de un docente.
     * Recibe el ID del docente por path param, delega al service y redirige al listado.
     */
    private static Object handleDelete(Request req, Response res) {
        String idParam = req.params(":id");

        try {
            DocenteValidator.validateId(idParam);
            int docenteId = Integer.parseInt(idParam.trim());

            docenteService.deleteDocente(docenteId);

            res.redirect("/teacher?success=Docente eliminado exitosamente.");

        } catch (ValidationException | ServiceException e) {
            res.redirect("/teacher?error=" + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error inesperado en handleDelete (Docente): " + e.getMessage());
            res.redirect("/teacher?error=Error interno al eliminar el docente. Intente de nuevo.");
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