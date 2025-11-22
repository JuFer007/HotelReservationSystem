package DAO.Implement;
import Conection.DatabaseConnection;
import DAO.Interfaces.IAdministradorDAO;
import Model.Administrador;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AdministradorDAOImpl implements IAdministradorDAO {
    private final DatabaseConnection db = DatabaseConnection.getInstance();

    @Override
    public boolean create(Administrador a) {
        String sql = "INSERT INTO Administrador (idEmpleado, correoElectronico) VALUES (?,?)";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setInt(1, a.getIdEmpleado());
            ps.setString(2, a.getCorreoElectronico());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error create Administrador: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Administrador read(int id) {
        String sql = "SELECT a.*, e.nombre, e.apellidoPaterno, e.apellidoMaterno, e.dni, e.telefono " +
                "FROM Administrador a " +
                "INNER JOIN Empleado e ON a.idEmpleado = e.idEmpleado " +
                "WHERE a.idAdministrador = ?";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            System.err.println("Error read Administrador: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean update(Administrador a) {
        String sql = "UPDATE Administrador SET idEmpleado=?, correoElectronico=? WHERE idAdministrador=?";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setInt(1, a.getIdEmpleado());
            ps.setString(2, a.getCorreoElectronico());
            ps.setInt(3, a.getIdAdministrador());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error update Administrador: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM Administrador WHERE idAdministrador = ?";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error delete Administrador: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Administrador> findAll() {
        List<Administrador> lista = new ArrayList<>();
        String sql = "SELECT a.*, e.nombre, e.apellidoPaterno, e.apellidoMaterno, e.dni, e.telefono " +
                "FROM Administrador a " +
                "INNER JOIN Empleado e ON a.idEmpleado = e.idEmpleado";
        try (Statement st = db.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("Error findAll Administrador: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public Administrador findByIdEmpleado(int idEmpleado) {
        String sql = "SELECT a.*, e.nombre, e.apellidoPaterno, e.apellidoMaterno, e.dni, e.telefono " +
                "FROM Administrador a " +
                "INNER JOIN Empleado e ON a.idEmpleado = e.idEmpleado " +
                "WHERE a.idEmpleado = ?";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setInt(1, idEmpleado);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            System.err.println("Error findByIdEmpleado: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Administrador findByCorreo(String correo) {
        String sql = "SELECT a.*, e.nombre, e.apellidoPaterno, e.apellidoMaterno, e.dni, e.telefono " +
                "FROM Administrador a " +
                "INNER JOIN Empleado e ON a.idEmpleado = e.idEmpleado " +
                "WHERE a.correoElectronico = ?";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setString(1, correo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            System.err.println("Error findByCorreo: " + e.getMessage());
        }
        return null;
    }

    private Administrador mapear(ResultSet rs) throws SQLException {
        Administrador a = new Administrador();
        a.setIdAdministrador(rs.getInt("idAdministrador"));
        a.setIdEmpleado(rs.getInt("idEmpleado"));
        a.setCorreoElectronico(rs.getString("correoElectronico"));
        a.setNombre(rs.getString("nombre"));
        a.setApellidoPaterno(rs.getString("apellidoPaterno"));
        a.setApellidoMaterno(rs.getString("apellidoMaterno"));
        a.setDni(rs.getString("dni"));
        a.setTelefono(rs.getString("telefono"));
        return a;
    }
}
