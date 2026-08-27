package com.lab.apis.controller;

import com.lab.apis.model.Estudiante;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/estudiantes")
public class EstudianteController {

    private final List<Estudiante> estudiantes = new ArrayList<>();
    private final AtomicLong secuenciaId = new AtomicLong(6);

    public EstudianteController() {
        estudiantes.add(new Estudiante(1L, "Angel", "Cinto", "Ingenieria en Sistemas", 21));
        estudiantes.add(new Estudiante(2L, "Maria", "Lopez", "Administracion de Empresas", 22));
        estudiantes.add(new Estudiante(3L, "Carlos", "Ruiz", "Ingenieria Civil", 20));
        estudiantes.add(new Estudiante(4L, "Ana", "Gomez", "Psicologia", 23));
        estudiantes.add(new Estudiante(5L, "Luis", "Perez", "Ingenieria en Sistemas", 19));
    }

    @GetMapping
    public List<Estudiante> obtenerTodos() {
        return estudiantes;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Estudiante> obtenerPorId(@PathVariable Long id) {
        return estudiantes.stream()
                .filter(e -> e.getId().equals(id))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Estudiante> crear(@RequestBody Estudiante estudiante) {
        estudiante.setId(secuenciaId.getAndIncrement());
        estudiantes.add(estudiante);
        return ResponseEntity.status(HttpStatus.CREATED).body(estudiante);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Estudiante> actualizar(@PathVariable Long id, @RequestBody Estudiante datos) {
        for (Estudiante estudiante : estudiantes) {
            if (estudiante.getId().equals(id)) {
                estudiante.setNombre(datos.getNombre());
                estudiante.setApellido(datos.getApellido());
                estudiante.setCarrera(datos.getCarrera());
                estudiante.setEdad(datos.getEdad());
                return ResponseEntity.ok(estudiante);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Estudiante> actualizarParcial(@PathVariable Long id, @RequestBody Map<String, Object> cambios) {
        for (Estudiante estudiante : estudiantes) {
            if (estudiante.getId().equals(id)) {
                if (cambios.containsKey("nombre")) {
                    estudiante.setNombre((String) cambios.get("nombre"));
                }
                if (cambios.containsKey("apellido")) {
                    estudiante.setApellido((String) cambios.get("apellido"));
                }
                if (cambios.containsKey("carrera")) {
                    estudiante.setCarrera((String) cambios.get("carrera"));
                }
                if (cambios.containsKey("edad")) {
                    estudiante.setEdad(((Number) cambios.get("edad")).intValue());
                }
                return ResponseEntity.ok(estudiante);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        boolean eliminado = estudiantes.removeIf(e -> e.getId().equals(id));
        return eliminado ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
