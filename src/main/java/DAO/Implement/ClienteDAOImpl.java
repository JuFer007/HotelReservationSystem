package DAO.Implement;

import Conection.DatabaseConnection;
import DAO.Interfaces.IClienteDAO;
import Model.Cliente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAOImpl implements IClienteDAO {
    private final DatabaseConnection db = DatabaseConnection.getInstance();

    @Override
    public boolean create(Cliente c) {
        String sql = "INSERT INTO Cliente (nombre, apellidoPaterno, apellidoMaterno, dni, telefono, email) VALUES (?,?,?,?,?,?)";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, c.getNombre());
            ps.setString(2, c.getApellidoPaterno());
            ps.setString(3, c.getApellidoMaterno());
            ps.setString(4, c.getDni());
            ps.setString(5, c.getTelefono());
            ps.setString(6, c.getEmail());

            int result = ps.executeUpdate();

            if (result > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    c.setIdCliente(rs.getInt(1));
                    System.out.println("Cliente insertado con ID: " + c.getIdCliente());
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error create Cliente: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return false;
    }

    @Override
    public Cliente read(int id) {
        String sql = "SELECT * FROM Cliente WHERE idCliente = ?";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            System.err.println("Error read Cliente: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean update(Cliente c) {
        String sql = "UPDATE Cliente SET nombre=?, apellidoPaterno=?, apellidoMaterno=?, dni=?, telefono=?, email=? WHERE idCliente=?";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setString(1, c.getNombre());
            ps.setString(2, c.getApellidoPaterno());
            ps.setString(3, c.getApellidoMaterno());
            ps.setString(4, c.getDni());
            ps.setString(5, c.getTelefono());
            ps.setString(6, c.getEmail());
            ps.setInt(7, c.getIdCliente());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error update Cliente: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM Cliente WHERE idCliente = ?";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error delete Cliente: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Cliente> findAll() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM Cliente ORDER BY apellidoPaterno, nombre";
        try (Statement st = db.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("Error findAll Cliente: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public Cliente findByDni(String dni) {
        String sql = "SELECT * FROM Cliente WHERE dni = ?";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setString(1, dni);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            System.err.println("Error findByDni: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Cliente> findByNombre(String nombre) {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM Cliente WHERE nombre LIKE ?";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setString(1, "%" + nombre + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("Error findByNombre: " + e.getMessage());
        }
        return lista;
    }

    private Cliente mapear(ResultSet rs) throws SQLException {
        return new Cliente(
                rs.getInt("idCliente"),
                rs.getString("nombre"),
                rs.getString("apellidoPaterno"),
                rs.getString("apellidoMaterno"),
                rs.getString("dni"),
                rs.getString("telefono"),
                rs.getString("email")
        );
    }
}