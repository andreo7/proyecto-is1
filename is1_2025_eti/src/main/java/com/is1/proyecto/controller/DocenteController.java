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

    /**
     * Registra todas las rutas de este controller en Spark.
     *
     * GET  /teacher              → listado de docentes
     * GET  /teacher/new          → formulario de alta
     * POST /teacher/new          → procesar alta
     * GET  /teacher/:id/edit     → formulario de edición
     * POST /teacher/:id/edit     → procesar edición
     * POST /teacher/:id/delete   → procesar baja
     */
    public static void registerRoutes() {
        get("/teacher",             DocenteController::showList,     engine);
        get("/teacher/new",         DocenteController::showForm,     engine);
        post("/teacher/new",        DocenteController::handleCreate);
        get("/teacher/:id/edit",    DocenteController::showEditForm, engine);
        post("/teacher/:id/edit",   DocenteController::handleUpdate);
        post("/teacher/:id/delete", DocenteController::handleDelete);
    }

    // -------------------------------------------------------------------------
    // Handlers GET
    // -------------------------------------------------------------------------

    private static ModelAndView showList(Request req, Response res) {
        Map<String, Object> model = new HashMap<>();
        addQueryMessages(req, model);
        model.put("docentes", docenteService.getAllDocentes());
        return new ModelAndView(model, "docentes_list.mustache");
    }

    private static ModelAndView showForm(Request req, Response res) {
        Map<String, Object> model = new HashMap<>();
        addQueryMessages(req, model);
        return new ModelAndView(model, "docente_form.mustache");
    }

    private static ModelAndView showEditForm(Request req, Response res) {
        Map<String, Object> model = new HashMap<>();
        try {
            int id = Integer.parseInt(req.params("id"));
            Map<String, Object> data = docenteService.findById(id);
            model.putAll(data);
        } catch (ServiceException e) {
            res.redirect("/teacher?error=" + e.getMessage());
            return null;
        } catch (NumberFormatException e) {
            res.redirect("/teacher?error=ID de docente inválido.");
            return null;
        }
        addQueryMessages(req, model);
        return new ModelAndView(model, "docente_edit.mustache");
    }

    // -------------------------------------------------------------------------
    // Handlers POST
    // -------------------------------------------------------------------------

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
            res.redirect("/teacher/new?success=Alta realizada exitosamente para " + capitalize(nombre) + "!");
        } catch (ValidationException | ServiceException e) {
            res.redirect("/teacher/new?error=" + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error inesperado en handleCreate (Docente): " + e.getMessage());
            res.redirect("/teacher/new?error=Error interno al dar de alta al docente. Intente de nuevo.");
        }
        return "";
    }

    private static Object handleUpdate(Request req, Response res) {
        String idStr     = req.params("id");
        String nombre    = req.queryParams("nombre");
        String apellido  = req.queryParams("apellido");
        String dni       = req.queryParams("dni");
        String contacto  = req.queryParams("contacto");
        String direccion = req.queryParams("direccion");
        String matricula = req.queryParams("matricula");

        try {
            DocenteValidator.validate(nombre, apellido, dni, contacto, direccion, matricula);
            int id = Integer.parseInt(idStr.trim());
            docenteService.update(id, nombre, apellido, dni, contacto, direccion, matricula);
            res.redirect("/teacher?success=Docente actualizado exitosamente.");
        } catch (ValidationException | ServiceException e) {
            res.redirect("/teacher/" + idStr + "/edit?error=" + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error inesperado en handleUpdate (Docente): " + e.getMessage());
            res.redirect("/teacher?error=Error interno al actualizar el docente. Intente de nuevo.");
        }
        return "";
    }

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

    private static void addQueryMessages(Request req, Map<String, Object> model) {
        String success = req.queryParams("success");
        String error   = req.queryParams("error");
        if (success != null && !success.isEmpty()) model.put("successMessage", success);
        if (error   != null && !error.isEmpty())   model.put("errorMessage",   error);
    }
}