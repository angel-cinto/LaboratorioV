package com.lab.apis.controller;

import com.lab.apis.model.Libro;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/libros")
public class LibroController {

    private final List<Libro> libros = new ArrayList<>();
    private final AtomicLong secuenciaId = new AtomicLong(6);

    public LibroController() {
        libros.add(new Libro(1L, "Cien anios de soledad", "Gabriel Garcia Marquez", "Novela", 180.0));
        libros.add(new Libro(2L, "El principito", "Antoine de Saint-Exupery", "Fabula", 95.0));
        libros.add(new Libro(3L, "1984", "George Orwell", "Ciencia ficcion", 150.0));
        libros.add(new Libro(4L, "Don Quijote de la Mancha", "Miguel de Cervantes", "Novela", 210.0));
        libros.add(new Libro(5L, "El alquimista", "Paulo Coelho", "Ficcion", 130.0));
    }

    @GetMapping
    public List<Libro> obtenerTodos() {
        return libros;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Libro> obtenerPorId(@PathVariable Long id) {
        return libros.stream()
                .filter(l -> l.getId().equals(id))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Libro> crear(@RequestBody Libro libro) {
        libro.setId(secuenciaId.getAndIncrement());
        libros.add(libro);
        return ResponseEntity.status(HttpStatus.CREATED).body(libro);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Libro> actualizar(@PathVariable Long id, @RequestBody Libro datos) {
        for (Libro libro : libros) {
            if (libro.getId().equals(id)) {
                libro.setTitulo(datos.getTitulo());
                libro.setAutor(datos.getAutor());
                libro.setGenero(datos.getGenero());
                libro.setPrecio(datos.getPrecio());
                return ResponseEntity.ok(libro);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Libro> actualizarParcial(@PathVariable Long id, @RequestBody Map<String, Object> cambios) {
        for (Libro libro : libros) {
            if (libro.getId().equals(id)) {
                if (cambios.containsKey("titulo")) {
                    libro.setTitulo((String) cambios.get("titulo"));
                }
                if (cambios.containsKey("autor")) {
                    libro.setAutor((String) cambios.get("autor"));
                }
                if (cambios.containsKey("genero")) {
                    libro.setGenero((String) cambios.get("genero"));
                }
                if (cambios.containsKey("precio")) {
                    libro.setPrecio(((Number) cambios.get("precio")).doubleValue());
                }
                return ResponseEntity.ok(libro);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        boolean eliminado = libros.removeIf(l -> l.getId().equals(id));
        return eliminado ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
