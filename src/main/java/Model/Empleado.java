package Model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "empleado")
@PrimaryKeyJoinColumn(name = "persona_id")
public class Empleado extends Persona {

    @Column(nullable = false)
    private double salario;

    @Column(nullable = false)
    private String turno;

    @Column(name = "fecha_contratacion", nullable = false)
    private LocalDate fechaContratacion;

    @ManyToMany(fetch = FetchType.EAGER) // 👈 Cambio realizado aquí
    @JoinTable(
        name = "empleado_roles",
        joinColumns = @JoinColumn(name = "empleado_id"),
        inverseJoinColumns = @JoinColumn(name = "rol_nombre")
    )
    private List<Rol> roles = new ArrayList<>();

    public Empleado() {
        super();
    }

    public Empleado(String nombre, String apellido, String correo, String cedula,
                    LocalDate fecha_de_nacimiento, double salario, String turno,
                    LocalDate fechaContratacion) {
        super(nombre, apellido, correo, cedula, fecha_de_nacimiento, null);
        this.salario = salario;
        this.turno = turno;
        this.fechaContratacion = fechaContratacion;
    }

    public Empleado(String nombre, String apellido, String correo, String cedula,
                    LocalDate fecha_de_nacimiento, String direccion, double salario,
                    String turno, LocalDate fechaContratacion) {
        super(nombre, apellido, correo, cedula, fecha_de_nacimiento, direccion);
        this.salario = salario;
        this.turno = turno;
        this.fechaContratacion = fechaContratacion;
    }

    public Empleado(int id, String nombre, String apellido, String correo, String cedula,
                    LocalDate fecha_de_nacimiento, String direccion, double salario,
                    String turno, LocalDate fechaContratacion) {
        super(id, nombre, apellido, correo, cedula, fecha_de_nacimiento, direccion);
        this.salario = salario;
        this.turno = turno;
        this.fechaContratacion = fechaContratacion;
    }

    // Getters y Setters
    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public LocalDate getFechaContratacion() {
        return fechaContratacion;
    }

    public void setFechaContratacion(LocalDate fechaContratacion) {
        this.fechaContratacion = fechaContratacion;
    }

    public List<Rol> getRoles() {
        return roles;
    }

    public void setRoles(List<Rol> roles) {
        this.roles = roles;
    }

    // Métodos para manejo de roles
    public void addRol(Rol rol) {
        if (!roles.contains(rol)) {
            roles.add(rol);
        }
    }

    public void removeRol(Rol rol) {
        roles.remove(rol);
    }

    public boolean hasRol(Rol rol) {
        return roles.contains(rol);
    }

    public boolean hasRolByNombre(String nombreRol) {
        return roles.stream().anyMatch(rol -> rol.getNombre().equals(nombreRol));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Empleado)) return false;
        if (!super.equals(o)) return false;
        Empleado empleado = (Empleado) o;
        return Double.compare(empleado.salario, salario) == 0 &&
               Objects.equals(turno, empleado.turno) &&
               Objects.equals(fechaContratacion, empleado.fechaContratacion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), salario, turno, fechaContratacion);
    }

    @Override
    public String toString() {
        return "Empleado{" +
                "id=" + getId() +
                ", nombre='" + getNombre() + '\'' +
                ", apellido='" + getApellido() + '\'' +
                ", correo='" + getCorreo() + '\'' +
                ", cedula='" + getCedula() + '\'' +
                ", fecha_de_nacimiento=" + getFecha_de_nacimiento() +
                ", direccion='" + getDireccion() + '\'' +
                ", salario=" + salario +
                ", turno='" + turno + '\'' +
                ", fechaContratacion=" + fechaContratacion +
                '}';
    }
}
