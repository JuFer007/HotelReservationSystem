package DAO.Implement;
import Conection.DatabaseConnection;
import DAO.Interfaces.ITipoHabitacionDAO;
import Model.TipoHabitacion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TipoHabitacionDAOImpl implements ITipoHabitacionDAO {
    private final DatabaseConnection db = DatabaseConnection.getInstance();

    @Override
    public boolean create(TipoHabitacion t) {
        String sql = "INSERT INTO TipoHabitacion (descripcion, precio) VALUES (?,?)";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setString(1, t.getDescripcion());
            ps.setDouble(2, t.getPrecio());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error create TipoHabitacion: " + e.getMessage());
            return false;
        }
    }

    @Override
    public TipoHabitacion read(int id) {
        String sql = "SELECT * FROM TipoHabitacion WHERE idTipoHabitacion = ?";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            System.err.println("Error read TipoHabitacion: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean update(TipoHabitacion t) {
        String sql = "UPDATE TipoHabitacion SET descripcion=?, precio=? WHERE idTipoHabitacion=?";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setString(1, t.getDescripcion());
            ps.setDouble(2, t.getPrecio());
            ps.setInt(3, t.getIdTipoHabitacion());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error update TipoHabitacion: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM TipoHabitacion WHERE idTipoHabitacion = ?";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error delete TipoHabitacion: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<TipoHabitacion> findAll() {
        List<TipoHabitacion> lista = new ArrayList<>();
        String sql = "SELECT * FROM TipoHabitacion";
        try (Statement st = db.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("Error findAll TipoHabitacion: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public TipoHabitacion findByDescripcion(String descripcion) {
        String sql = "SELECT * FROM TipoHabitacion WHERE descripcion = ?";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setString(1, descripcion);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            System.err.println("Error findByDescripcion: " + e.getMessage());
        }
        return null;
    }

    private TipoHabitacion mapear(ResultSet rs) throws SQLException {
        return new TipoHabitacion(
                rs.getInt("idTipoHabitacion"),
                rs.getString("descripcion"),
                rs.getDouble("precio")
        );
    }
}
