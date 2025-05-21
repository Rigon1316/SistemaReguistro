package Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "fidelidad")
public class Fidelidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "cliente_id", nullable = false, unique = true)
    private Cliente cliente;

    private int puntosAcumulados;

    private int puntosCanjeados;

    private LocalDate fechaAfiliacion;

    public Fidelidad() {

    }

    public Fidelidad(Cliente cliente, int puntosAcumulados, int puntosCanjeados,
            LocalDate fechaAfiliacion) {
        this.cliente = cliente;
        this.puntosAcumulados = puntosAcumulados;
        this.puntosCanjeados = puntosCanjeados;
        this.fechaAfiliacion = fechaAfiliacion;
    }

    public Fidelidad(int id, Cliente cliente, int puntosAcumulados, int puntosCanjeados,
            LocalDate fechaAfiliacion) {
        this.id = id;
        this.cliente = cliente;
        this.puntosAcumulados = puntosAcumulados;
        this.puntosCanjeados = puntosCanjeados;
        this.fechaAfiliacion = fechaAfiliacion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public int getPuntosAcumulados() {
        return puntosAcumulados;
    }

    public void setPuntosAcumulados(int puntosAcumulados) {
        this.puntosAcumulados = puntosAcumulados;
    }

    public int getPuntosCanjeados() {
        return puntosCanjeados;
    }

    public void setPuntosCanjeados(int puntosCanjeados) {
        this.puntosCanjeados = puntosCanjeados;
    }

    public LocalDate getFechaAfiliacion() {
        return fechaAfiliacion;
    }

    public void setFechaAfiliacion(LocalDate fechaAfiliacion) {
        this.fechaAfiliacion = fechaAfiliacion;
    }

    @Override
    public String toString() {
        return "Fidelidad{" + "id=" + id + ", cliente=" + cliente + ", puntosAcumulados=" + puntosAcumulados + ", puntosCanjeados=" + puntosCanjeados + ", fechaAfiliacion=" + fechaAfiliacion + '}';
    }

}
