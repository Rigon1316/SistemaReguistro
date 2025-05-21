package Negocio;

import Datos.RolDAO;
import Model.Rol;
import java.util.ArrayList;
import java.util.List;

public class RolServicio {

    private final RolDAO rolDAO;

    public RolServicio() {
        this.rolDAO = new RolDAO();
    }

    public boolean agregarRol(String nombreRol) {
        if (nombreRol == null || nombreRol.trim().isEmpty()) {
            return false;
        }

        List<String> rolesExistentes = listarRoles();
        if (rolesExistentes.contains(nombreRol)) {
            return false;
        }

        Rol nuevoRol = new Rol();
        nuevoRol.setNombre(nombreRol);

        int resultado = rolDAO.registrarRol(nuevoRol);
        return resultado == 0;
    }

    public boolean eliminarRol(String nombreRol) {
        if (nombreRol == null || nombreRol.trim().isEmpty()) {
            return false;
        }

        return rolDAO.eliminarRol(nombreRol);
    }

    public List<String> listarRoles() {
        List<Rol> listaRoles = rolDAO.listarTodos();
        List<String> nombresRoles = new ArrayList<>();

        if (listaRoles != null) {
            for (Rol rol : listaRoles) {
                nombresRoles.add(rol.getNombre());
            }
        }

        return nombresRoles;
    }

    public List<Rol> listarObjetosRol() {
        return rolDAO.listarTodos();
    }

    public boolean existeRol(String nombreRol) {
        if (nombreRol == null || nombreRol.trim().isEmpty()) {
            return false;
        }

        List<String> roles = listarRoles();
        return roles.contains(nombreRol);
    }
}
