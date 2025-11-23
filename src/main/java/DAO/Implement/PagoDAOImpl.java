package DAO.Implement;
import Conection.DatabaseConnection;
import DAO.Interfaces.IPagoDAO;
import Model.Pago;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PagoDAOImpl implements IPagoDAO {
    private final DatabaseConnection db = DatabaseConnection.getInstance();

    @Override
    public boolean create(Pago p) {
        String sql = "INSERT INTO Pago (idReserva, monto, fechaPago, metodoPago) VALUES (?,?,?,?)";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setInt(1, p.getIdReserva());
            ps.setDouble(2, p.getMonto());
            ps.setDate(3, Date.valueOf(p.getFechaPago()));
            ps.setString(4, p.getMetodoPago());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error create Pago: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Pago read(int id) {
        String sql = "SELECT * FROM Pago WHERE idPago = ?";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            System.err.println("Error read Pago: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean update(Pago p) {
        String sql = "UPDATE Pago SET idReserva=?, monto=?, fechaPago=?, metodoPago=? WHERE idPago=?";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setInt(1, p.getIdReserva());
            ps.setDouble(2, p.getMonto());
            ps.setDate(3, Date.valueOf(p.getFechaPago()));
            ps.setString(4, p.getMetodoPago());
            ps.setInt(5, p.getIdPago());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error update Pago: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM Pago WHERE idPago = ?";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error delete Pago: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Pago> findAll() {
        List<Pago> lista = new ArrayList<>();
        String sql = "SELECT * FROM Pago ORDER BY fechaPago DESC";
        try (Statement st = db.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("Error findAll Pago: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public Pago findByReserva(int idReserva) {
        String sql = "SELECT * FROM Pago WHERE idReserva = ?";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setInt(1, idReserva);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            System.err.println("Error findByReserva: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Pago> findByFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        List<Pago> lista = new ArrayList<>();
        String sql = "SELECT * FROM Pago WHERE fechaPago BETWEEN ? AND ? ORDER BY fechaPago DESC";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(fechaInicio));
            ps.setDate(2, Date.valueOf(fechaFin));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("Error findByFechas: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<Pago> findByMetodoPago(String metodoPago) {
        List<Pago> lista = new ArrayList<>();
        String sql = "SELECT * FROM Pago WHERE metodoPago = ?";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setString(1, metodoPago);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("Error findByMetodoPago: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public double getTotalPagosByFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        String sql = "SELECT SUM(monto) as total FROM Pago WHERE fechaPago BETWEEN ? AND ?";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(fechaInicio));
            ps.setDate(2, Date.valueOf(fechaFin));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("total");
            }
        } catch (SQLException e) {
            System.err.println("Error getTotalPagosByFechas: " + e.getMessage());
        }
        return 0.0;
    }

    private Pago mapear(ResultSet rs) throws SQLException {
        Pago p = new Pago();
        p.setIdPago(rs.getInt("idPago"));
        p.setIdReserva(rs.getInt("idReserva"));
        p.setMonto(rs.getDouble("monto"));
        p.setFechaPago(rs.getDate("fechaPago").toLocalDate());
        p.setMetodoPago(rs.getString("metodoPago"));
        return p;
    }
}
