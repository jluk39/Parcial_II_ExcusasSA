// src/main/java/com/excusas/repository/ExcusaRepository.java
package com.excusas.repository;

import com.excusas.model.excusa.IExcusa;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ExcusaRepository {

    private final List<IExcusa> excusas = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public IExcusa save(IExcusa excusa) {
        // Asignar ID si no lo tiene
        if (excusa.getId() == null) {
            excusa.setId(idGenerator.getAndIncrement());
        }

        // Actualizar si ya existe
        excusas.removeIf(e -> e.getId().equals(excusa.getId()));
        excusas.add(excusa);

        return excusa;
    }

    public List<IExcusa> findAll() {
        return new ArrayList<>(excusas);
    }

    public Optional<IExcusa> findById(Long id) {
        return excusas.stream()
                .filter(e -> e.getId().equals(id))
                .findFirst();
    }

    public List<IExcusa> findByEmpleadoNombre(String nombre) {
        return excusas.stream()
                .filter(e -> e.getEmpleado().getNombre().equals(nombre))
                .toList();
    }
}