package Datos;

import Model.Empleado;
import Model.Persona;
import Util.JPAUtil;

import jakarta.persistence.EntityManager;

import jakarta.persistence.NoResultException;

import java.util.ArrayList;
import java.util.List;

public class EmpleadoDAO {

    private EntityManager entityManager;

    public EmpleadoDAO() {
        this.entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
    }

    public int AgregarEmpleado(Empleado empleadoAgregar) {
        int result = 0;
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            Empleado empleadoExiste = em.createQuery(
                    "SELECT e FROM Empleado e WHERE e.cedula = :cedula", Empleado.class)
                    .setParameter("cedula", empleadoAgregar.getCedula())
                    .getSingleResult();
            if (empleadoExiste != null) {
                System.out.println("YA EXISTE EL EMPLEADO CON LA CÉDULA: " + empleadoAgregar.getCedula());
                return result;
            }
        } catch (NoResultException ex) {
            em.getTransaction().begin();
            em.persist(empleadoAgregar);
            em.getTransaction().commit();
            result = 1;
        } catch (Exception ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Error al agregar empleado: " + ex.getMessage());
        } finally {
            em.close();
        }
        return result;
    }

    public boolean eliminarEmpleado(String cedula) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        boolean eliminado = false;
        try {
            em.getTransaction().begin();
            Empleado empleadoEliminar = em.find(Empleado.class, cedula);
            if (empleadoEliminar != null) {
                em.remove(empleadoEliminar);
                em.getTransaction().commit();
                eliminado = true;
                System.out.println("Empleado eliminado con éxito.");
            } else {
                em.getTransaction().rollback();
                System.out.println("No se encontró el empleado con cédula: " + cedula);
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Error al eliminar empleado: " + e.getMessage());
        } finally {
            em.close();
        }
        return eliminado;
    }

    public Persona buscarPorCedula(String cedula) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        Persona persona = null;

        try {
            persona = em.createQuery(
                    "SELECT p FROM Persona p WHERE p.cedula = :cedula", Persona.class)
                    .setParameter("cedula", cedula)
                    .getSingleResult();
        } catch (NoResultException ex) {
            System.out.println("No se encontró una persona con cédula: " + cedula);
        } catch (Exception e) {
            System.err.println("Error al buscar persona: " + e.getMessage());
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }

        return persona;
    }
    public List<Empleado> listarEmpleados() {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        List<Empleado> lista = new ArrayList<>();
        try {
            lista = em.createQuery("SELECT e FROM Empleado e", Empleado.class).getResultList();
        } catch (Exception ex) {
            System.out.println("Error al obtener lista de empleados: " + ex.getMessage());
        } finally {
            em.close();
        }
        return lista;
    }

    public boolean actualizarEmpleado(Empleado empleado) {
        if (empleado == null || empleado.getCedula() == null || empleado.getCedula().trim().isEmpty()) {
            throw new IllegalArgumentException("El empleado a actualizar no puede ser nulo y debe tener una cédula válida.");
        }

        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        boolean actualizado = false;

        try {
            em.getTransaction().begin();

            Empleado empleadoExistente = em.find(Empleado.class, empleado.getCedula());

            if (empleadoExistente != null) {
                empleadoExistente.setNombre(empleado.getNombre());
                empleadoExistente.setApellido(empleado.getApellido());
                empleadoExistente.setRoles(empleado.getRoles());
                empleadoExistente.setDireccion(empleado.getDireccion());
                empleadoExistente.setSalario(empleado.getSalario());
                empleadoExistente.setTurno(empleado.getTurno());
                empleadoExistente.setFechaContratacion(empleado.getFechaContratacion());

                em.getTransaction().commit();
                actualizado = true;
                System.out.println("Empleado actualizado con éxito.");
            } else {
                em.getTransaction().rollback();
                System.out.println("No se encontró el empleado con cédula: " + empleado.getCedula());
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Error al actualizar empleado: " + e.getMessage());
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }

        return actualizado;
    }

}
