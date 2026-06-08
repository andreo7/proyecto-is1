package com.is1.proyecto.controller;

import com.is1.proyecto.exceptions.ServiceException;
import com.is1.proyecto.exceptions.ValidationException;
import com.is1.proyecto.service.DocenteMateriaService;
import com.is1.proyecto.util.WebUtils;
import com.is1.proyecto.validators.DocenteMateriaValidator;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.get;
import static spark.Spark.post;

/**
 * Maneja las rutas HTTP para la asociación Docente ↔ Materia.
 *
 * GET  /teacher/:id/materias            → ver materias del docente + formulario de alta
 * POST /teacher/:id/materias            → crear asociación
 * POST /teacher/:id/materias/:aid/edit  → actualizar cargo de una asociación
 * POST /teacher/:id/materias/:aid/delete → eliminar asociación
 */
public class DocenteMateriaController {

    private static final DocenteMateriaService service = new DocenteMateriaService();
    private static final MustacheTemplateEngine engine = new MustacheTemplateEngine();

    private DocenteMateriaController() {}

    public static void registerRoutes() {
        get("/teacher/:id/materias",
                DocenteMateriaController::showMaterias, engine);
        post("/teacher/:id/materias",
                DocenteMateriaController::handleAsociar);
        post("/teacher/:id/materias/:aid/edit",
                DocenteMateriaController::handleActualizarCargo);
        post("/teacher/:id/materias/:aid/delete",
                DocenteMateriaController::handleDesasociar);
    }

    // -------------------------------------------------------------------------
    // Handlers GET
    // -------------------------------------------------------------------------

    private static ModelAndView showMaterias(Request req, Response res) {
        String idStr = req.params("id");
        Map<String, Object> model = new HashMap<>();

        try {
            DocenteMateriaValidator.validateId(idStr, "docente");
            int idDocente = Integer.parseInt(idStr.trim());

            model.putAll(service.getDatosDcente(idDocente));
            model.put("materias",    service.getMateriasDeDocente(idDocente));
            model.put("disponibles", service.getMateriasDisponibles(idDocente));
            model.put("hayDisponibles", !service.getMateriasDisponibles(idDocente).isEmpty());

        } catch (ValidationException | ServiceException e) {
            res.redirect("/teacher?error=" + WebUtils.encode(e.getMessage()));
            return null;
        }

        addQueryMessages(req, model);
        return new ModelAndView(model, "docente_materias.mustache");
    }

    // -------------------------------------------------------------------------
    // Handlers POST
    // -------------------------------------------------------------------------

    private static Object handleAsociar(Request req, Response res) {
        String idStr       = req.params("id");
        String idMateriaStr = req.queryParams("idMateria");
        String cargo        = req.queryParams("cargo");

        try {
            DocenteMateriaValidator.validate(idStr, idMateriaStr, cargo);
            int idDocente = Integer.parseInt(idStr.trim());
            int idMateria = Integer.parseInt(idMateriaStr.trim());
            service.asociar(idDocente, idMateria, cargo);
            res.redirect("/teacher/" + idStr + "/materias?success="
                    + WebUtils.encode("Materia asociada exitosamente."));
        } catch (ValidationException | ServiceException e) {
            res.redirect("/teacher/" + idStr + "/materias?error="
                    + WebUtils.encode(e.getMessage()));
        } catch (Exception e) {
            System.err.println("Error inesperado en handleAsociar: " + e.getMessage());
            res.redirect("/teacher/" + idStr + "/materias?error="
                    + WebUtils.encode("Error interno. Intente de nuevo."));
        }
        return "";
    }

    private static Object handleActualizarCargo(Request req, Response res) {
        String idStr  = req.params("id");
        String aidStr = req.params("aid");
        String cargo  = req.queryParams("cargo");

        try {
            DocenteMateriaValidator.validateId(idStr,  "docente");
            DocenteMateriaValidator.validateId(aidStr, "asociación");
            if (cargo == null || cargo.trim().isEmpty()) {
                throw new ValidationException("El cargo es requerido.");
            }
            service.actualizarCargo(Integer.parseInt(aidStr.trim()), cargo);
            res.redirect("/teacher/" + idStr + "/materias?success="
                    + WebUtils.encode("Cargo actualizado exitosamente."));
        } catch (ValidationException | ServiceException e) {
            res.redirect("/teacher/" + idStr + "/materias?error="
                    + WebUtils.encode(e.getMessage()));
        } catch (Exception e) {
            System.err.println("Error inesperado en handleActualizarCargo: " + e.getMessage());
            res.redirect("/teacher/" + idStr + "/materias?error="
                    + WebUtils.encode("Error interno. Intente de nuevo."));
        }
        return "";
    }

    private static Object handleDesasociar(Request req, Response res) {
        String idStr  = req.params("id");
        String aidStr = req.params("aid");

        try {
            DocenteMateriaValidator.validateId(idStr,  "docente");
            DocenteMateriaValidator.validateId(aidStr, "asociación");
            service.desasociar(Integer.parseInt(aidStr.trim()));
            res.redirect("/teacher/" + idStr + "/materias?success="
                    + WebUtils.encode("Materia desasociada exitosamente."));
        } catch (ValidationException | ServiceException e) {
            res.redirect("/teacher/" + idStr + "/materias?error="
                    + WebUtils.encode(e.getMessage()));
        } catch (Exception e) {
            System.err.println("Error inesperado en handleDesasociar: " + e.getMessage());
            res.redirect("/teacher/" + idStr + "/materias?error="
                    + WebUtils.encode("Error interno. Intente de nuevo."));
        }
        return "";
    }

    // -------------------------------------------------------------------------
    // Utilidades
    // -------------------------------------------------------------------------

    private static void addQueryMessages(Request req, Map<String, Object> model) {
        String success = req.queryParams("success");
        String error   = req.queryParams("error");
        if (success != null && !success.isEmpty()) model.put("successMessage", success);
        if (error   != null && !error.isEmpty())   model.put("errorMessage",   error);
    }
}