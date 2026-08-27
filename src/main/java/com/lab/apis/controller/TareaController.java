package com.lab.apis.controller;

import com.lab.apis.model.Tarea;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/tareas")
public class TareaController {

    private final List<Tarea> tareas = new ArrayList<>();
    private final AtomicLong secuenciaId = new AtomicLong(6);

    public TareaController() {
        tareas.add(new Tarea(1L, "Preparar presentacion", "Presentacion del Laboratorio V", "ALTA", false));
        tareas.add(new Tarea(2L, "Revisar correos", "Revisar bandeja de entrada", "BAJA", true));
        tareas.add(new Tarea(3L, "Actualizar documentacion", "Actualizar README del proyecto", "MEDIA", false));
        tareas.add(new Tarea(4L, "Probar endpoints", "Probar las APIs con Postman", "ALTA", false));
        tareas.add(new Tarea(5L, "Hacer commit", "Subir avances al repositorio", "MEDIA", true));
    }

    @GetMapping
    public List<Tarea> obtenerTodos() {
        return tareas;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tarea> obtenerPorId(@PathVariable Long id) {
        return tareas.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Tarea> crear(@RequestBody Tarea tarea) {
        tarea.setId(secuenciaId.getAndIncrement());
        tareas.add(tarea);
        return ResponseEntity.status(HttpStatus.CREATED).body(tarea);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tarea> actualizar(@PathVariable Long id, @RequestBody Tarea datos) {
        for (Tarea tarea : tareas) {
            if (tarea.getId().equals(id)) {
                tarea.setTitulo(datos.getTitulo());
                tarea.setDescripcion(datos.getDescripcion());
                tarea.setPrioridad(datos.getPrioridad());
                tarea.setCompletada(datos.isCompletada());
                return ResponseEntity.ok(tarea);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Tarea> actualizarParcial(@PathVariable Long id, @RequestBody Map<String, Object> cambios) {
        for (Tarea tarea : tareas) {
            if (tarea.getId().equals(id)) {
                if (cambios.containsKey("titulo")) {
                    tarea.setTitulo((String) cambios.get("titulo"));
                }
                if (cambios.containsKey("descripcion")) {
                    tarea.setDescripcion((String) cambios.get("descripcion"));
                }
                if (cambios.containsKey("prioridad")) {
                    tarea.setPrioridad((String) cambios.get("prioridad"));
                }
                if (cambios.containsKey("completada")) {
                    tarea.setCompletada((Boolean) cambios.get("completada"));
                }
                return ResponseEntity.ok(tarea);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        boolean eliminado = tareas.removeIf(t -> t.getId().equals(id));
        return eliminado ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
