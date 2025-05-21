package Datos;

import Model.Empleado;
import Util.JPAUtil;
import jakarta.persistence.EntityManager;

public class EmpleadoDAO {

    public int RegistrarEmpleadoDB(Empleado nuevoEmpleado) {
        // Inicia la sesion de trabajo con la base de datos
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            Long count = em.createQuery(
                    "SELECT COUNT(p) FROM Persona p WHERE p.numIdentificacion = :numId", Long.class)
                    .setParameter("numId", nuevoEmpleado.getNumIdentificacion())
                    .getSingleResult();

            if (count > 0) {
                return 0;
            }

            em.getTransaction().begin();
            em.persist(nuevoEmpleado);
            em.getTransaction().commit();
            return 1;
        } catch (Exception ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return 2;
        } finally {
            em.close();
        }
    }

    public boolean ActualizarEmpleadoDB(int id, Empleado empleadoActualizar) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            Empleado existente = em.find(Empleado.class, id);
            if (existente == null) {
                return false;
            }

            em.getTransaction().begin();

            existente.setNombre(empleadoActualizar.getNombre());
            existente.setApellido(empleadoActualizar.getApellido());
            existente.setNumIdentificacion(empleadoActualizar.getNumIdentificacion());
            existente.setCorreo(empleadoActualizar.getCorreo());
            existente.setFechaNacimiento(empleadoActualizar.getFechaNacimiento());
            existente.setEdad(empleadoActualizar.getEdad());
            existente.setRol(empleadoActualizar.getRol());
            existente.setActivo(empleadoActualizar.isActivo());

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
}
