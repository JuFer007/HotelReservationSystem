package Controller;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class NuevaReservaController {
    @FXML private TextField nombreNuevaReserva;
    @FXML private TextField apellidosNuevaReserva;
    @FXML private TextField correoNuevaReserva;
    @FXML private TextField cajaTelefNuevaReserva;
    @FXML private DatePicker fechaInicioNuevaReserva;
    @FXML private DatePicker fechaSalidaNuevaReserva;
    @FXML private Button btnAgregarReserva;
    @FXML private ListView<?> listaClientes;
    @FXML private TableView<?> tablaHabitaciones;
    @FXML private TableColumn<?, ?> columnCodigoHabitacion;
    @FXML private TableColumn<?, ?> columnTipoHabitacion;
    @FXML private TableColumn<?, ?> columnNumHabitacion;
    @FXML private TableColumn<?, ?> columnEstadoHabitacion;
}
