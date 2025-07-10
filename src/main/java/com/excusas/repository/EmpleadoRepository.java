// src/main/java/com/excusas/repository/EmpleadoRepository.java
package com.excusas.repository;

import com.excusas.model.empleado.Empleado;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class EmpleadoRepository {

    private final List<Empleado> empleados = new ArrayList<>();

    public Empleado save(Empleado empleado) {
        // Remover si ya existe (actualización)
        empleados.removeIf(e -> e.getLegajo().equals(empleado.getLegajo()));
        empleados.add(empleado);
        return empleado;
    }

    public List<Empleado> findAll() {
        return new ArrayList<>(empleados);
    }

    public Optional<Empleado> findByLegajo(Integer legajo) {
        return empleados.stream()
                .filter(e -> e.getLegajo().equals(legajo))
                .findFirst();
    }

    public Optional<Empleado> findByNombre(String nombre) {
        return empleados.stream()
                .filter(e -> e.getNombre().equals(nombre))
                .findFirst();
    }

    public void deleteByLegajo(Integer legajo) {
        empleados.removeIf(e -> e.getLegajo().equals(legajo));
    }
}