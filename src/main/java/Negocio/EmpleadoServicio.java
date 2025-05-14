package Servicios;

import Datos.EmpleadoDAO;
import Model.Empleado;
import Model.Persona;
import Util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.util.List;

public class EmpleadoServicio {

    private final EmpleadoDAO empleadoDAO;

    public EmpleadoServicio() {
        this.empleadoDAO = new EmpleadoDAO();
    }

    public int agregarEmpleado(Empleado empleado) {
        return empleadoDAO.AgregarEmpleado(empleado);
    }

    public boolean eliminarEmpleado(String cedula) {
        return empleadoDAO.eliminarEmpleado(cedula);
    }

    public boolean actualizarEmpleado(Empleado empleado) {
        return empleadoDAO.actualizarEmpleado(empleado);
    }

    public List<Empleado> listarEmpleados() {
        return empleadoDAO.listarEmpleados();
    }

    public Empleado buscarPorCedula(String cedula) {
        List<Empleado> empleados = empleadoDAO.listarEmpleados();
        for (Empleado emp : empleados) {
            if (emp.getCedula().equals(cedula)) {
                return emp;
            }
        }
        return null;
    }

    
    public Persona buscarPersonaPorCedula(String cedula) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        Persona persona = null;

        try {
            // Consulta específica para buscar personas que no son empleados
            persona = em.createQuery(
                    "SELECT p FROM Persona p WHERE p.cedula = :cedula AND NOT EXISTS "
                    + "(SELECT e FROM Empleado e WHERE e.cedula = p.cedula)", Persona.class)
                    .setParameter("cedula", cedula)
                    .getSingleResult();
        } catch (NoResultException ex) {
            // No se encontró ninguna persona con esa cédula
            persona = null;
        } catch (Exception ex) {
            System.err.println("Error al buscar persona por cédula: " + ex.getMessage());
        } finally {
            em.close();
        }

        return persona;
    }
}
