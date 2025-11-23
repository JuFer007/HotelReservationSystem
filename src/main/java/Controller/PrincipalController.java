package Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;

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
    @FXML private Button btnCerrarVentana;
    @FXML private Button btnMinizarVentana;

    //CERRAR LA VENTANA
    @FXML
    private void CloserWindow(ActionEvent event){
        Stage stage = (Stage) btnCerrarVentana.getScene().getWindow();
        stage.close();
    }

    //MINIMIZAR LA VENTANA
    @FXML
    private void Minimizer(ActionEvent event){
        Stage stage = (Stage) btnMinizarVentana.getScene().getWindow();
        stage.setIconified(true);
    }

    //CERRAR LA SESION
    @FXML
    private void Logout(){
        Stage stageActual = (Stage) btnCerrarSesion.getScene().getWindow();
        stageActual.close();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/InicioSesion/fmrInicioSesion.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stageActual.setScene(scene);
            stageActual.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //PARA INICIAR EL FORMULARIO
    private void cargarFormulario(String ruta){
        try {
            Parent root = FXMLLoader.load(getClass().getResource(ruta));

            anchorPanePrincipal.getChildren().clear();
            anchorPanePrincipal.getChildren().add(root);

            AnchorPane.setTopAnchor(root, 0.0);
            AnchorPane.setBottomAnchor(root, 0.0);
            AnchorPane.setLeftAnchor(root, 0.0);
            AnchorPane.setRightAnchor(root, 0.0);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void CReportes() throws IOException {
        cargarFormulario("/Views/Reportes/fmrReporte.fxml");
    }

    @FXML
    private void ModificarReserva() throws IOException {
        cargarFormulario("/Views/Reservas/fmrModificarEliminar_Reserva.fxml");
    }

    @FXML
    private void NuevaReserva() throws IOException {
        cargarFormulario("/Views/Reservas/fmrNuevaReserva.fxml");
    }

    @FXML
    private void CHabitacion() throws IOException{
        cargarFormulario("/Views/Habitaciones/fmrHabitaciones.fxml");
    }
}
