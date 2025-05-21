
package Negocio;

import Datos.EmpleadoDAO;
import Datos.PersonaDAO;
import Model.Cliente;
import Model.Empleado;
import Model.Persona;
import Datos.ClienteDAO;
import java.util.List;



public class PersonaServicio {
    
    private final PersonaDAO personaDAO;
    private final ClienteDAO clienteDAO;
    private final EmpleadoDAO empleadoDAO;
    
    
    public PersonaServicio(){
        this.personaDAO = new PersonaDAO();
        this.clienteDAO = new ClienteDAO();
        this.empleadoDAO = new EmpleadoDAO();
    }
    
    
    
    public int AgregarPersona(Persona nuevaPersona){
        nuevaPersona.CalcularEdad();
        if(nuevaPersona.getEdad() >= 18){
            String nombre = nuevaPersona.getNombre().toUpperCase(); 
            nuevaPersona.setNombre(nombre);

            String apellido = nuevaPersona.getApellido().toUpperCase(); 
            nuevaPersona.setApellido(apellido);
            
            if(nuevaPersona instanceof Cliente cliente){
                return this.clienteDAO.RegistrarClienteDB(cliente);
                
            } else if(nuevaPersona instanceof Empleado empleado){
                return this.empleadoDAO.RegistrarEmpleadoDB(empleado);
            }else{
                return 2;
            }
        } else {
            return 3;
        }
    }
    
    
    
    public List<Persona> ListarPersonas(){
        return personaDAO.ListarPersonasRegistradas();
    }
    
    
    public boolean EliminarPersonaPorId(int numId){
        return personaDAO.EliminarPersona(numId);
    }
    
    
    public boolean ActualizarPersona(int id, Persona persona) {
        return personaDAO.ActualizarPersona(id, persona);
    }
}
