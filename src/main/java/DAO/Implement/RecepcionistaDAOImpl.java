package DAO.Implement;

import Conection.DatabaseConnection;
import DAO.Interfaces.IRecepcionistaDAO;
import Model.Recepcionista;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RecepcionistaDAOImpl implements IRecepcionistaDAO {
    private final DatabaseConnection db = DatabaseConnection.getInstance();

    @Override
    public boolean create(Recepcionista r) {
        String sql = "INSERT INTO Recepcionista (idEmpleado, turnoTrabajo) VALUES (?,?)";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setInt(1, r.getIdEmpleado());
            ps.setString(2, r.getTurnoTrabajo());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error create Recepcionista: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Recepcionista read(int id) {
        String sql = "SELECT r.*, e.nombre, e.apellidoPaterno, e.apellidoMaterno, e.dni, e.telefono " +
                "FROM Recepcionista r " +
                "INNER JOIN Empleado e ON r.idEmpleado = e.idEmpleado " +
                "WHERE r.idRecepcionista = ?";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            System.err.println("Error read Recepcionista: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean update(Recepcionista r) {
        String sql = "UPDATE Recepcionista SET idEmpleado=?, turnoTrabajo=? WHERE idRecepcionista=?";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setInt(1, r.getIdEmpleado());
            ps.setString(2, r.getTurnoTrabajo());
            ps.setInt(3, r.getIdRecepcionista());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error update Recepcionista: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM Recepcionista WHERE idRecepcionista = ?";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error delete Recepcionista: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Recepcionista> findAll() {
        List<Recepcionista> lista = new ArrayList<>();
        String sql = "SELECT r.*, e.nombre, e.apellidoPaterno, e.apellidoMaterno, e.dni, e.telefono " +
                "FROM Recepcionista r " +
                "INNER JOIN Empleado e ON r.idEmpleado = e.idEmpleado";
        try (Statement st = db.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("Error findAll Recepcionista: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public Recepcionista findByIdEmpleado(int idEmpleado) {
        String sql = "SELECT r.*, e.nombre, e.apellidoPaterno, e.apellidoMaterno, e.dni, e.telefono " +
                "FROM Recepcionista r " +
                "INNER JOIN Empleado e ON r.idEmpleado = e.idEmpleado " +
                "WHERE r.idEmpleado = ?";
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
    public List<Recepcionista> findByTurno(String turno) {
        List<Recepcionista> lista = new ArrayList<>();
        String sql = "SELECT r.*, e.nombre, e.apellidoPaterno, e.apellidoMaterno, e.dni, e.telefono " +
                "FROM Recepcionista r " +
                "INNER JOIN Empleado e ON r.idEmpleado = e.idEmpleado " +
                "WHERE r.turnoTrabajo = ?";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setString(1, turno);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("Error findByTurno: " + e.getMessage());
        }
        return lista;
    }

    private Recepcionista mapear(ResultSet rs) throws SQLException {
        Recepcionista r = new Recepcionista();
        r.setIdRecepcionista(rs.getInt("idRecepcionista"));
        r.setIdEmpleado(rs.getInt("idEmpleado"));
        r.setTurnoTrabajo(rs.getString("turnoTrabajo"));
        // Datos del empleado
        r.setNombre(rs.getString("nombre"));
        r.setApellidoPaterno(rs.getString("apellidoPaterno"));
        r.setApellidoMaterno(rs.getString("apellidoMaterno"));
        r.setDni(rs.getString("dni"));
        r.setTelefono(rs.getString("telefono"));
        return r;
    }
}
