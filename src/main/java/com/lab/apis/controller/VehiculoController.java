package com.lab.apis.controller;

import com.lab.apis.model.Vehiculo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/vehiculos")
public class VehiculoController {

    private final List<Vehiculo> vehiculos = new ArrayList<>();
    private final AtomicLong secuenciaId = new AtomicLong(6);

    public VehiculoController() {
        vehiculos.add(new Vehiculo(1L, "Toyota", "Corolla", 2022, 145000.0));
        vehiculos.add(new Vehiculo(2L, "Honda", "Civic", 2021, 138000.0));
        vehiculos.add(new Vehiculo(3L, "Yamaha", "FZ", 2023, 22000.0));
        vehiculos.add(new Vehiculo(4L, "Mazda", "CX-5", 2020, 165000.0));
        vehiculos.add(new Vehiculo(5L, "Chevrolet", "Spark", 2019, 65000.0));
    }

    @GetMapping
    public List<Vehiculo> obtenerTodos() {
        return vehiculos;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vehiculo> obtenerPorId(@PathVariable Long id) {
        return vehiculos.stream()
                .filter(v -> v.getId().equals(id))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Vehiculo> crear(@RequestBody Vehiculo vehiculo) {
        vehiculo.setId(secuenciaId.getAndIncrement());
        vehiculos.add(vehiculo);
        return ResponseEntity.status(HttpStatus.CREATED).body(vehiculo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vehiculo> actualizar(@PathVariable Long id, @RequestBody Vehiculo datos) {
        for (Vehiculo vehiculo : vehiculos) {
            if (vehiculo.getId().equals(id)) {
                vehiculo.setMarca(datos.getMarca());
                vehiculo.setModelo(datos.getModelo());
                vehiculo.setAnio(datos.getAnio());
                vehiculo.setPrecio(datos.getPrecio());
                return ResponseEntity.ok(vehiculo);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Vehiculo> actualizarParcial(@PathVariable Long id, @RequestBody Map<String, Object> cambios) {
        for (Vehiculo vehiculo : vehiculos) {
            if (vehiculo.getId().equals(id)) {
                if (cambios.containsKey("marca")) {
                    vehiculo.setMarca((String) cambios.get("marca"));
                }
                if (cambios.containsKey("modelo")) {
                    vehiculo.setModelo((String) cambios.get("modelo"));
                }
                if (cambios.containsKey("anio")) {
                    vehiculo.setAnio(((Number) cambios.get("anio")).intValue());
                }
                if (cambios.containsKey("precio")) {
                    vehiculo.setPrecio(((Number) cambios.get("precio")).doubleValue());
                }
                return ResponseEntity.ok(vehiculo);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        boolean eliminado = vehiculos.removeIf(v -> v.getId().equals(id));
        return eliminado ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
