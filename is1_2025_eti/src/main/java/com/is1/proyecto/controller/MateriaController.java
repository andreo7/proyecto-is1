package com.is1.proyecto.controller;

import com.is1.proyecto.exceptions.ServiceException;
import com.is1.proyecto.exceptions.ValidationException;
import com.is1.proyecto.models.Materia;
import com.is1.proyecto.service.MateriaService;
import com.is1.proyecto.validators.MateriaValidator;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.get;
import static spark.Spark.post;

public class MateriaController {

    private static final MustacheTemplateEngine engine = new MustacheTemplateEngine();
    private static final MateriaService materiaService = new MateriaService();

    private MateriaController() {}

    public static void registerRoutes() {
        get("/materia/new",  MateriaController::showForm,     engine);
        post("/materia/new", MateriaController::handleCreate);
        get("/materia/list",     MateriaController::showList,     engine);
        post("/materia/delete",  MateriaController::handleDelete);
        get("/materia/:id/edit",  MateriaController::showEditForm, engine);
        post("/materia/:id/edit", MateriaController::handleUpdate);
    }

    // -------------------------------------------------------------------------
    // Handlers GET
    // -------------------------------------------------------------------------

    private static ModelAndView showForm(Request req, Response res) {
        Map<String, Object> model = new HashMap<>();
        String success = req.queryParams("success");
        String error   = req.queryParams("error");
        if (success != null) model.put("successMessage", success);
        if (error   != null) model.put("errorMessage",   error);
        return new ModelAndView(model, "materia_form.mustache");
    }
    private static ModelAndView showList(Request req, Response res) {
        Map<String, Object> model = new HashMap<>();
        model.put("materias", materiaService.getAllMaterias());
        return new ModelAndView(model, "materia_list.mustache");
    }
    private static ModelAndView showEditForm(Request req, Response res) {
        Map<String, Object> model = new HashMap<>();
        try {
            int id = Integer.parseInt(req.params("id"));
            Materia materia = materiaService.findById(id);
            model.put("id",          materia.getId());
            model.put("nombre",      materia.getNombre());
            model.put("descripcion", materia.getDescripcion());
            model.put("codigo",      materia.getCodigo());
        } catch (ServiceException e) {
            res.redirect("/materia/list?error=" + e.getMessage());
            return null;
        }
        String success = req.queryParams("success");
        String error   = req.queryParams("error");
        if (success != null) model.put("successMessage", success);
        if (error   != null) model.put("errorMessage",   error);
        return new ModelAndView(model, "materia_edit.mustache");
    }

    // -------------------------------------------------------------------------
    // Handlers POST
    // -------------------------------------------------------------------------

    private static Object handleCreate(Request req, Response res) {
        String nombre      = req.queryParams("nombre");
        String descripcion = req.queryParams("descripcion");
        String codigoStr   = req.queryParams("codigo");

        try {
            MateriaValidator.validate(nombre, descripcion, codigoStr);
            materiaService.createMateria(Integer.parseInt(codigoStr.trim()), nombre, descripcion);
            res.status(201);
            res.redirect("/materia/new?success=Alta de materia realizada exitosamente para "
                    + nombre.substring(0, 1).toUpperCase() + nombre.substring(1).toLowerCase() + "!");
        } catch (ValidationException | ServiceException e) {
            res.status(400);
            res.redirect("/materia/new?error=" + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error al dar de alta materia: " + e.getMessage());
            e.printStackTrace();
            res.status(500);
            res.redirect("/materia/new?error=Error interno. Intente de nuevo.");
        }
        return "";
    }
    private static Object handleDelete(Request req, Response res) {
        String idStr = req.queryParams("id");

        try {
            materiaService.delete(Integer.parseInt(idStr.trim()));
            res.redirect("/materia/list?success=Materia eliminada exitosamente.");
        } catch (ServiceException e) {
            res.status(400);
            res.redirect("/materia/list?error=" + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error al eliminar materia: " + e.getMessage());
            e.printStackTrace();
            res.status(500);
            res.redirect("/materia/list?error=Error interno. Intente de nuevo.");
        }
        return "";
    }
    private static Object handleUpdate(Request req, Response res) {
        String idStr       = req.params(":id");
        String nombre      = req.queryParams("nombre");
        String descripcion = req.queryParams("descripcion");
        String codigoStr   = req.queryParams("codigo");

        try {
            MateriaValidator.validate(nombre, descripcion, codigoStr);
            materiaService.update(Integer.parseInt(idStr.trim()), nombre, descripcion,
                    Integer.parseInt(codigoStr.trim()));
            res.redirect("/materia/list?success=Materia actualizada exitosamente.");
        } catch (ValidationException | ServiceException e) {
            res.status(400);
            res.redirect("/materia/list?error=" + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error al actualizar materia: " + e.getMessage());
            e.printStackTrace();
            res.status(500);
            res.redirect("/materia/list?error=Error interno. Intente de nuevo.");
        }
        return "";
    }
}