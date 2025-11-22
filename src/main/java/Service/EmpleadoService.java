package Service;
import DAO.Implement.EmpleadoDAOImpl;
import DAO.Interfaces.IEmpleadoDAO;
import Model.Empleado;
import java.util.List;

public class EmpleadoService {
    private final IEmpleadoDAO empleadoDAO;

    public EmpleadoService() {
        this.empleadoDAO = new EmpleadoDAOImpl();
    }

    public boolean registrar(Empleado empleado) {
        if (empleadoDAO.findByDni(empleado.getDni()) != null) {
            System.err.println("Ya existe un empleado con ese DNI");
            return false;
        }
        return empleadoDAO.create(empleado);
    }

    public Empleado obtenerPorId(int id) {
        return empleadoDAO.read(id);
    }

    public Empleado buscarPorDni(String dni) {
        return empleadoDAO.findByDni(dni);
    }

    public List<Empleado> obtenerTodos() {
        return empleadoDAO.findAll();
    }

    public boolean actualizar(Empleado empleado) {
        Empleado existente = empleadoDAO.read(empleado.getIdEmpleado());
        if (existente == null) {
            System.err.println("Empleado no encontrado");
            return false;
        }
        return empleadoDAO.update(empleado);
    }

    public boolean eliminar(int id) {
        return empleadoDAO.delete(id);
    }

    public boolean validarDni(String dni) {
        return dni != null && dni.matches("^[0-9]{8}$");
    }

    public boolean validarTelefono(String telefono) {
        return telefono != null && telefono.matches("^[0-9]{9,15}$");
    }
}
