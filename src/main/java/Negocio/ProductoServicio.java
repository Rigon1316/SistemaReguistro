package Negocio;

import Model.Producto;
import Datos.ProductoDAO;
import java.util.List;

public class ProductoServicio {

    private final ProductoDAO productoDAO;

    public ProductoServicio() {
        this.productoDAO = new ProductoDAO();
    }

    public int agregarNuevoProducto(Producto producto) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo.");
        }

        if (producto.getPrecio() <= 0) {
            System.out.println("El precio debe ser mayor que cero.");
            return 0;
        }

        if (producto.getStock() < 0) {
            System.out.println("El stock no puede ser negativo.");
            return 0;
        }

        return productoDAO.verificarAgregarProducto(producto);
    }

    
    public List<Producto> listarProductos() {
        return productoDAO.listarProductos();
    }

    
    public Producto buscarPorCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El código de producto no puede estar vacío.");
        }
        return productoDAO.buscarPorCodigo(codigo);
    }

    
    public Producto buscarPorId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID del producto debe ser mayor que cero.");
        }
        return productoDAO.buscarPorId(id);
    }

    
    public boolean actualizarProducto(Producto producto) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo.");
        }
        if (producto.getId() <= 0) {
            throw new IllegalArgumentException("El ID del producto debe ser válido.");
        }
        if (producto.getPrecio() <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor que cero.");
        }
        if (producto.getStock() < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo.");
        }

        return productoDAO.actualizarProducto(producto);
    }

    
    public boolean eliminarProducto(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID del producto debe ser mayor que cero.");
        }
        return productoDAO.eliminarProducto(id);
    }

    
    public List<Producto> buscarProductos(String criterio) {
        if (criterio == null || criterio.trim().isEmpty()) {
            return listarProductos(); 
        }
        return productoDAO.buscarProductos(criterio);
    }

    
    public boolean actualizarStock(int idProducto, int nuevoStock) {
        if (idProducto <= 0) {
            throw new IllegalArgumentException("El ID del producto debe ser mayor que cero.");
        }
        if (nuevoStock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo.");
        }

        return productoDAO.actualizarStock(idProducto, nuevoStock);
    }
}
