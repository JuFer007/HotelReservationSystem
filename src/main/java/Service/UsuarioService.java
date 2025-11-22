package Service;
import DAO.Implement.UsuarioDAOImpl;
import DAO.Interfaces.IUsuarioDAO;
import Model.Usuario;
import java.util.List;

public class UsuarioService {
    private final IUsuarioDAO usuarioDAO;

    public UsuarioService() {
        this.usuarioDAO = new UsuarioDAOImpl();
    }

    public Usuario validarCredenciales(String nombreUsuario, String contrasena) {
        if (nombreUsuario == null || nombreUsuario.isEmpty()) return null;
        if (contrasena == null || contrasena.isEmpty()) return null;
        return usuarioDAO.validarCredenciales(nombreUsuario, contrasena);
    }

    public boolean registrar(Usuario usuario) {
        if (usuarioDAO.findByNombreUsuario(usuario.getNombreUsuario()) != null) {
            return false;
        }
        return usuarioDAO.create(usuario);
    }

    public List<Usuario> obtenerTodos() {
        return usuarioDAO.findAll();
    }

    public boolean actualizar(Usuario usuario) {
        return usuarioDAO.update(usuario);
    }

    public boolean eliminar(int id) {
        return usuarioDAO.delete(id);
    }
}
