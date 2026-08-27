package com.lab.apis.controller;

import com.lab.apis.model.Producto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final List<Producto> productos = new ArrayList<>();
    private final AtomicLong secuenciaId = new AtomicLong(6);

    public ProductoController() {
        productos.add(new Producto(1L, "Laptop Lenovo", 5500.0, "Electronica"));
        productos.add(new Producto(2L, "Mouse inalambrico", 120.0, "Electronica"));
        productos.add(new Producto(3L, "Escritorio de madera", 850.0, "Muebles"));
        productos.add(new Producto(4L, "Silla ergonomica", 950.0, "Muebles"));
        productos.add(new Producto(5L, "Cuaderno profesional", 25.0, "Papeleria"));
    }

    @GetMapping
    public List<Producto> obtenerTodos() {
        return productos;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable Long id) {
        return productos.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Producto> crear(@RequestBody Producto producto) {
        producto.setId(secuenciaId.getAndIncrement());
        productos.add(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(producto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(@PathVariable Long id, @RequestBody Producto datos) {
        for (Producto producto : productos) {
            if (producto.getId().equals(id)) {
                producto.setNombre(datos.getNombre());
                producto.setPrecio(datos.getPrecio());
                producto.setCategoria(datos.getCategoria());
                return ResponseEntity.ok(producto);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Producto> actualizarParcial(@PathVariable Long id, @RequestBody Map<String, Object> cambios) {
        for (Producto producto : productos) {
            if (producto.getId().equals(id)) {
                if (cambios.containsKey("nombre")) {
                    producto.setNombre((String) cambios.get("nombre"));
                }
                if (cambios.containsKey("precio")) {
                    producto.setPrecio(((Number) cambios.get("precio")).doubleValue());
                }
                if (cambios.containsKey("categoria")) {
                    producto.setCategoria((String) cambios.get("categoria"));
                }
                return ResponseEntity.ok(producto);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        boolean eliminado = productos.removeIf(p -> p.getId().equals(id));
        return eliminado ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
