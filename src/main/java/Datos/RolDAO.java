package Datos;

import Model.Rol;
import Util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class RolDAO {

    public int registrarRol(Rol rolAgregar) {
        EntityManager entityManager = null;
        try {
            entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
            entityManager.getTransaction().begin();
            entityManager.persist(rolAgregar);
            entityManager.getTransaction().commit();
            return 0;
        } catch (Exception ex) {
            if (entityManager != null && entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            System.err.println("Error al registrar rol: " + ex.getMessage());
            ex.printStackTrace();
            return 1;
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }

    public boolean eliminarRol(String nombre) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        boolean eliminado = false;
        try {
            em.getTransaction().begin();
            Rol rolEliminar = em.find(Rol.class, nombre);
            if (rolEliminar != null) {
                em.remove(rolEliminar);
                em.getTransaction().commit();
                eliminado = true;
                System.out.println("Rol eliminado con éxito.");
            } else {
                em.getTransaction().rollback();
                System.out.println("No se encontró el rol con nombre: " + nombre);
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Error al eliminar rol: " + e.getMessage());
        } finally {
            em.close();
        }
        return eliminado;
    }

    public List<Rol> listarTodos() {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        List<Rol> roles = null;
        try {
            TypedQuery<Rol> query = em.createQuery("SELECT r FROM Rol r", Rol.class);
            roles = query.getResultList();
        } catch (Exception e) {
            System.err.println("Error al listar roles: " + e.getMessage());
        } finally {
            em.close();
        }
        return roles;
    }

    public List<Rol> listarTodosConEmpleados() {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        List<Rol> roles = null;
        try {
            TypedQuery<Rol> query = em.createQuery(
                    "SELECT DISTINCT r FROM Rol r LEFT JOIN FETCH r.empleados", Rol.class);
            roles = query.getResultList();
        } catch (Exception e) {
            System.err.println("Error al listar roles con empleados: " + e.getMessage());
        } finally {
            em.close();
        }
        return roles;
    }
}
