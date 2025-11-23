package Controller;
import DAO.Implement.ClienteDAOImpl;
import DAO.Implement.HabitacionDAOImpl;
import DAO.Implement.ReservaDAOImpl;
import Model.Cliente;
import Model.Reserva;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ModificarEliminarReservaController implements Initializable {

    @FXML private TextField cajaNombre;
    @FXML private TextField cajaApellidos;
    @FXML private TextField cajaCorreoE;
    @FXML private TextField cajaTelefono;
    @FXML private DatePicker cajaFechaInicio;
    @FXML private DatePicker cajaFechaSalida;
    @FXML private Button btnModificarReserva;
    @FXML private Button btnEliminarReserva;

    @FXML private TableView<Reserva> tablaReservas;
    @FXML private TableColumn<Reserva, Integer> clumnNumReserva;
    @FXML private TableColumn<Reserva, String> columnCliente;
    @FXML private TableColumn<Reserva, LocalDate> columnFechaIniciio;
    @FXML private TableColumn<Reserva, LocalDate> columnFechaSalida;
    @FXML private TableColumn<Reserva, String> columnTipoHabitaciones;
    @FXML private TableColumn<Reserva, String> columnNumHabitacion;

    private final ReservaDAOImpl reservaDAO = new ReservaDAOImpl();
    private final ClienteDAOImpl clienteDAO = new ClienteDAOImpl();
    private final HabitacionDAOImpl habitacionDAO = new HabitacionDAOImpl();

    private ObservableList<Reserva> listaReservas;

    private Reserva reservaSeleccionada = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configurarTablaReservas();
        cargarReservas();
        configurarEventos();
        deshabilitarFormulario();
    }

    private void configurarTablaReservas() {
        clumnNumReserva.setCellValueFactory(new PropertyValueFactory<>("idReserva"));

        columnCliente.setCellValueFactory(cellData -> {
            Reserva reserva = cellData.getValue();
            if (reserva.getCliente() != null) {
                return new javafx.beans.property.SimpleStringProperty(
                        reserva.getCliente().getNombreCompleto()
                );
            }
            return new javafx.beans.property.SimpleStringProperty("N/A");
        });

        columnFechaIniciio.setCellValueFactory(new PropertyValueFactory<>("fechaIngreso"));
        columnFechaSalida.setCellValueFactory(new PropertyValueFactory<>("fechaSalida"));

        columnTipoHabitaciones.setCellValueFactory(cellData -> {
            Reserva reserva = cellData.getValue();
            if (reserva.getHabitacion() != null && reserva.getHabitacion().getTipoHabitacion() != null) {
                return new javafx.beans.property.SimpleStringProperty(
                        reserva.getHabitacion().getTipoHabitacion().getDescripcion()
                );
            }
            return new javafx.beans.property.SimpleStringProperty("N/A");
        });

        columnNumHabitacion.setCellValueFactory(cellData -> {
            Reserva reserva = cellData.getValue();
            if (reserva.getHabitacion() != null) {
                return new javafx.beans.property.SimpleStringProperty(
                        reserva.getHabitacion().getNumeroHabitacion()
                );
            }
            return new javafx.beans.property.SimpleStringProperty("N/A");
        });
    }

    private void cargarReservas() {
        List<Reserva> reservas = reservaDAO.findAll();

        reservas.removeIf(r -> r.getEstadoReserva().equalsIgnoreCase("Finalizada") ||
                r.getEstadoReserva().equalsIgnoreCase("Cancelada"));

        listaReservas = FXCollections.observableArrayList(reservas);
        tablaReservas.setItems(listaReservas);

        System.out.println("Reservas cargadas: " + reservas.size());
    }

    private void configurarEventos() {
        tablaReservas.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                reservaSeleccionada = newVal;
                llenarFormularioConReserva(newVal);
                habilitarFormulario();
            } else {
                reservaSeleccionada = null;
                limpiarFormulario();
                deshabilitarFormulario();
            }
        });

        btnModificarReserva.setOnAction(event -> modificarReserva());

        btnEliminarReserva.setOnAction(event -> eliminarReserva());
    }

    private void llenarFormularioConReserva(Reserva reserva) {
        if (reserva.getCliente() != null) {
            cajaNombre.setText(reserva.getCliente().getNombre());

            String apellidos = reserva.getCliente().getApellidoPaterno();
            if (reserva.getCliente().getApellidoMaterno() != null &&
                    !reserva.getCliente().getApellidoMaterno().isEmpty()) {
                apellidos += " " + reserva.getCliente().getApellidoMaterno();
            }
            cajaApellidos.setText(apellidos);

            cajaCorreoE.setText(reserva.getCliente().getEmail() != null ?
                    reserva.getCliente().getEmail() : "");
            cajaTelefono.setText(reserva.getCliente().getTelefono());
        }

        cajaFechaInicio.setValue(reserva.getFechaIngreso());
        cajaFechaSalida.setValue(reserva.getFechaSalida());
    }

    @FXML
    private void modificarReserva() {
        if (reservaSeleccionada == null) {
            mostrarAlerta("Sin Selección",
                    "Por favor seleccione una reserva de la tabla",
                    Alert.AlertType.WARNING);
            return;
        }

        if (!validarFechas()) {
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar Modificación");
        confirmacion.setHeaderText("¿Está seguro de modificar esta reserva?");
        confirmacion.setContentText("Reserva N° " + reservaSeleccionada.getIdReserva() +
                " - Cliente: " + reservaSeleccionada.getCliente().getNombreCompleto());

        Optional<ButtonType> resultado = confirmacion.showAndWait();

        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            try {
                Cliente cliente = reservaSeleccionada.getCliente();
                cliente.setNombre(cajaNombre.getText().trim());

                String[] apellidos = cajaApellidos.getText().trim().split("\\s+", 2);
                cliente.setApellidoPaterno(apellidos[0]);
                cliente.setApellidoMaterno(apellidos.length > 1 ? apellidos[1] : "");

                cliente.setEmail(cajaCorreoE.getText().trim());
                cliente.setTelefono(cajaTelefono.getText().trim());

                boolean clienteActualizado = clienteDAO.update(cliente);

                reservaSeleccionada.setFechaIngreso(cajaFechaInicio.getValue());
                reservaSeleccionada.setFechaSalida(cajaFechaSalida.getValue());

                boolean reservaActualizada = reservaDAO.update(reservaSeleccionada);

                if (clienteActualizado && reservaActualizada) {
                    mostrarAlerta("Éxito",
                            "Reserva modificada correctamente",
                            Alert.AlertType.INFORMATION);
                    cargarReservas();
                    limpiarFormulario();
                    deshabilitarFormulario();
                } else {
                    mostrarAlerta("Error",
                            "No se pudo modificar la reserva completamente",
                            Alert.AlertType.ERROR);
                }

            } catch (Exception e) {
                System.err.println("Error al modificar reserva: " + e.getMessage());
                e.printStackTrace();
                mostrarAlerta("Error",
                        "Ocurrió un error al modificar la reserva",
                        Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void eliminarReserva() {
        if (reservaSeleccionada == null) {
            mostrarAlerta("Sin Selección",
                    "Por favor seleccione una reserva de la tabla",
                    Alert.AlertType.WARNING);
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar Eliminación");
        confirmacion.setHeaderText("¿Está seguro de eliminar esta reserva?");
        confirmacion.setContentText("Reserva N° " + reservaSeleccionada.getIdReserva() +
                "\nCliente: " + reservaSeleccionada.getCliente().getNombreCompleto() +
                "\n\nEsta acción no se puede deshacer.");

        Optional<ButtonType> resultado = confirmacion.showAndWait();

        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            try {
                int idHabitacion = reservaSeleccionada.getIdHabitacion();

                boolean reservaEliminada = reservaDAO.delete(reservaSeleccionada.getIdReserva());

                if (reservaEliminada) {
                    habitacionDAO.updateEstado(idHabitacion, "Disponible");

                    mostrarAlerta("Éxito",
                            "Reserva eliminada correctamente",
                            Alert.AlertType.INFORMATION);
                    cargarReservas(); // Recargar tabla
                    limpiarFormulario();
                    deshabilitarFormulario();
                } else {
                    mostrarAlerta("Error",
                            "No se pudo eliminar la reserva",
                            Alert.AlertType.ERROR);
                }

            } catch (Exception e) {
                System.err.println("Error al eliminar reserva: " + e.getMessage());
                e.printStackTrace();
                mostrarAlerta("Error",
                        "Ocurrió un error al eliminar la reserva.\n" +
                                "Verifique que no existan pagos asociados.",
                        Alert.AlertType.ERROR);
            }
        }
    }

    private boolean validarFechas() {
        if (cajaFechaInicio.getValue() == null) {
            mostrarAlerta("Fecha no seleccionada",
                    "Seleccione la fecha de ingreso",
                    Alert.AlertType.WARNING);
            return false;
        }

        if (cajaFechaSalida.getValue() == null) {
            mostrarAlerta("Fecha no seleccionada",
                    "Seleccione la fecha de salida",
                    Alert.AlertType.WARNING);
            return false;
        }

        if (cajaFechaSalida.getValue().isBefore(cajaFechaInicio.getValue()) ||
                cajaFechaSalida.getValue().isEqual(cajaFechaInicio.getValue())) {
            mostrarAlerta("Fechas inválidas",
                    "La fecha de salida debe ser posterior a la fecha de ingreso",
                    Alert.AlertType.WARNING);
            return false;
        }

        return true;
    }

    private void limpiarFormulario() {
        cajaNombre.clear();
        cajaApellidos.clear();
        cajaCorreoE.clear();
        cajaTelefono.clear();
        cajaFechaInicio.setValue(null);
        cajaFechaSalida.setValue(null);
        tablaReservas.getSelectionModel().clearSelection();
        reservaSeleccionada = null;
    }

    private void deshabilitarFormulario() {
        cajaNombre.setDisable(true);
        cajaApellidos.setDisable(true);
        cajaCorreoE.setDisable(true);
        cajaTelefono.setDisable(true);
        cajaFechaInicio.setDisable(true);
        cajaFechaSalida.setDisable(true);
        btnModificarReserva.setDisable(true);
        btnEliminarReserva.setDisable(true);
    }

    private void habilitarFormulario() {
        cajaNombre.setDisable(false);
        cajaApellidos.setDisable(false);
        cajaCorreoE.setDisable(false);
        cajaTelefono.setDisable(false);
        cajaFechaInicio.setDisable(false);
        cajaFechaSalida.setDisable(false);
        btnModificarReserva.setDisable(false);
        btnEliminarReserva.setDisable(false);
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}