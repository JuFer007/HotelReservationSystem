package Controller;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class ModificarEliminarReservaController {
    @FXML private TextField cajaNombre;
    @FXML private TextField cajaApellidos;
    @FXML private TextField cajaCorreoE;
    @FXML private TextField cajaTelefono;
    @FXML private DatePicker cajaFechaInicio;
    @FXML private DatePicker cajaFechaSalida;
    @FXML private Button btnModificarReserva;
    @FXML private Button btnEliminarReserva;
    @FXML private TableView<?> tablaReservas;
    @FXML private TableColumn<?, ?> clumnNumReserva;
    @FXML private TableColumn<?, ?> columnCliente;
    @FXML private TableColumn<?, ?> columnFechaIniciio;
    @FXML private TableColumn<?, ?> columnFechaSalida;
    @FXML private TableColumn<?, ?> columnTipoHabitaciones;
    @FXML private TableColumn<?, ?> columnNumHabitacion;
}
