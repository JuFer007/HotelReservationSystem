package Controller;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;

public class PrincipalController {
    @FXML private AnchorPane anchorPanePrincipal;
    @FXML private Button btnCerrarSesion;
    @FXML private MenuBar menuReservas;
    @FXML private MenuItem nuevaReserva;
    @FXML private MenuItem modificarEliminarReserva;
    @FXML private Menu menuHabitaciones;
    @FXML private MenuItem menuVerificarDispo;
    @FXML private Menu menuReportes;
    @FXML private MenuItem menuReporteGeneral;
}
