package Service;
import DAO.Implement.HabitacionDAOImpl;
import DAO.Implement.ReservaDAOImpl;
import DAO.Interfaces.IHabitacionDAO;
import DAO.Interfaces.IReservaDAO;
import Model.Reserva;
import Patterns.Command.*;
import Patterns.TemplateMethod.ProcesoReserva;
import Patterns.TemplateMethod.ReservaEspecial;
import Patterns.TemplateMethod.ReservaNormal;
import java.time.LocalDate;
import java.util.List;

public class ReservaService {
    private final IReservaDAO reservaDAO;
    private final IHabitacionDAO habitacionDAO;
    private final CommandManager commandManager;

    public ReservaService() {
        this.reservaDAO = new ReservaDAOImpl();
        this.habitacionDAO = new HabitacionDAOImpl();
        this.commandManager = new CommandManager();
    }

    public boolean registrar(Reserva reserva, boolean esEspecial, double descuento) {
        if (reserva.getFechaIngreso().isAfter(reserva.getFechaSalida())) {
            return false;
        }

        reserva.setFechaReserva(LocalDate.now());
        reserva.setEstadoReserva("Pendiente");

        ProcesoReserva proceso;

        if (esEspecial) {
            proceso = new ReservaEspecial(descuento);
        } else {
            proceso = new ReservaNormal();
        }

        return proceso.procesarReserva(reserva);
    }

    public boolean modificar(int idReserva, LocalDate nuevaIngreso, LocalDate nuevaSalida) {
        IReservaCommand comando = new ModificarReservaCommand(idReserva, nuevaIngreso, nuevaSalida);
        return commandManager.executeCommand(comando);
    }

    public boolean cancelar(int idReserva) {
        Reserva reserva = reservaDAO.read(idReserva);
        if (reserva == null) return false;

        habitacionDAO.updateEstado(reserva.getIdHabitacion(), "Disponible");

        IReservaCommand comando = new CancelarReservaCommand(idReserva);
        return commandManager.executeCommand(comando);
    }

    public boolean deshacerUltimaAccion() {
        return commandManager.undoLast();
    }

    public Reserva obtenerPorId(int id) {
        return reservaDAO.read(id);
    }

    public List<Reserva> obtenerTodas() {
        return reservaDAO.findAll();
    }

    public List<Reserva> obtenerPorCliente(int idCliente) {
        return reservaDAO.findByCliente(idCliente);
    }

    public List<Reserva> obtenerPorEstado(String estado) {
        return reservaDAO.findByEstado(estado);
    }

    public List<Reserva> obtenerPorFechas(LocalDate inicio, LocalDate fin) {
        return reservaDAO.findByFechas(inicio, fin);
    }
}
