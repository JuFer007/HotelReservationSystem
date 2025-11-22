package Service;
import DAO.Implement.ClienteDAOImpl;
import DAO.Implement.HabitacionDAOImpl;
import DAO.Implement.PagoDAOImpl;
import DAO.Implement.ReservaDAOImpl;
import DAO.Interfaces.IClienteDAO;
import DAO.Interfaces.IHabitacionDAO;
import DAO.Interfaces.IPagoDAO;
import DAO.Interfaces.IReservaDAO;
import Model.Cliente;
import Model.Habitacion;
import Model.Pago;
import Model.Reserva;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReporteService {
    private final IPagoDAO pagoDAO;
    private final IReservaDAO reservaDAO;
    private final IHabitacionDAO habitacionDAO;
    private final IClienteDAO clienteDAO;

    public ReporteService() {
        this.pagoDAO = new PagoDAOImpl();
        this.reservaDAO = new ReservaDAOImpl();
        this.habitacionDAO = new HabitacionDAOImpl();
        this.clienteDAO = new ClienteDAOImpl();
    }

    public Map<String, Object> generarReporteIngresos(LocalDate fechaInicio, LocalDate fechaFin) {
        Map<String, Object> reporte = new HashMap<>();
        List<Pago> pagos = pagoDAO.findByFechas(fechaInicio, fechaFin);
        double total = pagoDAO.getTotalPagosByFechas(fechaInicio, fechaFin);

        Map<String, Double> ingresosPorMetodo = new HashMap<>();
        for (Pago pago : pagos) {
            String metodo = pago.getMetodoPago();
            ingresosPorMetodo.put(metodo,
                    ingresosPorMetodo.getOrDefault(metodo, 0.0) + pago.getMonto());
        }

        reporte.put("fechaInicio", fechaInicio);
        reporte.put("fechaFin", fechaFin);
        reporte.put("totalIngresos", total);
        reporte.put("cantidadPagos", pagos.size());
        reporte.put("ingresosPorMetodo", ingresosPorMetodo);
        reporte.put("promedioIngreso", pagos.isEmpty() ? 0 : total / pagos.size());

        return reporte;
    }

    // Reporte de Reservas
    public Map<String, Object> generarReporteReservas(LocalDate fechaInicio, LocalDate fechaFin) {
        Map<String, Object> reporte = new HashMap<>();
        List<Reserva> reservas = reservaDAO.findByFechas(fechaInicio, fechaFin);

        Map<String, Integer> reservasPorEstado = new HashMap<>();
        for (Reserva reserva : reservas) {
            String estado = reserva.getEstadoReserva();
            reservasPorEstado.put(estado,
                    reservasPorEstado.getOrDefault(estado, 0) + 1);
        }

        reporte.put("fechaInicio", fechaInicio);
        reporte.put("fechaFin", fechaFin);
        reporte.put("totalReservas", reservas.size());
        reporte.put("reservasPorEstado", reservasPorEstado);

        return reporte;
    }

    public Map<String, Object> generarReporteOcupacion() {
        Map<String, Object> reporte = new HashMap<>();
        List<Habitacion> todas = habitacionDAO.findAll();
        List<Habitacion> disponibles = habitacionDAO.findByEstado("DISPONIBLE");
        List<Habitacion> ocupadas = habitacionDAO.findByEstado("OCUPADA");

        double porcentajeOcupacion = todas.isEmpty() ? 0 :
                (ocupadas.size() * 100.0 / todas.size());

        reporte.put("totalHabitaciones", todas.size());
        reporte.put("disponibles", disponibles.size());
        reporte.put("ocupadas", ocupadas.size());
        reporte.put("porcentajeOcupacion", porcentajeOcupacion);

        return reporte;
    }

    public List<Map<String, Object>> generarReporteClientesFrecuentes() {
        List<Map<String, Object>> reporte = new ArrayList<>();
        List<Cliente> clientes = clienteDAO.findAll();

        for (Cliente cliente : clientes) {
            List<Reserva> reservas = reservaDAO.findByCliente(cliente.getIdCliente());
            if (!reservas.isEmpty()) {
                Map<String, Object> info = new HashMap<>();
                info.put("cliente", cliente);
                info.put("totalReservas", reservas.size());

                double totalGastado = 0;
                for (Reserva reserva : reservas) {
                    Pago pago = pagoDAO.findByReserva(reserva.getIdReserva());
                    if (pago != null) totalGastado += pago.getMonto();
                }
                info.put("totalGastado", totalGastado);
                reporte.add(info);
            }
        }

        reporte.sort((a, b) ->
                Integer.compare((Integer)b.get("totalReservas"), (Integer)a.get("totalReservas")));

        return reporte;
    }

    public Map<String, Object> generarReporteMensual(int mes, int anio) {
        LocalDate inicio = LocalDate.of(anio, mes, 1);
        LocalDate fin = inicio.withDayOfMonth(inicio.lengthOfMonth());

        Map<String, Object> reporte = new HashMap<>();
        reporte.put("mes", mes);
        reporte.put("anio", anio);
        reporte.put("ingresos", generarReporteIngresos(inicio, fin));
        reporte.put("reservas", generarReporteReservas(inicio, fin));
        reporte.put("ocupacion", generarReporteOcupacion());

        return reporte;
    }

    public String exportarTexto(Map<String, Object> reporte) {
        StringBuilder sb = new StringBuilder();
        sb.append("=".repeat(60)).append("\n");
        sb.append("REPORTE GENERADO\n");
        sb.append("Fecha: ").append(LocalDate.now()).append("\n");
        sb.append("=".repeat(60)).append("\n\n");

        for (Map.Entry<String, Object> entry : reporte.entrySet()) {
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }

        return sb.toString();
    }
}
