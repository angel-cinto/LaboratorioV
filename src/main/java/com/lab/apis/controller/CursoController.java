package com.lab.apis.controller;

import com.lab.apis.model.Curso;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/cursos")
public class CursoController {

    private final List<Curso> cursos = new ArrayList<>();
    private final AtomicLong secuenciaId = new AtomicLong(6);

    public CursoController() {
        cursos.add(new Curso(1L, "Programacion 2", "Fundamentos de POO y Java", 4, "Presencial"));
        cursos.add(new Curso(2L, "Bases de Datos", "Modelado y SQL", 3, "Virtual"));
        cursos.add(new Curso(3L, "Redes", "Fundamentos de redes de computadoras", 3, "Presencial"));
        cursos.add(new Curso(4L, "Ingenieria de Software", "Procesos y metodologias agiles", 4, "Hibrida"));
        cursos.add(new Curso(5L, "Matematica Discreta", "Logica y estructuras discretas", 3, "Virtual"));
    }

    @GetMapping
    public List<Curso> obtenerTodos() {
        return cursos;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Curso> obtenerPorId(@PathVariable Long id) {
        return cursos.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Curso> crear(@RequestBody Curso curso) {
        curso.setId(secuenciaId.getAndIncrement());
        cursos.add(curso);
        return ResponseEntity.status(HttpStatus.CREATED).body(curso);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Curso> actualizar(@PathVariable Long id, @RequestBody Curso datos) {
        for (Curso curso : cursos) {
            if (curso.getId().equals(id)) {
                curso.setNombre(datos.getNombre());
                curso.setDescripcion(datos.getDescripcion());
                curso.setCreditos(datos.getCreditos());
                curso.setModalidad(datos.getModalidad());
                return ResponseEntity.ok(curso);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Curso> actualizarParcial(@PathVariable Long id, @RequestBody Map<String, Object> cambios) {
        for (Curso curso : cursos) {
            if (curso.getId().equals(id)) {
                if (cambios.containsKey("nombre")) {
                    curso.setNombre((String) cambios.get("nombre"));
                }
                if (cambios.containsKey("descripcion")) {
                    curso.setDescripcion((String) cambios.get("descripcion"));
                }
                if (cambios.containsKey("creditos")) {
                    curso.setCreditos(((Number) cambios.get("creditos")).intValue());
                }
                if (cambios.containsKey("modalidad")) {
                    curso.setModalidad((String) cambios.get("modalidad"));
                }
                return ResponseEntity.ok(curso);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        boolean eliminado = cursos.removeIf(c -> c.getId().equals(id));
        return eliminado ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
