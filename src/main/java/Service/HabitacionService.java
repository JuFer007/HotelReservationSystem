package Service;
import DAO.Implement.HabitacionDAOImpl;
import DAO.Implement.TipoHabitacionDAOImpl;
import DAO.Interfaces.IHabitacionDAO;
import DAO.Interfaces.ITipoHabitacionDAO;
import Model.Habitacion;
import Model.TipoHabitacion;
import Patterns.FactoryMethod.HabitacionFactory;
import Patterns.FactoryMethod.IHabitacionTipo;
import java.util.List;

public class HabitacionService {
    private final IHabitacionDAO habitacionDAO;
    private final ITipoHabitacionDAO tipoDAO;

    public HabitacionService() {
        this.habitacionDAO = new HabitacionDAOImpl();
        this.tipoDAO = new TipoHabitacionDAOImpl();
    }

    public boolean registrarNuevaHabitacion(String numeroHabitacion, String tipo) {
        if (habitacionDAO.findByNumero(numeroHabitacion) != null) {
            System.err.println("Error: El número de habitación ya existe.");
            return false;
        }

        IHabitacionTipo habitacionTipo = HabitacionFactory.getHabitacion(tipo);
        if (habitacionTipo == null) {
            System.err.println("Error: El tipo de habitación '" + tipo + "' no es válido.");
            return false;
        }

        TipoHabitacion tipoEnDB = tipoDAO.findByDescripcion(habitacionTipo.getTipo());
        if (tipoEnDB == null) {
            System.err.println("Error: No se encontró el tipo de habitación '" + habitacionTipo.getTipo() + "' en la base de datos.");
            return false;
        }

        Habitacion nuevaHabitacion = new Habitacion();
        nuevaHabitacion.setNumeroHabitacion(numeroHabitacion);
        nuevaHabitacion.setIdTipoHabitacion(tipoEnDB.getIdTipoHabitacion());
        nuevaHabitacion.setEstado("Disponible");

        return habitacionDAO.create(nuevaHabitacion);
    }

    public Habitacion obtenerPorId(int id) {
        return habitacionDAO.read(id);
    }

    public List<Habitacion> obtenerTodas() {
        return habitacionDAO.findAll();
    }

    public List<Habitacion> obtenerDisponibles() {
        return habitacionDAO.findDisponibles();
    }

    public List<Habitacion> obtenerPorTipo(int idTipo) {
        return habitacionDAO.findByTipo(idTipo);
    }

    public List<TipoHabitacion> obtenerTipos() {
        return tipoDAO.findAll();
    }

    public boolean actualizar(Habitacion habitacion) {
        return habitacionDAO.update(habitacion);
    }

    public boolean cambiarEstado(int idHabitacion, String estado) {
        return habitacionDAO.updateEstado(idHabitacion, estado);
    }

    public boolean eliminar(int id) {
        return habitacionDAO.delete(id);
    }
}
