package com.excusas.model.empleado;

import com.excusas.model.excusa.Excusa;
import com.excusas.model.excusa.IExcusa;
import com.excusas.model.excusa.motivo.IMotivoExcusa;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "empleados")
public class Empleado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Column(nullable = false)
    private String nombre;

    @Email(message = "El email debe tener un formato válido")
    @NotBlank(message = "El email no puede estar vacío")
    @Column(nullable = false, unique = true)
    private String email;

    @NotNull(message = "El legajo no puede ser nulo")
    @Column(nullable = false, unique = true)
    private Integer legajo;

    @OneToMany(mappedBy = "empleado", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Excusa> excusas = new ArrayList<>();

    // Constructor por defecto para JPA
    public Empleado() {}

    public Empleado(String nombre, String email, int legajo) {
        this.nombre = nombre;
        this.email = email;
        this.legajo = legajo;
    }

    // Getters existentes
    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public Integer getLegajo() {
        return legajo;
    }

    public IExcusa generarExcusa(IMotivoExcusa motivo) {
        return new Excusa(motivo, this);
    }

    // Nuevos getters y setters para JPA
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLegajo(Integer legajo) {
        this.legajo = legajo;
    }

    public List<Excusa> getExcusas() {
        return excusas;
    }

    public void setExcusas(List<Excusa> excusas) {
        this.excusas = excusas;
    }

    public void agregarExcusa(Excusa excusa) {
        this.excusas.add(excusa);
        excusa.setEmpleado(this);
    }

    @Override
    public String toString() {
        return "Empleado{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", legajo=" + legajo +
                '}';
    }
}
