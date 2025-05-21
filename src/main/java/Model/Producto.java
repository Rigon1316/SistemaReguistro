package Model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
@Entity
@Table(name = "producto")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private float precio;
    @Column(nullable = false, unique = true)
    private String codigo;
    @Column(nullable = false)
    private String nombre;
    @Column
    private int stock;
    
    public Producto() {
    }
    
    public Producto(float precio, String codigo, String nombre, int stock) {
        this.precio = precio;
        this.codigo = codigo;
        this.nombre = nombre;
        this.stock = stock;
    }
    
    public Producto(int id, float precio, String codigo, String nombre, int stock) {
        this.id = id;
        this.precio = precio;
        this.codigo = codigo;
        this.nombre = nombre;
        this.stock = stock;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public float getPrecio() {
        return precio;
    }
    
    public void setPrecio(float precio) {
        this.precio = precio;
    }
    
    public String getCodigo() {
        return codigo;
    }
    
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public int getStock() {
        return stock;
    }
    
    public void setStock(int stock) {
        this.stock = stock;
    }
    
    @Override
    public String toString() {
        return "Producto{" + "id=" + id + ", precio=" + precio + ", codigo=" + codigo + ", nombre=" + nombre + ", stock=" + stock + '}';
    }
}