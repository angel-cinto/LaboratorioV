package com.lab.apis.controller;

import com.lab.apis.model.Pelicula;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/peliculas")
public class PeliculaController {

    private final List<Pelicula> peliculas = new ArrayList<>();
    private final AtomicLong secuenciaId = new AtomicLong(6);

    public PeliculaController() {
        peliculas.add(new Pelicula(1L, "El Padrino", "Francis Ford Coppola", "Drama", 1972));
        peliculas.add(new Pelicula(2L, "Inception", "Christopher Nolan", "Ciencia ficcion", 2010));
        peliculas.add(new Pelicula(3L, "Pulp Fiction", "Quentin Tarantino", "Crimen", 1994));
        peliculas.add(new Pelicula(4L, "Coco", "Lee Unkrich", "Animacion", 2017));
        peliculas.add(new Pelicula(5L, "Interstellar", "Christopher Nolan", "Ciencia ficcion", 2014));
    }

    @GetMapping
    public List<Pelicula> obtenerTodos() {
        return peliculas;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pelicula> obtenerPorId(@PathVariable Long id) {
        return peliculas.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Pelicula> crear(@RequestBody Pelicula pelicula) {
        pelicula.setId(secuenciaId.getAndIncrement());
        peliculas.add(pelicula);
        return ResponseEntity.status(HttpStatus.CREATED).body(pelicula);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pelicula> actualizar(@PathVariable Long id, @RequestBody Pelicula datos) {
        for (Pelicula pelicula : peliculas) {
            if (pelicula.getId().equals(id)) {
                pelicula.setTitulo(datos.getTitulo());
                pelicula.setDirector(datos.getDirector());
                pelicula.setGenero(datos.getGenero());
                pelicula.setAnio(datos.getAnio());
                return ResponseEntity.ok(pelicula);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Pelicula> actualizarParcial(@PathVariable Long id, @RequestBody Map<String, Object> cambios) {
        for (Pelicula pelicula : peliculas) {
            if (pelicula.getId().equals(id)) {
                if (cambios.containsKey("titulo")) {
                    pelicula.setTitulo((String) cambios.get("titulo"));
                }
                if (cambios.containsKey("director")) {
                    pelicula.setDirector((String) cambios.get("director"));
                }
                if (cambios.containsKey("genero")) {
                    pelicula.setGenero((String) cambios.get("genero"));
                }
                if (cambios.containsKey("anio")) {
                    pelicula.setAnio(((Number) cambios.get("anio")).intValue());
                }
                return ResponseEntity.ok(pelicula);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        boolean eliminado = peliculas.removeIf(p -> p.getId().equals(id));
        return eliminado ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
