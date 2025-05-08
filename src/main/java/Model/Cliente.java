package Model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "cliente")
public class Cliente extends Persona {
    
    @OneToOne(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Fidelidad fidelidad;
    
    public Cliente() {
    }
    
    
    public Cliente(int id, Fidelidad fidelidad, String nombre, String apellido, String correo, 
                  String cedula, LocalDate fecha_de_nacimiento, String direccion) {
        super(id, nombre, apellido, correo, cedula, fecha_de_nacimiento, direccion);
        this.fidelidad = fidelidad;
    }
    
    public Cliente(Fidelidad fidelidad, String nombre, String apellido, String correo, 
                  String cedula, LocalDate fecha_de_nacimiento, String direccion) {
        super(nombre, apellido, correo, cedula, fecha_de_nacimiento, direccion);
        this.fidelidad = fidelidad;
    }
    
    public Cliente(String nombre, String apellido, String correo, String cedula, 
                  LocalDate fecha_de_nacimiento, String direccion) {
        super(nombre, apellido, correo, cedula, fecha_de_nacimiento, direccion);
    }
    
    public Fidelidad getFidelidad() {
        return fidelidad;
    }
    
    public void setFidelidad(Fidelidad fidelidad) {
        this.fidelidad = fidelidad;
    }
    
    @Override
    public String toString() {
        return "Cliente{" + super.toString() + ", fidelidad=" + fidelidad + '}';
    }
}