package Datos;

import Model.Factura;
import Util.JPAUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

public class FacturaDAO {

    public int RegistrarFactura(Factura facturaAgregar) {
        
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            em.getTransaction().begin();

            em.persist(facturaAgregar);

            em.getTransaction().commit();
            return 0;
        } catch (Exception ex) {
            em.getTransaction().rollback();
            System.err.println("Error de sesion de trabajo: " + ex.getMessage());
            return 1;
        } finally {
            em.close();
        }
    }

    
    public Factura ObtenerFacturaCompletaPorId(int idFactura) {
        
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            
            return em.createQuery("""
                    SELECT f FROM Factura f
                    JOIN FETCH f.persona
                    LEFT JOIN FETCH f.detalles d
                    LEFT JOIN FETCH d.producto
                    WHERE f.id = :idFactura
                    """, Factura.class)
                    .setParameter("idFactura", idFactura)
                    .getSingleResult();
//            return em.createQuery("SELECT p FROM Factura p WHERE p.id = :idS", Factura.class)
//                    .setParameter("idS", idFactura)
//                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
}
