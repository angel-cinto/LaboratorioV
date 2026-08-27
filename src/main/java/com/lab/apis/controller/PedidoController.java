package com.lab.apis.controller;

import com.lab.apis.model.Pedido;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final List<Pedido> pedidos = new ArrayList<>();
    private final AtomicLong secuenciaId = new AtomicLong(6);

    public PedidoController() {
        pedidos.add(new Pedido(1L, "Angel Cinto", "Laptop Lenovo", 1, 5500.0, "PENDIENTE"));
        pedidos.add(new Pedido(2L, "Maria Lopez", "Mouse inalambrico", 2, 240.0, "ENVIADO"));
        pedidos.add(new Pedido(3L, "Carlos Ruiz", "Silla ergonomica", 1, 950.0, "ENTREGADO"));
        pedidos.add(new Pedido(4L, "Ana Gomez", "Cuaderno profesional", 5, 125.0, "PENDIENTE"));
        pedidos.add(new Pedido(5L, "Luis Perez", "Escritorio de madera", 1, 850.0, "ENVIADO"));
    }

    @GetMapping
    public List<Pedido> obtenerTodos() {
        return pedidos;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> obtenerPorId(@PathVariable Long id) {
        return pedidos.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Pedido> crear(@RequestBody Pedido pedido) {
        pedido.setId(secuenciaId.getAndIncrement());
        pedidos.add(pedido);
        return ResponseEntity.status(HttpStatus.CREATED).body(pedido);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pedido> actualizar(@PathVariable Long id, @RequestBody Pedido datos) {
        for (Pedido pedido : pedidos) {
            if (pedido.getId().equals(id)) {
                pedido.setCliente(datos.getCliente());
                pedido.setProducto(datos.getProducto());
                pedido.setCantidad(datos.getCantidad());
                pedido.setTotal(datos.getTotal());
                pedido.setEstado(datos.getEstado());
                return ResponseEntity.ok(pedido);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Pedido> actualizarParcial(@PathVariable Long id, @RequestBody Map<String, Object> cambios) {
        for (Pedido pedido : pedidos) {
            if (pedido.getId().equals(id)) {
                if (cambios.containsKey("cliente")) {
                    pedido.setCliente((String) cambios.get("cliente"));
                }
                if (cambios.containsKey("producto")) {
                    pedido.setProducto((String) cambios.get("producto"));
                }
                if (cambios.containsKey("cantidad")) {
                    pedido.setCantidad(((Number) cambios.get("cantidad")).intValue());
                }
                if (cambios.containsKey("total")) {
                    pedido.setTotal(((Number) cambios.get("total")).doubleValue());
                }
                if (cambios.containsKey("estado")) {
                    pedido.setEstado((String) cambios.get("estado"));
                }
                return ResponseEntity.ok(pedido);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        boolean eliminado = pedidos.removeIf(p -> p.getId().equals(id));
        return eliminado ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
