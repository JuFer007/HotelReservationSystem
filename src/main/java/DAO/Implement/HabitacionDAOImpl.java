package DAO.Implement;
import Conection.DatabaseConnection;
import DAO.Interfaces.IHabitacionDAO;
import Model.Habitacion;
import Model.TipoHabitacion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class HabitacionDAOImpl implements IHabitacionDAO {
    private final DatabaseConnection db = DatabaseConnection.getInstance();

    @Override
    public boolean create(Habitacion h) {
        String sql = "INSERT INTO Habitacion (idTipoHabitacion, numeroHabitacion, estado) VALUES (?,?,?)";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setInt(1, h.getIdTipoHabitacion());
            ps.setString(2, h.getNumeroHabitacion());
            ps.setString(3, h.getEstado());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error create Habitacion: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Habitacion read(int id) {
        String sql = "SELECT h.*, t.descripcion, t.precio FROM Habitacion h " +
                "INNER JOIN TipoHabitacion t ON h.idTipoHabitacion = t.idTipoHabitacion " +
                "WHERE h.idHabitacion = ?";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapearConTipo(rs);
        } catch (SQLException e) {
            System.err.println("Error read Habitacion: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean update(Habitacion h) {
        String sql = "UPDATE Habitacion SET idTipoHabitacion=?, numeroHabitacion=?, estado=? WHERE idHabitacion=?";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setInt(1, h.getIdTipoHabitacion());
            ps.setString(2, h.getNumeroHabitacion());
            ps.setString(3, h.getEstado());
            ps.setInt(4, h.getIdHabitacion());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error update Habitacion: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM Habitacion WHERE idHabitacion = ?";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error delete Habitacion: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Habitacion> findAll() {
        List<Habitacion> lista = new ArrayList<>();
        String sql = "SELECT h.*, t.descripcion, t.precio FROM Habitacion h " +
                "INNER JOIN TipoHabitacion t ON h.idTipoHabitacion = t.idTipoHabitacion";
        try (Statement st = db.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapearConTipo(rs));
        } catch (SQLException e) {
            System.err.println("Error findAll Habitacion: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public Habitacion findByNumero(String numero) {
        String sql = "SELECT h.*, t.descripcion, t.precio FROM Habitacion h " +
                "INNER JOIN TipoHabitacion t ON h.idTipoHabitacion = t.idTipoHabitacion " +
                "WHERE h.numeroHabitacion = ?";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setString(1, numero);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapearConTipo(rs);
        } catch (SQLException e) {
            System.err.println("Error findByNumero: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Habitacion> findByEstado(String estado) {
        List<Habitacion> lista = new ArrayList<>();
        String sql = "SELECT h.*, t.descripcion, t.precio FROM Habitacion h " +
                "INNER JOIN TipoHabitacion t ON h.idTipoHabitacion = t.idTipoHabitacion " +
                "WHERE h.estado = ?";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setString(1, estado);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapearConTipo(rs));
        } catch (SQLException e) {
            System.err.println("Error findByEstado: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<Habitacion> findByTipo(int idTipo) {
        List<Habitacion> lista = new ArrayList<>();
        String sql = "SELECT h.*, t.descripcion, t.precio FROM Habitacion h " +
                "INNER JOIN TipoHabitacion t ON h.idTipoHabitacion = t.idTipoHabitacion " +
                "WHERE h.idTipoHabitacion = ?";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setInt(1, idTipo);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapearConTipo(rs));
        } catch (SQLException e) {
            System.err.println("Error findByTipo: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<Habitacion> findDisponibles() {
        return findByEstado("Disponible");
    }

    @Override
    public boolean updateEstado(int id, String estado) {
        String sql = "UPDATE Habitacion SET estado = ? WHERE idHabitacion = ?";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setString(1, estado);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updateEstado: " + e.getMessage());
            return false;
        }
    }

    private Habitacion mapearConTipo(ResultSet rs) throws SQLException {
        Habitacion h = new Habitacion(
                rs.getInt("idHabitacion"),
                rs.getInt("idTipoHabitacion"),
                rs.getString("numeroHabitacion"),
                rs.getString("estado")
        );
        TipoHabitacion tipo = new TipoHabitacion(
                rs.getInt("idTipoHabitacion"),
                rs.getString("descripcion"),
                rs.getDouble("precio")
        );
        h.setTipoHabitacion(tipo);
        return h;
    }
}
