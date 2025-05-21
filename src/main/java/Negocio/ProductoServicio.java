package Negocio;

import Model.Producto;
import Datos.ProductoDAO;
import java.util.List;

public class ProductoServicio {

    private final ProductoDAO productoDAO;

    public ProductoServicio() {
        this.productoDAO = new ProductoDAO();
    }

    public int AgregarNuevoProducto(Producto nuevoProducto) {
        return this.productoDAO.RegistrarProducto(nuevoProducto);
    }

    public List<Producto> ListarProductos() {
        return this.productoDAO.ListarProductosRegistrados();
    }

    public boolean EliminarProductoPorId(int numId) {
        return this.productoDAO.EliminarProducto(numId);
    }

    public boolean ActualizarProducto(int id, Producto producto) {
        return this.productoDAO.ActualizarProducto(id, producto);
    }
}
