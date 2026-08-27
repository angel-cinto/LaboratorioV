package com.lab.apis.controller;

import com.lab.apis.model.Cliente;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final List<Cliente> clientes = new ArrayList<>();
    private final AtomicLong secuenciaId = new AtomicLong(6);

    public ClienteController() {
        clientes.add(new Cliente(1L, "Angel", "Cinto", "angel.cinto@correo.com", "5555-1001"));
        clientes.add(new Cliente(2L, "Maria", "Lopez", "maria.lopez@correo.com", "5555-1002"));
        clientes.add(new Cliente(3L, "Carlos", "Ruiz", "carlos.ruiz@correo.com", "5555-1003"));
        clientes.add(new Cliente(4L, "Ana", "Gomez", "ana.gomez@correo.com", "5555-1004"));
        clientes.add(new Cliente(5L, "Luis", "Perez", "luis.perez@correo.com", "5555-1005"));
    }

    @GetMapping
    public List<Cliente> obtenerTodos() {
        return clientes;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> obtenerPorId(@PathVariable Long id) {
        return clientes.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Cliente> crear(@RequestBody Cliente cliente) {
        cliente.setId(secuenciaId.getAndIncrement());
        clientes.add(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(cliente);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> actualizar(@PathVariable Long id, @RequestBody Cliente datos) {
        for (Cliente cliente : clientes) {
            if (cliente.getId().equals(id)) {
                cliente.setNombre(datos.getNombre());
                cliente.setApellido(datos.getApellido());
                cliente.setCorreo(datos.getCorreo());
                cliente.setTelefono(datos.getTelefono());
                return ResponseEntity.ok(cliente);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Cliente> actualizarParcial(@PathVariable Long id, @RequestBody Map<String, Object> cambios) {
        for (Cliente cliente : clientes) {
            if (cliente.getId().equals(id)) {
                if (cambios.containsKey("nombre")) {
                    cliente.setNombre((String) cambios.get("nombre"));
                }
                if (cambios.containsKey("apellido")) {
                    cliente.setApellido((String) cambios.get("apellido"));
                }
                if (cambios.containsKey("correo")) {
                    cliente.setCorreo((String) cambios.get("correo"));
                }
                if (cambios.containsKey("telefono")) {
                    cliente.setTelefono((String) cambios.get("telefono"));
                }
                return ResponseEntity.ok(cliente);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        boolean eliminado = clientes.removeIf(c -> c.getId().equals(id));
        return eliminado ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
