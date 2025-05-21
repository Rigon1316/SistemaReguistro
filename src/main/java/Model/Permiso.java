package Model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "permiso")
public class Permiso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String descripcion;

    @ManyToOne(optional = false)
    @JoinColumn(name = "rol_nombre", referencedColumnName = "nombre")
    private Rol rol;

    @ManyToOne(optional = false)
    @JoinColumn(name = "modulo_id")
    private Modulo modulo;

    public Permiso() {}

    public Permiso(String descripcion, Rol rol, Modulo modulo) {
        this.descripcion = descripcion;
        this.rol = rol;
        this.modulo = modulo;
    }

    public Long getId() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public Modulo getModulo() {
        return modulo;
    }

    public void setModulo(Modulo modulo) {
        this.modulo = modulo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Permiso)) return false;
        Permiso permiso = (Permiso) o;
        return Objects.equals(id, permiso.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Permiso{" +
                "id=" + id +
                ", descripcion='" + descripcion + '\'' +
                ", modulo=" + (modulo != null ? modulo.getNombre() : null) +
                ", rol=" + (rol != null ? rol.getNombre() : null) +
                '}';
    }
}
