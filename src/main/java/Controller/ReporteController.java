package Controller;
import DAO.Implement.ClienteDAOImpl;
import DAO.Implement.HabitacionDAOImpl;
import DAO.Implement.ReservaDAOImpl;
import Model.Reserva;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class ReporteController implements Initializable {

    @FXML private Label cantidadReservas;
    @FXML private Label cantidadHabitaciones;
    @FXML private Label cantidadClientes;
    @FXML private AreaChart<String, Number> graficoReservas;

    // DAOs
    private final ReservaDAOImpl reservaDAO = new ReservaDAOImpl();
    private final HabitacionDAOImpl habitacionDAO = new HabitacionDAOImpl();
    private final ClienteDAOImpl clienteDAO = new ClienteDAOImpl();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cargarEstadisticas();
        cargarGraficoReservas();
        mostrarResumenConsola();
    }

    private void cargarEstadisticas() {
        // Cargar cantidad de reservas activas
        List<Reserva> todasReservas = reservaDAO.findAll();
        long reservasActivas = todasReservas.stream()
                .filter(r -> !r.getEstadoReserva().equalsIgnoreCase("Cancelada") &&
                        !r.getEstadoReserva().equalsIgnoreCase("Finalizada"))
                .count();
        cantidadReservas.setText(String.valueOf(reservasActivas));

        // Cargar cantidad total de habitaciones
        int totalHabitaciones = habitacionDAO.findAll().size();
        cantidadHabitaciones.setText(String.valueOf(totalHabitaciones));

        // Cargar cantidad total de clientes
        int totalClientes = clienteDAO.findAll().size();
        cantidadClientes.setText(String.valueOf(totalClientes));

        System.out.println("Estadísticas cargadas:");
        System.out.println("  Reservas Activas: " + reservasActivas);
        System.out.println("  Total Habitaciones: " + totalHabitaciones);
        System.out.println("  Total Clientes: " + totalClientes);
    }

    private void cargarGraficoReservas() {
        // Configurar título del gráfico
        graficoReservas.setTitle("Reservas por Mes");
        graficoReservas.setLegendVisible(true);

        // Obtener todas las reservas
        List<Reserva> todasReservas = reservaDAO.findAll();

        // Agrupar reservas por mes
        Map<String, Long> reservasPorMes = agruparReservasPorMes(todasReservas);

        // Crear serie de datos
        XYChart.Series<String, Number> serieReservas = new XYChart.Series<>();
        serieReservas.setName("Cantidad de Reservas");

        // Agregar datos a la serie (últimos 6 meses)
        List<String> ultimos6Meses = obtenerUltimos6Meses();

        for (String mes : ultimos6Meses) {
            long cantidad = reservasPorMes.getOrDefault(mes, 0L);
            serieReservas.getData().add(new XYChart.Data<>(mes, cantidad));
        }

        // Agregar serie al gráfico
        graficoReservas.getData().add(serieReservas);

        System.out.println("Gráfico de reservas cargado con " + ultimos6Meses.size() + " meses");
    }

    private Map<String, Long> agruparReservasPorMes(List<Reserva> reservas) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM yyyy", new Locale("es", "ES"));

        return reservas.stream()
                .collect(Collectors.groupingBy(
                        reserva -> reserva.getFechaReserva().format(formatter),
                        Collectors.counting()
                ));
    }

    private List<String> obtenerUltimos6Meses() {
        List<String> meses = new ArrayList<>();
        LocalDate fechaActual = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM yyyy", new Locale("es", "ES"));

        // Generar los últimos 6 meses
        for (int i = 5; i >= 0; i--) {
            LocalDate fecha = fechaActual.minusMonths(i);
            meses.add(fecha.format(formatter));
        }

        return meses;
    }

    private void mostrarResumenConsola() {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║        RESUMEN DEL SISTEMA HOTELERO       ║");
        System.out.println("╚════════════════════════════════════════════╝");

        // Resumen de Reservas
        List<Reserva> todasReservas = reservaDAO.findAll();
        long confirmadas = todasReservas.stream()
                .filter(r -> r.getEstadoReserva().equalsIgnoreCase("Confirmada"))
                .count();
        long enProceso = todasReservas.stream()
                .filter(r -> r.getEstadoReserva().equalsIgnoreCase("En proceso"))
                .count();
        long finalizadas = todasReservas.stream()
                .filter(r -> r.getEstadoReserva().equalsIgnoreCase("Finalizada"))
                .count();
        long canceladas = todasReservas.stream()
                .filter(r -> r.getEstadoReserva().equalsIgnoreCase("Cancelada"))
                .count();

        System.out.println("\n RESERVAS:");
        System.out.println("  ├─ Total: " + todasReservas.size());
        System.out.println("  ├─ Confirmadas: " + confirmadas);
        System.out.println("  ├─ En Proceso: " + enProceso);
        System.out.println("  ├─ Finalizadas: " + finalizadas);
        System.out.println("  └─ Canceladas: " + canceladas);

        // Resumen de Habitaciones
        int disponibles = habitacionDAO.findByEstado("Disponible").size();
        int ocupadas = habitacionDAO.findByEstado("Ocupada").size();
        int reservadas = habitacionDAO.findByEstado("Reservada").size();
        int mantenimiento = habitacionDAO.findByEstado("Mantenimiento").size();
        int totalHab = habitacionDAO.findAll().size();

        System.out.println("\n HABITACIONES:");
        System.out.println("  ├─ Total: " + totalHab);
        System.out.println("  ├─ Disponibles: " + disponibles);
        System.out.println("  ├─ Ocupadas: " + ocupadas);
        System.out.println("  ├─ Reservadas: " + reservadas);
        System.out.println("  └─ Mantenimiento: " + mantenimiento);

        // Tasa de ocupación
        double tasaOcupacion = totalHab > 0 ?
                ((double)(ocupadas + reservadas) / totalHab) * 100 : 0;
        System.out.println("\nÉTRICAS:");
        System.out.println("  └─ Tasa de Ocupación: " + String.format("%.1f%%", tasaOcupacion));

        // Clientes
        int totalClientes = clienteDAO.findAll().size();
        System.out.println("\nCLIENTES:");
        System.out.println("  └─ Total Registrados: " + totalClientes);

        // Reservas por mes (últimos 3 meses)
        System.out.println("\n RESERVAS RECIENTES:");
        Map<String, Long> reservasPorMes = agruparReservasPorMes(todasReservas);
        List<String> ultimos3Meses = obtenerUltimos6Meses().subList(3, 6);

        for (String mes : ultimos3Meses) {
            long cantidad = reservasPorMes.getOrDefault(mes, 0L);
            System.out.println("  ├─ " + mes + ": " + cantidad + " reservas");
        }

        System.out.println("\n╚════════════════════════════════════════════╝\n");
    }

    // Método público para refrescar los datos
    public void actualizarReportes() {
        graficoReservas.getData().clear();
        cargarEstadisticas();
        cargarGraficoReservas();
        mostrarResumenConsola();
        System.out.println("✓ Reportes actualizados");
    }

    public Map<String, Object> obtenerDatosReporte() {
        Map<String, Object> datos = new HashMap<>();

        List<Reserva> reservas = reservaDAO.findAll();
        datos.put("totalReservas", reservas.size());
        datos.put("reservasActivas", reservas.stream()
                .filter(r -> r.getEstadoReserva().equalsIgnoreCase("Confirmada") ||
                        r.getEstadoReserva().equalsIgnoreCase("En proceso"))
                .count());

        datos.put("totalHabitaciones", habitacionDAO.findAll().size());
        datos.put("habitacionesDisponibles", habitacionDAO.findByEstado("Disponible").size());

        datos.put("totalClientes", clienteDAO.findAll().size());

        int totalHab = habitacionDAO.findAll().size();
        int ocupadas = habitacionDAO.findByEstado("Ocupada").size();
        int reservadas = habitacionDAO.findByEstado("Reservada").size();
        double tasaOcupacion = totalHab > 0 ?
                ((double)(ocupadas + reservadas) / totalHab) * 100 : 0;
        datos.put("tasaOcupacion", tasaOcupacion);

        return datos;
    }
}