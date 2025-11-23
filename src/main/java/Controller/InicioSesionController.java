package Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

import java.io.IOException;

public class InicioSesionController {
    @FXML private Button btnIngresar;
    @FXML private TextField cajaNombreUsuario;
    @FXML private PasswordField cajaContraseña;
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

    @FXML
    private void Login(ActionEvent event) {
        Stage stageAactual = (Stage) btnIngresar.getScene().getWindow();
        stageAactual.close();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/Principal/fmrPrincipal.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stageAactual.setScene(scene);
            stageAactual.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
