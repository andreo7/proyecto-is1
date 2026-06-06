package com.is1.proyecto.controller;

import com.is1.proyecto.exceptions.ServiceException;
import com.is1.proyecto.exceptions.ValidationException;
import com.is1.proyecto.service.MateriaService;
import com.is1.proyecto.validators.MateriaValidator;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

public class MateriaController {

    private static final MustacheTemplateEngine engine = new MustacheTemplateEngine();
    private static final MateriaService materiaService = new MateriaService();

    private MateriaController() {}

    public static void registerRoutes() {
        get("/materia/new",  MateriaController::showForm,     engine);
        post("/materia/new", MateriaController::handleCreate);
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
}