package Service;
import DAO.Implement.ClienteDAOImpl;
import DAO.Interfaces.IClienteDAO;
import Model.Cliente;
import java.util.List;

public class ClienteService {
    private final IClienteDAO clienteDAO;

    public ClienteService() {
        this.clienteDAO = new ClienteDAOImpl();
    }

    public boolean registrar(Cliente cliente) {
        if (clienteDAO.findByDni(cliente.getDni()) != null) {
            return false;
        }
        return clienteDAO.create(cliente);
    }

    public Cliente obtenerPorId(int id) {
        return clienteDAO.read(id);
    }

    public Cliente buscarPorDni(String dni) {
        return clienteDAO.findByDni(dni);
    }

    public List<Cliente> obtenerTodos() {
        return clienteDAO.findAll();
    }

    public List<Cliente> buscarPorNombre(String nombre) {
        return clienteDAO.findByNombre(nombre);
    }

    public boolean actualizar(Cliente cliente) {
        return clienteDAO.update(cliente);
    }

    public boolean eliminar(int id) {
        return clienteDAO.delete(id);
    }
}
