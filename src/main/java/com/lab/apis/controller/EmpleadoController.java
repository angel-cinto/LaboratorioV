package com.lab.apis.controller;

import com.lab.apis.model.Empleado;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/empleados")
public class EmpleadoController {

    private final List<Empleado> empleados = new ArrayList<>();
    private final AtomicLong secuenciaId = new AtomicLong(6);

    public EmpleadoController() {
        empleados.add(new Empleado(1L, "Angel Cinto", "Desarrollador", 6000.0, "Tecnologia"));
        empleados.add(new Empleado(2L, "Maria Lopez", "Analista", 5200.0, "Finanzas"));
        empleados.add(new Empleado(3L, "Carlos Ruiz", "Gerente", 9500.0, "Ventas"));
        empleados.add(new Empleado(4L, "Ana Gomez", "Recursos Humanos", 4800.0, "Recursos Humanos"));
        empleados.add(new Empleado(5L, "Luis Perez", "Soporte Tecnico", 4200.0, "Tecnologia"));
    }

    @GetMapping
    public List<Empleado> obtenerTodos() {
        return empleados;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Empleado> obtenerPorId(@PathVariable Long id) {
        return empleados.stream()
                .filter(e -> e.getId().equals(id))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Empleado> crear(@RequestBody Empleado empleado) {
        empleado.setId(secuenciaId.getAndIncrement());
        empleados.add(empleado);
        return ResponseEntity.status(HttpStatus.CREATED).body(empleado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Empleado> actualizar(@PathVariable Long id, @RequestBody Empleado datos) {
        for (Empleado empleado : empleados) {
            if (empleado.getId().equals(id)) {
                empleado.setNombre(datos.getNombre());
                empleado.setPuesto(datos.getPuesto());
                empleado.setSalario(datos.getSalario());
                empleado.setDepartamento(datos.getDepartamento());
                return ResponseEntity.ok(empleado);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Empleado> actualizarParcial(@PathVariable Long id, @RequestBody Map<String, Object> cambios) {
        for (Empleado empleado : empleados) {
            if (empleado.getId().equals(id)) {
                if (cambios.containsKey("nombre")) {
                    empleado.setNombre((String) cambios.get("nombre"));
                }
                if (cambios.containsKey("puesto")) {
                    empleado.setPuesto((String) cambios.get("puesto"));
                }
                if (cambios.containsKey("salario")) {
                    empleado.setSalario(((Number) cambios.get("salario")).doubleValue());
                }
                if (cambios.containsKey("departamento")) {
                    empleado.setDepartamento((String) cambios.get("departamento"));
                }
                return ResponseEntity.ok(empleado);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        boolean eliminado = empleados.removeIf(e -> e.getId().equals(id));
        return eliminado ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
