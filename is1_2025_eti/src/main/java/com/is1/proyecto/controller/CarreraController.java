package com.is1.proyecto.controller;

import com.is1.proyecto.exceptions.ServiceException;
import com.is1.proyecto.exceptions.ValidationException;
import com.is1.proyecto.service.CarreraService;
import com.is1.proyecto.validators.CarreraValidator;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.get;
import static spark.Spark.post;

/**
 * Maneja todas las rutas HTTP relacionadas a carreras.
 *
 * Responsabilidades:
 *   - Leer parámetros del Request de Spark
 *   - Delegar validación a CarreraValidator
 *   - Delegar lógica de negocio a CarreraService
 *   - Construir el modelo para las vistas Mustache
 *   - Manejar redirects y códigos HTTP
 *
 * NO contiene lógica de negocio ni accesos directos a la base de datos.
 */
public class CarreraController {

    private static final CarreraService carreraService = new CarreraService();
    private static final MustacheTemplateEngine engine = new MustacheTemplateEngine();

    private CarreraController() {}

    /**
     * Registra todas las rutas de este controller en Spark.
     *
     * GET  /carrera/list          → listado de carreras
     * GET  /carrera/new           → formulario de alta
     * POST /carrera/new           → procesar alta
     * GET  /carrera/:id/edit      → formulario de edición
     * POST /carrera/:id/edit      → procesar edición
     * POST /carrera/delete        → procesar baja
     */
    public static void registerRoutes() {
        get("/carrera/list",      CarreraController::showList,     engine);
        get("/carrera/new",       CarreraController::showForm,     engine);
        post("/carrera/new",      CarreraController::handleCreate);
        get("/carrera/:id/edit",  CarreraController::showEditForm, engine);
        post("/carrera/:id/edit", CarreraController::handleUpdate);
        post("/carrera/delete",   CarreraController::handleDelete);
    }

    // -------------------------------------------------------------------------
    // Handlers GET
    // -------------------------------------------------------------------------

    private static ModelAndView showList(Request req, Response res) {
        Map<String, Object> model = new HashMap<>();
        addQueryMessages(req, model);
        model.put("carreras", carreraService.getAllCarreras());
        return new ModelAndView(model, "carrera_list.mustache");
    }

    private static ModelAndView showForm(Request req, Response res) {
        Map<String, Object> model = new HashMap<>();
        addQueryMessages(req, model);
        return new ModelAndView(model, "carrera_form.mustache");
    }

    private static ModelAndView showEditForm(Request req, Response res) {
        Map<String, Object> model = new HashMap<>();
        try {
            int id = Integer.parseInt(req.params("id"));
            Map<String, Object> data = carreraService.findById(id);
            model.putAll(data);
        } catch (ServiceException e) {
            res.redirect("/carrera/list?error=" + e.getMessage());
            return null;
        } catch (NumberFormatException e) {
            res.redirect("/carrera/list?error=ID de carrera inválido.");
            return null;
        }
        addQueryMessages(req, model);
        return new ModelAndView(model, "carrera_edit.mustache");
    }

    // -------------------------------------------------------------------------
    // Handlers POST
    // -------------------------------------------------------------------------

    private static Object handleCreate(Request req, Response res) {
        String nombre      = req.queryParams("nombre");
        String descripcion = req.queryParams("descripcion");
        String codigoStr   = req.queryParams("codigo");

        try {
            CarreraValidator.validate(nombre, descripcion, codigoStr);
            carreraService.createCarrera(Integer.parseInt(codigoStr.trim()), nombre, descripcion);
            res.status(201);
            res.redirect("/carrera/new?success=Alta de carrera realizada exitosamente para "
                    + capitalize(nombre) + "!");
        } catch (ValidationException | ServiceException e) {
            res.status(400);
            res.redirect("/carrera/new?error=" + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error al dar de alta carrera: " + e.getMessage());
            e.printStackTrace();
            res.status(500);
            res.redirect("/carrera/new?error=Error interno. Intente de nuevo.");
        }
        return "";
    }

    private static Object handleUpdate(Request req, Response res) {
        String idStr       = req.params(":id");
        String nombre      = req.queryParams("nombre");
        String descripcion = req.queryParams("descripcion");
        String codigoStr   = req.queryParams("codigo");

        try {
            CarreraValidator.validate(nombre, descripcion, codigoStr);
            carreraService.update(Integer.parseInt(idStr.trim()), Integer.parseInt(codigoStr.trim()),
                    nombre, descripcion);
            res.redirect("/carrera/list?success=Carrera actualizada exitosamente.");
        } catch (ValidationException | ServiceException e) {
            res.status(400);
            res.redirect("/carrera/" + idStr + "/edit?error=" + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error al actualizar carrera: " + e.getMessage());
            e.printStackTrace();
            res.status(500);
            res.redirect("/carrera/list?error=Error interno. Intente de nuevo.");
        }
        return "";
    }

    private static Object handleDelete(Request req, Response res) {
        String idStr = req.queryParams("id");

        try {
            carreraService.delete(Integer.parseInt(idStr.trim()));
            res.redirect("/carrera/list?success=Carrera eliminada exitosamente.");
        } catch (ServiceException e) {
            res.status(400);
            res.redirect("/carrera/list?error=" + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error al eliminar carrera: " + e.getMessage());
            e.printStackTrace();
            res.status(500);
            res.redirect("/carrera/list?error=Error interno. Intente de nuevo.");
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