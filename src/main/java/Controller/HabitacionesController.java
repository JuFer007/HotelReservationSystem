package Controller;
import Model.Habitacion;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class HabitacionesController {
    @FXML private TableView<Habitacion> tablaHabitaciones;
    @FXML private TableColumn<Habitacion, String> columnNumeroHabitacion;
    @FXML private TableColumn<Habitacion, String> columnTipoHabitacion;
    @FXML private TableColumn<Habitacion, String> columnEstadoHabitacion;
    @FXML private TableColumn<Habitacion, String> columnCodigoHabitacion;
    @FXML private ComboBox<String> comboBoxEstado;
}
