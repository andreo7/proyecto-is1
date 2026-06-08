package com.is1.proyecto.service;

import com.is1.proyecto.exceptions.ServiceException;
import com.is1.proyecto.models.Docente;
import com.is1.proyecto.models.DocenteMateria;
import com.is1.proyecto.models.Materia;
import com.is1.proyecto.models.Persona;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Contiene la lógica de negocio para la asociación entre Docente y Materia.
 * Esta clase NO sabe nada de HTTP.
 * Lanza ServiceException ante violaciones de reglas de dominio.
 */
public class DocenteMateriaService {

    /**
     * Crea la asociación entre un docente y una materia con el cargo indicado.
     *
     * @throws ServiceException si el docente o materia no existen,
     *                          o si la asociación ya existe.
     */
    public DocenteMateria asociar(int idDocente, int idMateria, String cargo) {
        Docente docente = Docente.findById(idDocente);
        if (docente == null) {
            throw new ServiceException("El docente con ID " + idDocente + " no existe.");
        }

        Materia materia = Materia.findById(idMateria);
        if (materia == null) {
            throw new ServiceException("La materia con ID " + idMateria + " no existe.");
        }

        if (DocenteMateria.findFirst(
                "id_docente = ? AND id_materia = ?", idDocente, idMateria) != null) {
            throw new ServiceException(
                    "El docente ya está asociado a esa materia.");
        }

        DocenteMateria dm = new DocenteMateria();
        dm.setDocente(docente);
        dm.setMateria(materia);
        dm.setCargo(cargo.trim());
        dm.saveIt();

        return dm;
    }

    /**
     * Actualiza el cargo de una asociación docente-materia existente.
     *
     * @throws ServiceException si la asociación no existe.
     */
    public DocenteMateria actualizarCargo(int idAsociacion, String nuevoCargo) {
        DocenteMateria dm = DocenteMateria.findById(idAsociacion);
        if (dm == null) {
            throw new ServiceException(
                    "La asociación con ID " + idAsociacion + " no existe.");
        }
        dm.setCargo(nuevoCargo.trim());
        dm.saveIt();
        return dm;
    }

    /**
     * Elimina la asociación entre un docente y una materia.
     *
     * @throws ServiceException si la asociación no existe.
     */
    public void desasociar(int idAsociacion) {
        DocenteMateria dm = DocenteMateria.findById(idAsociacion);
        if (dm == null) {
            throw new ServiceException(
                    "La asociación con ID " + idAsociacion + " no existe.");
        }
        dm.delete();
    }

    /**
     * Retorna todas las materias asociadas a un docente como lista de mapas para Mustache.
     * Incluye nombre de la materia, código y cargo del docente en cada una.
     *
     * @throws ServiceException si el docente no existe.
     */
    public List<Map<String, Object>> getMateriasDeDocente(int idDocente) {
        Docente docente = Docente.findById(idDocente);
        if (docente == null) {
            throw new ServiceException("El docente con ID " + idDocente + " no existe.");
        }

        List<DocenteMateria> asociaciones =
                DocenteMateria.where("id_docente = ?", idDocente)
                        .include(Materia.class)
                        .load();

        List<Map<String, Object>> result = new ArrayList<>();
        for (DocenteMateria dm : asociaciones) {
            Materia m = dm.getMateria();
            Map<String, Object> row = new HashMap<>();
            row.put("idAsociacion", dm.getId());
            row.put("idMateria",    m.getId());
            row.put("nombre",       m.getNombre());
            row.put("descripcion",  m.getDescripcion());
            row.put("codigo",       m.getCodigo());
            row.put("cargo",        dm.getCargo());
            // Flags para el select de edición
            row.put("esTitular",  "Titular".equals(dm.getCargo()));
            row.put("esAdjunto",  "Adjunto".equals(dm.getCargo()));
            row.put("esJTP",      "JTP".equals(dm.getCargo()));
            row.put("esAyudante", "Ayudante".equals(dm.getCargo()));
            result.add(row);
        }
        return result;
    }

    /**
     * Retorna todas las materias que aún NO están asociadas al docente,
     * para poblar el select de alta de asociación.
     *
     * @throws ServiceException si el docente no existe.
     */
    public List<Map<String, Object>> getMateriasDisponibles(int idDocente) {
        Docente docente = Docente.findById(idDocente);
        if (docente == null) {
            throw new ServiceException("El docente con ID " + idDocente + " no existe.");
        }

        // IDs de materias ya asociadas al docente
        List<DocenteMateria> yaAsociadas =
                DocenteMateria.where("id_docente = ?", idDocente).load();

        List<Object> idsAsociados = new ArrayList<>();
        for (DocenteMateria dm : yaAsociadas) {
            idsAsociados.add(dm.get("id_materia"));
        }

        List<Materia> todasLasMaterias = Materia.findAll().load();
        List<Map<String, Object>> disponibles = new ArrayList<>();

        for (Materia m : todasLasMaterias) {
            if (!idsAsociados.contains(m.getId())) {
                Map<String, Object> row = new HashMap<>();
                row.put("id",     m.getId());
                row.put("nombre", m.getNombre());
                row.put("codigo", m.getCodigo());
                disponibles.add(row);
            }
        }
        return disponibles;
    }

    /**
     * Retorna los datos del docente como mapa, para el encabezado de la vista.
     *
     * @throws ServiceException si el docente no existe.
     */
    public Map<String, Object> getDatosDcente(int idDocente) {
        Docente docente = Docente.findById(idDocente);
        if (docente == null) {
            throw new ServiceException("El docente con ID " + idDocente + " no existe.");
        }
        Persona persona = docente.getPerson();

        Map<String, Object> data = new HashMap<>();
        data.put("idDocente",         docente.getId());
        data.put("nombreDocente",     persona.getNombre());
        data.put("apellidoDocente",   persona.getApellido());
        data.put("matriculaDocente",  docente.getMatricula());
        return data;
    }
}