package Datos;

import Model.Producto;
import Util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import java.util.List;

public class ProductoDAO {

    public int RegistrarProducto(Producto productoAgregar){
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            Long count = em.createQuery(
                "SELECT COUNT(p) FROM Producto p WHERE p.codigo = :numCod", Long.class)
            .setParameter("numCod", productoAgregar.getCodigo())
            .getSingleResult();
            
            if(count > 0){
                return 0;
            }
            
            em.getTransaction().begin();
            em.persist(productoAgregar);
            em.getTransaction().commit();
            return 1;
        }catch(Exception ex){
            if(em.getTransaction().isActive()){
               em.getTransaction().rollback(); 
            }
            return 2;
            
        }finally{
            em.close();
        }
    }
    
    public boolean ActualizarProducto(int id, Producto productoActualizar) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            Producto existente = em.find(Producto.class, id);
            if (existente == null) return false;

            em.getTransaction().begin();
            existente.setNombre(productoActualizar.getNombre());
            existente.setCodigo(productoActualizar.getCodigo());
            existente.setPrecio(productoActualizar.getPrecio());
            
            
            em.getTransaction().commit();
            return true;

        } catch (Exception ex) {
           
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return false;
        } finally {
            em.close();
        }
    }
    
    
    public boolean EliminarProducto(int numId) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            Producto producto = em.find(Producto.class, numId);
            if (producto == null) return false;

            em.getTransaction().begin();
            em.remove(producto);
            em.getTransaction().commit();
            return true;

        } catch (Exception ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return false;
        } finally {
            em.close();
        }
    }
    
    public List<Producto> ListarProductosRegistrados() {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            return em.createQuery("SELECT p FROM Producto p", Producto.class).getResultList();
        } finally {
            em.close();
        }
    }
    
    public Producto BuscarProductoPorCodigo(String codigo){
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            return em.createQuery("SELECT p FROM Producto p WHERE p.codigo = :cod", Producto.class)
                    .setParameter("cod", codigo)
                    .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        } finally {
            em.close();
        }
    }
}
