/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Datos;

import Model.Cliente;
import Util.JPAUtil;
import jakarta.persistence.EntityManager;

/**
 *
 * @author XRLab
 */
public class ClienteDAO {

    // [0] ya existe el cliente  [1] registro de cliente exitoso
    // [2] Error interno
    // Se emplea este metodo para verificar si existe el cliente por su cedula
    // y si no existe se procede a registrarla
    public int RegistrarClienteDB(Cliente nuevoCliente) {
        // Inicia la sesion de trabajo con la base de datos
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            Long count = em.createQuery(
                    "SELECT COUNT(p) FROM Persona p WHERE p.numIdentificacion = :numId", Long.class)
                    .setParameter("numId", nuevoCliente.getNumIdentificacion())
                    .getSingleResult();

            // Existe la persona, porque el contador dio un resultado
            if (count > 0) {
                return 0;
            }

            // Se inicia la transicion
            em.getTransaction().begin();
            // Se inserta la persona
            em.persist(nuevoCliente);
            // Confirmar y guardar los cambios
            em.getTransaction().commit();
            return 1;
        } catch (Exception ex) {
            // Revertir todo, no guardar nada
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return 2;
        } finally {
            em.close();
        }
    }

    public boolean ActualizarClienteDB(int id, Cliente personaActualizar) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {

            Cliente existente = em.find(Cliente.class, id);
            if (existente == null) {
                return false;
            }

            em.getTransaction().begin();

            existente.setNombre(personaActualizar.getNombre());
            existente.setApellido(personaActualizar.getApellido());
            existente.setNumIdentificacion(personaActualizar.getNumIdentificacion());
            existente.setCorreo(personaActualizar.getCorreo());
            existente.setFechaNacimiento(personaActualizar.getFechaNacimiento());
            existente.setEdad(personaActualizar.getEdad());
            existente.setDireccion(personaActualizar.getDireccion());

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
