//package Model;
//
//import jakarta.persistence.CascadeType;
//import jakarta.persistence.Entity;
//import jakarta.persistence.FetchType;
//import jakarta.persistence.OneToOne;
//import jakarta.persistence.Table;
//import jakarta.persistence.ElementCollection;
//import jakarta.persistence.CollectionTable;
//import jakarta.persistence.Column;
//import jakarta.persistence.JoinColumn;
//
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//
//@Entity
//@Table(name = "empleado")
//public class Empleado extends Persona {
//
//    @Column(nullable = false)
//    private String direccion;
//    @Column(nullable = false)
//    private double salario;
//
//    @ElementCollection(fetch = FetchType.EAGER)
//    @CollectionTable(name = "empleado_roles", joinColumns = @JoinColumn(name = "empleado_id"))
//    @Column(name = "rol")
//    private List<String> roles = new ArrayList<>();
//
//    @OneToOne(mappedBy = "empleado", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
//    private Persona usuario;
//
//    public Empleado() {
//    }
//
//    public Empleado(int id, String nombre, String apellido, String correo, String cedula, LocalDate fecha_de_nacimiento, String Direccion) {
//        super(id, nombre, apellido, correo, cedula, fecha_de_nacimiento, Direccion);
//    }
//
//    public Empleado(String direccion, List<String> roles, double salario, Persona usuario,
//            int id, String nombre, String apellido, String correo,
//            String cedula, LocalDate fechaNacimiento) {
//        super(id, nombre, apellido, correo, cedula, fechaNacimiento, direccion);
//        this.salario = salario;
//        this.roles = roles;
//        this.usuario = usuario;
//    }
//
//    public String getDireccion() {
//        return direccion;
//    }
//
//    public void setDireccion(String direccion) {
//        this.direccion = direccion;
//    }
//
//    public List<String> getRoles() {
//        return roles;
//    }
//
//    public void setRoles(List<String> roles) {
//        this.roles = roles;
//    }
//
//    public void addRol(String rol) {
//        if (!roles.contains(rol)) {
//            roles.add(rol);
//        }
//    }
//
//    public void removeRol(String rol) {
//        roles.remove(rol);
//    }
//
//    public double getSalario() {
//        return salario;
//    }
//
//    public void setSalario(double salario) {
//        this.salario = salario;
//    }
//
//    public Persona getUsuario() {
//        return usuario;
//    }
//
//    public void setUsuario(Persona usuario) {
//        this.usuario = usuario;
//    }
//
//    @Override
//    public String toString() {
//        return "Empleado{" + "direccion=" + direccion + ", salario=" + salario + ", roles=" + roles + ", usuario=" + usuario + '}';
//    }
//
//    public boolean hasRol(String rol) {
//        return roles.contains(rol);
//    }
//
//}
