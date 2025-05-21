package Model;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "rol")
public class Rol {
    @Id
    @Column(name = "nombre", nullable = false, unique = true)
    private String nombre;
    
    @OneToMany(mappedBy = "rol", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Empleado> empleados;
    
    @OneToMany(mappedBy = "rol", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Permiso> permisos;
    
    public Rol() {
    }
    
    public Rol(String nombre) {
        this.nombre = nombre;
    }
    
    public Rol(String nombre, List<Empleado> empleados, List<Permiso> permisos) {
        this.nombre = nombre;
        this.empleados = empleados;
        this.permisos = permisos;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public List<Empleado> getEmpleados() {
        return empleados;
    }
    
    public void setEmpleados(List<Empleado> empleados) {
        this.empleados = empleados;
    }
    
    public List<Permiso> getPermisos() {
        return permisos;
    }
    
    public void setPermisos(List<Permiso> permisos) {
        this.permisos = permisos;
    }
    
    @Override
    public String toString() {
        
        return nombre;
    }
}