package Datos;

import Model.Empleado;
import Util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

public class AutentificacionDAO {
    
    // Se emplea este metodo para poder iniciar sesion con usuario y clave
    public Empleado IniciarSesionUsuarioClave(String usuario, String clave){
        // Se inicia la sesion de trabajo
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try{
            return em.createQuery(
               "SELECT e FROM Empleado e WHERE e.numIdentificacion = :user AND e.clave = :contrasena", Empleado.class)
                .setParameter("user", usuario)
                .setParameter("contrasena", clave)
                .getSingleResult();
        }catch(NoResultException ex){
            return null;
        }finally{
            em.close();
        }
    }
}
