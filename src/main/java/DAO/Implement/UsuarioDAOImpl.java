package DAO.Implement;
import Conection.DatabaseConnection;
import DAO.Interfaces.IUsuarioDAO;
import Model.Empleado;
import Model.Usuario;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAOImpl implements IUsuarioDAO {
    private final DatabaseConnection db = DatabaseConnection.getInstance();

    @Override
    public boolean create(Usuario u) {
        String sql = "INSERT INTO Usuario (idEmpleado, nombreUsuario, contraseña) VALUES (?,?,?)";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setInt(1, u.getIdEmpleado());
            ps.setString(2, u.getNombreUsuario());
            ps.setString(3, u.getContrasena());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error create Usuario: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Usuario read(int id) {
        String sql = "SELECT u.*, e.* FROM Usuario u " +
                "INNER JOIN Empleado e ON u.idEmpleado = e.idEmpleado " +
                "WHERE u.idUsuario = ?";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapearConEmpleado(rs);
        } catch (SQLException e) {
            System.err.println("Error read Usuario: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean update(Usuario u) {
        String sql = "UPDATE Usuario SET nombreUsuario=?, contraseña=? WHERE idUsuario=?";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setString(1, u.getNombreUsuario());
            ps.setString(2, u.getContrasena());
            ps.setInt(3, u.getIdUsuario());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error update Usuario: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM Usuario WHERE idUsuario = ?";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error delete Usuario: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Usuario> findAll() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT u.*, e.* FROM Usuario u " +
                "INNER JOIN Empleado e ON u.idEmpleado = e.idEmpleado";
        try (Statement st = db.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapearConEmpleado(rs));
        } catch (SQLException e) {
            System.err.println("Error findAll Usuario: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public Usuario findByNombreUsuario(String nombreUsuario) {
        String sql = "SELECT u.*, e.* FROM Usuario u " +
                "INNER JOIN Empleado e ON u.idEmpleado = e.idEmpleado " +
                "WHERE u.nombreUsuario = ?";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setString(1, nombreUsuario);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapearConEmpleado(rs);
        } catch (SQLException e) {
            System.err.println("Error findByNombreUsuario: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Usuario validarCredenciales(String nombreUsuario, String contrasena) {
        String sql = "SELECT u.*, e.* FROM Usuario u " +
                "INNER JOIN Empleado e ON u.idEmpleado = e.idEmpleado " +
                "WHERE u.nombreUsuario = ? AND u.contraseña = ?";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setString(1, nombreUsuario);
            ps.setString(2, contrasena);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapearConEmpleado(rs);
        } catch (SQLException e) {
            System.err.println("Error validarCredenciales: " + e.getMessage());
        }
        return null;
    }

    private Usuario mapearConEmpleado(ResultSet rs) throws SQLException {
        Usuario u = new Usuario(
                rs.getInt("idUsuario"),
                rs.getInt("idEmpleado"),
                rs.getString("nombreUsuario"),
                rs.getString("contraseña")
        );
        Empleado emp = new Empleado(
                rs.getInt("idEmpleado"),
                rs.getString("nombre"),
                rs.getString("apellidoPaterno"),
                rs.getString("apellidoMaterno"),
                rs.getString("dni"),
                rs.getString("telefono")
        );
        u.setEmpleado(emp);
        return u;
    }
}
