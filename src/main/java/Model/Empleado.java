package Model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "empleado")
public class Empleado extends Persona {

    @ManyToOne(optional = false)
    @JoinColumn(name = "rol_nombre", referencedColumnName = "nombre")
    private Rol rol;

    @Column(nullable = false)
    private LocalDate fechaIngreso;

    @Column
    private boolean activo;

    @Column
    private String clave;

    public Empleado() {
    }

    public Empleado(String nombre, String apellido, String numIdentificacion,
                    String correo, LocalDate fechaNacimiento, Rol rol,
                    LocalDate fechaIngreso, boolean activo) {
        super(nombre, apellido, numIdentificacion, correo, fechaNacimiento);
        this.rol = rol;
        this.fechaIngreso = fechaIngreso;
        this.activo = activo;
        this.clave = numIdentificacion;
    }

    public Empleado(int id, String nombre, String apellido, String numIdentificacion,
                    String correo, LocalDate fechaNacimiento, Rol rol,
                    LocalDate fechaIngreso, boolean activo, String clave) {
        super(nombre, apellido, numIdentificacion, correo, fechaNacimiento);
        this.rol = rol;
        this.fechaIngreso = fechaIngreso;
        this.activo = activo;
        this.clave = clave;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
public String toString() {
    return "Empleado{" +
           "id=" + getId() +
           ", nombre=" + getNombre() +
           ", apellido=" + getApellido() +
           ", rol=" + (rol != null ? rol.getNombre() : "null") +
           ", fechaIngreso=" + fechaIngreso +
           ", activo=" + activo +
           "}";
}
}
