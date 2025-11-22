package DAO.Implement;
import Conection.DatabaseConnection;
import DAO.Interfaces.IReservaDAO;
import Model.Cliente;
import Model.Empleado;
import Model.Habitacion;
import Model.Reserva;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservaDAOImpl implements IReservaDAO {
    private final DatabaseConnection db = DatabaseConnection.getInstance();

    @Override
    public boolean create(Reserva r) {
        String sql = "INSERT INTO Reserva (idCliente, idEmpleado, idHabitacion, fechaReserva, " +
                "fechaIngreso, fechaSalida, estadoReserva) VALUES (?,?,?,?,?,?,?)";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setInt(1, r.getIdCliente());
            ps.setInt(2, r.getIdEmpleado());
            ps.setInt(3, r.getIdHabitacion());
            ps.setDate(4, Date.valueOf(r.getFechaReserva()));
            ps.setDate(5, Date.valueOf(r.getFechaIngreso()));
            ps.setDate(6, Date.valueOf(r.getFechaSalida()));
            ps.setString(7, r.getEstadoReserva());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error create Reserva: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Reserva read(int id) {
        String sql = "SELECT r.*, " +
                "c.nombre AS cNombre, c.apellidoPaterno AS cApellidoP, c.dni AS cDni, " +
                "e.nombre AS eNombre, e.apellidoPaterno AS eApellidoP, " +
                "h.numeroHabitacion " +
                "FROM Reserva r " +
                "INNER JOIN Cliente c ON r.idCliente = c.idCliente " +
                "INNER JOIN Empleado e ON r.idEmpleado = e.idEmpleado " +
                "INNER JOIN Habitacion h ON r.idHabitacion = h.idHabitacion " +
                "WHERE r.idReserva = ?";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapearCompleto(rs);
        } catch (SQLException e) {
            System.err.println("Error read Reserva: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean update(Reserva r) {
        String sql = "UPDATE Reserva SET idCliente=?, idEmpleado=?, idHabitacion=?, " +
                "fechaReserva=?, fechaIngreso=?, fechaSalida=?, estadoReserva=? " +
                "WHERE idReserva=?";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setInt(1, r.getIdCliente());
            ps.setInt(2, r.getIdEmpleado());
            ps.setInt(3, r.getIdHabitacion());
            ps.setDate(4, Date.valueOf(r.getFechaReserva()));
            ps.setDate(5, Date.valueOf(r.getFechaIngreso()));
            ps.setDate(6, Date.valueOf(r.getFechaSalida()));
            ps.setString(7, r.getEstadoReserva());
            ps.setInt(8, r.getIdReserva());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error update Reserva: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM Reserva WHERE idReserva = ?";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error delete Reserva: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Reserva> findAll() {
        List<Reserva> lista = new ArrayList<>();
        String sql = "SELECT r.*, " +
                "c.nombre AS cNombre, c.apellidoPaterno AS cApellidoP, c.dni AS cDni, " +
                "e.nombre AS eNombre, e.apellidoPaterno AS eApellidoP, " +
                "h.numeroHabitacion " +
                "FROM Reserva r " +
                "INNER JOIN Cliente c ON r.idCliente = c.idCliente " +
                "INNER JOIN Empleado e ON r.idEmpleado = e.idEmpleado " +
                "INNER JOIN Habitacion h ON r.idHabitacion = h.idHabitacion " +
                "ORDER BY r.fechaReserva DESC";
        try (Statement st = db.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapearCompleto(rs));
        } catch (SQLException e) {
            System.err.println("Error findAll Reserva: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<Reserva> findByCliente(int idCliente) {
        List<Reserva> lista = new ArrayList<>();
        String sql = "SELECT r.*, c.nombre AS cNombre, c.apellidoPaterno AS cApellidoP, c.dni AS cDni, " +
                "e.nombre AS eNombre, e.apellidoPaterno AS eApellidoP, h.numeroHabitacion " +
                "FROM Reserva r " +
                "INNER JOIN Cliente c ON r.idCliente = c.idCliente " +
                "INNER JOIN Empleado e ON r.idEmpleado = e.idEmpleado " +
                "INNER JOIN Habitacion h ON r.idHabitacion = h.idHabitacion " +
                "WHERE r.idCliente = ?";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapearCompleto(rs));
        } catch (SQLException e) {
            System.err.println("Error findByCliente: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<Reserva> findByHabitacion(int idHabitacion) {
        List<Reserva> lista = new ArrayList<>();
        String sql = "SELECT r.*, c.nombre AS cNombre, c.apellidoPaterno AS cApellidoP, c.dni AS cDni, " +
                "e.nombre AS eNombre, e.apellidoPaterno AS eApellidoP, h.numeroHabitacion " +
                "FROM Reserva r " +
                "INNER JOIN Cliente c ON r.idCliente = c.idCliente " +
                "INNER JOIN Empleado e ON r.idEmpleado = e.idEmpleado " +
                "INNER JOIN Habitacion h ON r.idHabitacion = h.idHabitacion " +
                "WHERE r.idHabitacion = ?";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setInt(1, idHabitacion);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapearCompleto(rs));
        } catch (SQLException e) {
            System.err.println("Error findByHabitacion: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<Reserva> findByEstado(String estado) {
        List<Reserva> lista = new ArrayList<>();
        String sql = "SELECT r.*, c.nombre AS cNombre, c.apellidoPaterno AS cApellidoP, c.dni AS cDni, " +
                "e.nombre AS eNombre, e.apellidoPaterno AS eApellidoP, h.numeroHabitacion " +
                "FROM Reserva r " +
                "INNER JOIN Cliente c ON r.idCliente = c.idCliente " +
                "INNER JOIN Empleado e ON r.idEmpleado = e.idEmpleado " +
                "INNER JOIN Habitacion h ON r.idHabitacion = h.idHabitacion " +
                "WHERE r.estadoReserva = ?";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setString(1, estado);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapearCompleto(rs));
        } catch (SQLException e) {
            System.err.println("Error findByEstado: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<Reserva> findByFechas(LocalDate inicio, LocalDate fin) {
        List<Reserva> lista = new ArrayList<>();
        String sql = "SELECT r.*, c.nombre AS cNombre, c.apellidoPaterno AS cApellidoP, c.dni AS cDni, " +
                "e.nombre AS eNombre, e.apellidoPaterno AS eApellidoP, h.numeroHabitacion " +
                "FROM Reserva r " +
                "INNER JOIN Cliente c ON r.idCliente = c.idCliente " +
                "INNER JOIN Empleado e ON r.idEmpleado = e.idEmpleado " +
                "INNER JOIN Habitacion h ON r.idHabitacion = h.idHabitacion " +
                "WHERE r.fechaReserva BETWEEN ? AND ?";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(inicio));
            ps.setDate(2, Date.valueOf(fin));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapearCompleto(rs));
        } catch (SQLException e) {
            System.err.println("Error findByFechas: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public boolean updateEstado(int idReserva, String estado) {
        String sql = "UPDATE Reserva SET estadoReserva = ? WHERE idReserva = ?";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setString(1, estado);
            ps.setInt(2, idReserva);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updateEstado: " + e.getMessage());
            return false;
        }
    }

    private Reserva mapearCompleto(ResultSet rs) throws SQLException {
        Reserva r = new Reserva(
                rs.getInt("idReserva"),
                rs.getInt("idCliente"),
                rs.getInt("idEmpleado"),
                rs.getInt("idHabitacion"),
                rs.getDate("fechaReserva").toLocalDate(),
                rs.getDate("fechaIngreso").toLocalDate(),
                rs.getDate("fechaSalida").toLocalDate(),
                rs.getString("estadoReserva")
        );

        Cliente c = new Cliente();
        c.setIdCliente(rs.getInt("idCliente"));
        c.setNombre(rs.getString("cNombre"));
        c.setApellidoPaterno(rs.getString("cApellidoP"));
        c.setDni(rs.getString("cDni"));
        r.setCliente(c);

        Empleado e = new Empleado();
        e.setIdEmpleado(rs.getInt("idEmpleado"));
        e.setNombre(rs.getString("eNombre"));
        e.setApellidoPaterno(rs.getString("eApellidoP"));
        r.setEmpleado(e);

        Habitacion h = new Habitacion();
        h.setIdHabitacion(rs.getInt("idHabitacion"));
        h.setNumeroHabitacion(rs.getString("numeroHabitacion"));
        r.setHabitacion(h);
        return r;
    }
}
