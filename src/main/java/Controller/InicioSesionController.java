package Controller;

import Conection.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InicioSesionController {

    @FXML private TextField cajaNombreUsuario;
    @FXML private PasswordField cajaContraseña;
    @FXML private Button btnIngresar;
    @FXML private Button btnMinizarVentana;

    @FXML
    private void Login(ActionEvent event) {
        String usuario = cajaNombreUsuario.getText().trim();
        String contraseña = cajaContraseña.getText().trim();

        if (usuario.isEmpty() || contraseña.isEmpty()) {
            mostrarAlerta("Campos Vacíos", "Por favor ingrese usuario y contraseña", Alert.AlertType.WARNING);
            return;
        }

        if (validarUsuario(usuario, contraseña)) {
            Stage stageActual = (Stage) btnIngresar.getScene().getWindow();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/Principal/fmrPrincipal.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                stageActual.setScene(scene);
                stageActual.setTitle("Sistema Hotel - Panel Principal");
                stageActual.show();
            } catch (IOException e) {
                e.printStackTrace();
                mostrarAlerta("Error", "No se pudo cargar la ventana principal", Alert.AlertType.ERROR);
            }
        } else {
            mostrarAlerta("Acceso Denegado", "Usuario o contraseña incorrectos", Alert.AlertType.ERROR);
            cajaContraseña.clear();
        }
    }

    private boolean validarUsuario(String usuario, String contraseña) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getInstance().getConnection();

            if (conn == null) {
                mostrarAlerta("Error de Conexión",
                        "No se pudo conectar a la base de datos.\nVerifica que MySQL esté corriendo.",
                        Alert.AlertType.ERROR);
                return false;
            }

            String query = "SELECT u.idUsuario, u.nombreUsuario, e.nombre, e.apellidoPaterno " +
                    "FROM Usuario u " +
                    "INNER JOIN Empleado e ON u.idEmpleado = e.idEmpleado " +
                    "INNER JOIN Administrador a ON e.idEmpleado = a.idEmpleado " +
                    "WHERE u.nombreUsuario = ? AND u.contraseña = ?";

            try (PreparedStatement pst = conn.prepareStatement(query)) {
                pst.setString(1, usuario);
                pst.setString(2, contraseña);

                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    String nombreCompleto = rs.getString("nombre") + " " + rs.getString("apellidoPaterno");
                    System.out.println("Login exitoso: " + nombreCompleto);
                    return true;
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al validar usuario: " + e.getMessage());
            e.printStackTrace();
            mostrarAlerta("Error SQL", "Error al consultar la base de datos:\n" + e.getMessage(), Alert.AlertType.ERROR);
        }

        return false;
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    @FXML
    private void CloserWindow(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void Minimizer(ActionEvent event) {
        Stage stage = (Stage) btnMinizarVentana.getScene().getWindow();
        stage.setIconified(true);
    }
}