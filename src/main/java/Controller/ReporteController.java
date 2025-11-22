package Controller;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.chart.AreaChart;

public class ReporteController {
    @FXML private Label cantidadReservas;
    @FXML private Label cantidadHabitaciones;
    @FXML private Label cantidadClientes;
    @FXML private AreaChart<?, ?> graficoReservas;
}
