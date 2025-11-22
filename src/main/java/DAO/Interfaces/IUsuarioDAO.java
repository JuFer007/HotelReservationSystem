package DAO.Interfaces;
import Model.Usuario;

public interface IUsuarioDAO extends IGenericDAO<Usuario> {
    Usuario findByNombreUsuario(String nombreUsuario);
    Usuario validarCredenciales(String nombreUsuario, String contrasena);
}
