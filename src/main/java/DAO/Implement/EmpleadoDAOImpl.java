package DAO.Implement;
import Conection.DatabaseConnection;
import DAO.Interfaces.IEmpleadoDAO;
import Model.Empleado;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoDAOImpl implements IEmpleadoDAO {
    private final DatabaseConnection db = DatabaseConnection.getInstance();

    @Override
    public boolean create(Empleado e) {
        String sql = "INSERT INTO Empleado (nombre, apellidoPaterno, apellidoMaterno, dni, telefono) VALUES (?,?,?,?,?)";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setString(1, e.getNombre());
            ps.setString(2, e.getApellidoPaterno());
            ps.setString(3, e.getApellidoMaterno());
            ps.setString(4, e.getDni());
            ps.setString(5, e.getTelefono());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.err.println("Error create Empleado: " + ex.getMessage());
            return false;
        }
    }

    @Override
    public Empleado read(int id) {
        String sql = "SELECT * FROM Empleado WHERE idEmpleado = ?";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            System.err.println("Error read Empleado: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean update(Empleado e) {
        String sql = "UPDATE Empleado SET nombre=?, apellidoPaterno=?, apellidoMaterno=?, dni=?, telefono=? WHERE idEmpleado=?";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setString(1, e.getNombre());
            ps.setString(2, e.getApellidoPaterno());
            ps.setString(3, e.getApellidoMaterno());
            ps.setString(4, e.getDni());
            ps.setString(5, e.getTelefono());
            ps.setInt(6, e.getIdEmpleado());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.err.println("Error update Empleado: " + ex.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM Empleado WHERE idEmpleado = ?";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error delete Empleado: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Empleado> findAll() {
        List<Empleado> lista = new ArrayList<>();
        String sql = "SELECT * FROM Empleado";
        try (Statement st = db.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("Error findAll Empleado: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public Empleado findByDni(String dni) {
        String sql = "SELECT * FROM Empleado WHERE dni = ?";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setString(1, dni);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            System.err.println("Error findByDni: " + e.getMessage());
        }
        return null;
    }

    private Empleado mapear(ResultSet rs) throws SQLException {
        return new Empleado(
                rs.getInt("idEmpleado"),
                rs.getString("nombre"),
                rs.getString("apellidoPaterno"),
                rs.getString("apellidoMaterno"),
                rs.getString("dni"),
                rs.getString("telefono")
        );
    }
}
