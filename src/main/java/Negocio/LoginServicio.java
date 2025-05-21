package Negocio;

import Datos.AutentificacionDAO;
import Model.Empleado;


public class LoginServicio {

 private final AutentificacionDAO autenticacionDAO;
    
    
    public LoginServicio(){
        this.autenticacionDAO = new AutentificacionDAO();
    }
    
    
    public boolean LoginUsuarioClave(String usuario, String clave){
        Empleado empleadoLogeado = this.autenticacionDAO.IniciarSesionUsuarioClave(usuario, clave);
        return empleadoLogeado != null;
    }
}
