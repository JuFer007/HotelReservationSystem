package Service;
import DAO.Implement.PagoDAOImpl;
import DAO.Interfaces.IPagoDAO;
import Model.Pago;
import java.time.LocalDate;
import java.util.List;

public class PagoService {
    private final IPagoDAO pagoDAO;

    public PagoService() {
        this.pagoDAO = new PagoDAOImpl();
    }

    public boolean registrar(Pago pago) {
        if (pagoDAO.findByReserva(pago.getIdReserva()) != null) {
            System.err.println("Ya existe un pago para esta reserva");
            return false;
        }
        if (pago.getMonto() <= 0) {
            System.err.println("El monto debe ser mayor a cero");
            return false;
        }
        return pagoDAO.create(pago);
    }

    public Pago obtenerPorId(int id) {
        return pagoDAO.read(id);
    }

    public Pago buscarPorReserva(int idReserva) {
        return pagoDAO.findByReserva(idReserva);
    }

    public List<Pago> obtenerTodos() {
        return pagoDAO.findAll();
    }

    public List<Pago> buscarPorFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        return pagoDAO.findByFechas(fechaInicio, fechaFin);
    }

    public List<Pago> buscarPorMetodoPago(String metodoPago) {
        return pagoDAO.findByMetodoPago(metodoPago);
    }

    public double obtenerTotalPorFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        return pagoDAO.getTotalPagosByFechas(fechaInicio, fechaFin);
    }

    public boolean actualizar(Pago pago) {
        Pago existente = pagoDAO.read(pago.getIdPago());
        if (existente == null) {
            System.err.println("Pago no encontrado");
            return false;
        }
        return pagoDAO.update(pago);
    }

    public boolean eliminar(int id) {
        return pagoDAO.delete(id);
    }

    public double calcularTotalDiario() {
        LocalDate hoy = LocalDate.now();
        return pagoDAO.getTotalPagosByFechas(hoy, hoy);
    }

    public double calcularTotalMensual() {
        LocalDate hoy = LocalDate.now();
        LocalDate inicioMes = hoy.withDayOfMonth(1);
        LocalDate finMes = hoy.withDayOfMonth(hoy.lengthOfMonth());
        return pagoDAO.getTotalPagosByFechas(inicioMes, finMes);
    }
}
