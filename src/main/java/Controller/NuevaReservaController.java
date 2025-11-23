package Controller;
import DAO.Implement.ClienteDAOImpl;
import DAO.Implement.HabitacionDAOImpl;
import DAO.Implement.ReservaDAOImpl;
import Model.Cliente;
import Model.Habitacion;
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
import java.util.ResourceBundle;

public class NuevaReservaController implements Initializable {

    @FXML private TextField nombreNuevaReserva;
    @FXML private TextField apellidosNuevaReserva;
    @FXML private TextField correoNuevaReserva;
    @FXML private TextField cajaTelefNuevaReserva;
    @FXML private TextField DniNuevaReserva;
    @FXML private DatePicker fechaInicioNuevaReserva;
    @FXML private DatePicker fechaSalidaNuevaReserva;
    @FXML private Button btnAgregarReserva;

    @FXML private ListView<Cliente> listaClientes;

    @FXML private TableView<Habitacion> tablaHabitaciones;
    @FXML private TableColumn<Habitacion, Integer> columnCodigoHabitacion;
    @FXML private TableColumn<Habitacion, String> columnTipoHabitacion;
    @FXML private TableColumn<Habitacion, String> columnNumHabitacion;
    @FXML private TableColumn<Habitacion, String> columnEstadoHabitacion;

    private final HabitacionDAOImpl habitacionDAO = new HabitacionDAOImpl();
    private final ClienteDAOImpl clienteDAO = new ClienteDAOImpl();
    private final ReservaDAOImpl reservaDAO = new ReservaDAOImpl();

    private ObservableList<Habitacion> listaHabitaciones;
    private ObservableList<Cliente> listaClientesObs;

    private Cliente clienteActual = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configurarTablaHabitaciones();
        configurarListaClientes();
        cargarDatos();
        configurarEventos();
    }

    private void configurarTablaHabitaciones() {
        columnCodigoHabitacion.setCellValueFactory(new PropertyValueFactory<>("idHabitacion"));

        columnTipoHabitacion.setCellValueFactory(cellData -> {
            if (cellData.getValue().getTipoHabitacion() != null) {
                return new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getTipoHabitacion().getDescripcion()
                );
            }
            return new javafx.beans.property.SimpleStringProperty("N/A");
        });

        columnNumHabitacion.setCellValueFactory(new PropertyValueFactory<>("numeroHabitacion"));
        columnEstadoHabitacion.setCellValueFactory(new PropertyValueFactory<>("estado"));

        columnEstadoHabitacion.setCellFactory(column -> new TableCell<Habitacion, String>() {
            @Override
            protected void updateItem(String estado, boolean empty) {
                super.updateItem(estado, empty);
                if (empty || estado == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(estado);
                    switch (estado) {
                        case "Disponible":
                            setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                            break;
                        case "Ocupada":
                            setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                            break;
                        case "Reservada":
                            setStyle("-fx-text-fill: orange; -fx-font-weight: bold;");
                            break;
                        case "Mantenimiento":
                            setStyle("-fx-text-fill: gray; -fx-font-weight: bold;");
                            break;
                        default:
                            setStyle("");
                    }
                }
            }
        });
    }

    private void configurarListaClientes() {
        listaClientes.setCellFactory(param -> new ListCell<Cliente>() {
            @Override
            protected void updateItem(Cliente cliente, boolean empty) {
                super.updateItem(cliente, empty);
                if (empty || cliente == null) {
                    setText(null);
                } else {
                    setText(cliente.getNombreCompleto() + " - DNI: " + cliente.getDni());
                }
            }
        });
    }

    private void cargarDatos() {
        cargarHabitacionesDisponibles();
        cargarClientes();
    }

    private void cargarHabitacionesDisponibles() {
        List<Habitacion> habitaciones = habitacionDAO.findDisponibles();
        listaHabitaciones = FXCollections.observableArrayList(habitaciones);
        tablaHabitaciones.setItems(listaHabitaciones);

        System.out.println("Habitaciones disponibles cargadas: " + habitaciones.size());
    }

    private void cargarClientes() {
        List<Cliente> clientes = clienteDAO.findAll();
        listaClientesObs = FXCollections.observableArrayList(clientes);
        listaClientes.setItems(listaClientesObs);

        System.out.println("Clientes cargados: " + clientes.size());
    }

    private void configurarEventos() {
        listaClientes.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                llenarFormularioConCliente(newVal);
                clienteActual = newVal;
            }
        });

        nombreNuevaReserva.textProperty().addListener((obs, oldVal, newVal) -> {
            if (clienteActual != null && !clienteActual.getNombre().equals(newVal)) {
                listaClientes.getSelectionModel().clearSelection();
                clienteActual = null;
            }
        });

        btnAgregarReserva.setOnAction(event -> agregarReserva());
    }

    private void llenarFormularioConCliente(Cliente cliente) {
        nombreNuevaReserva.setText(cliente.getNombre());
        String apellidos = cliente.getApellidoPaterno();
        if (cliente.getApellidoMaterno() != null && !cliente.getApellidoMaterno().isEmpty()) {
            apellidos += " " + cliente.getApellidoMaterno();
        }
        apellidosNuevaReserva.setText(apellidos);
        DniNuevaReserva.setText(cliente.getDni());
        correoNuevaReserva.setText(cliente.getEmail() != null ? cliente.getEmail() : "");
        cajaTelefNuevaReserva.setText(cliente.getTelefono());
    }

    @FXML
    private void agregarReserva() {
        if (!validarCampos()) {
            return;
        }

        Habitacion habitacionSeleccionada = tablaHabitaciones.getSelectionModel().getSelectedItem();
        if (habitacionSeleccionada == null) {
            mostrarAlerta("Habitación no seleccionada",
                    "Por favor seleccione una habitación de la tabla.",
                    Alert.AlertType.WARNING);
            return;
        }

        Cliente cliente = obtenerOCrearCliente();
        if (cliente == null || cliente.getIdCliente() == 0) {
            mostrarAlerta("Error",
                    "No se pudo obtener o crear el cliente",
                    Alert.AlertType.ERROR);
            return;
        }

        boolean reservaCreada = crearReserva(cliente, habitacionSeleccionada);

        if (reservaCreada) {
            mostrarAlerta("Éxito",
                    "Reserva creada exitosamente para " + cliente.getNombreCompleto(),
                    Alert.AlertType.INFORMATION);
            limpiarFormulario();
            cargarHabitacionesDisponibles(); // Recargar tabla
        } else {
            mostrarAlerta("Error",
                    "No se pudo crear la reserva. Verifique la consola para más detalles.",
                    Alert.AlertType.ERROR);
        }
    }

    private boolean validarCampos() {
        if (nombreNuevaReserva.getText().trim().isEmpty()) {
            mostrarAlerta("Campo vacío", "Ingrese el nombre del cliente", Alert.AlertType.WARNING);
            nombreNuevaReserva.requestFocus();
            return false;
        }

        if (apellidosNuevaReserva.getText().trim().isEmpty()) {
            mostrarAlerta("Campo vacío", "Ingrese los apellidos del cliente", Alert.AlertType.WARNING);
            apellidosNuevaReserva.requestFocus();
            return false;
        }

        if (cajaTelefNuevaReserva.getText().trim().isEmpty()) {
            mostrarAlerta("Campo vacío", "Ingrese el teléfono del cliente", Alert.AlertType.WARNING);
            cajaTelefNuevaReserva.requestFocus();
            return false;
        }

        if (fechaInicioNuevaReserva.getValue() == null) {
            mostrarAlerta("Fecha no seleccionada", "Seleccione la fecha de ingreso", Alert.AlertType.WARNING);
            fechaInicioNuevaReserva.requestFocus();
            return false;
        }

        if (fechaSalidaNuevaReserva.getValue() == null) {
            mostrarAlerta("Fecha no seleccionada", "Seleccione la fecha de salida", Alert.AlertType.WARNING);
            fechaSalidaNuevaReserva.requestFocus();
            return false;
        }

        if (fechaSalidaNuevaReserva.getValue().isBefore(fechaInicioNuevaReserva.getValue()) ||
                fechaSalidaNuevaReserva.getValue().isEqual(fechaInicioNuevaReserva.getValue())) {
            mostrarAlerta("Fechas inválidas",
                    "La fecha de salida debe ser posterior a la fecha de ingreso",
                    Alert.AlertType.WARNING);
            return false;
        }

        if (fechaInicioNuevaReserva.getValue().isBefore(LocalDate.now())) {
            mostrarAlerta("Fecha inválida",
                    "La fecha de ingreso no puede ser en el pasado",
                    Alert.AlertType.WARNING);
            return false;
        }

        if (DniNuevaReserva.getText().trim().isEmpty()) {
            mostrarAlerta("Campo vacío", "Ingrese el DNI del cliente", Alert.AlertType.WARNING);
            DniNuevaReserva.requestFocus();
            return false;
        }

        return true;
    }

    private Cliente obtenerOCrearCliente() {
        if (clienteActual != null) {
            System.out.println("Usando cliente existente: " + clienteActual.getNombreCompleto());
            return clienteActual;
        }

        String dniIngresado = DniNuevaReserva.getText().trim();

        if (!dniIngresado.isEmpty()) {
            Cliente clienteExistente = clienteDAO.findByDni(dniIngresado);
            if (clienteExistente != null) {
                System.out.println("✓ Cliente existente encontrado con DNI: " + dniIngresado);
                System.out.println("  ID: " + clienteExistente.getIdCliente());
                System.out.println("  Nombre: " + clienteExistente.getNombreCompleto());
                return clienteExistente;
            }
        }

        Cliente nuevoCliente = new Cliente();
        nuevoCliente.setNombre(nombreNuevaReserva.getText().trim());

        String[] apellidos = apellidosNuevaReserva.getText().trim().split("\\s+", 2);
        nuevoCliente.setApellidoPaterno(apellidos[0]);
        nuevoCliente.setApellidoMaterno(apellidos.length > 1 ? apellidos[1] : "");

        String dniAUsar;
        if (!dniIngresado.isEmpty()) {
            dniAUsar = dniIngresado;
        } else {
            String timestamp = String.valueOf(System.currentTimeMillis());
            dniAUsar = "TMP" + timestamp.substring(timestamp.length() - 10);
        }
        nuevoCliente.setDni(dniAUsar);

        nuevoCliente.setTelefono(cajaTelefNuevaReserva.getText().trim());

        String email = correoNuevaReserva.getText().trim();
        nuevoCliente.setEmail(email.isEmpty() ? "sin_email@temp.com" : email);

        System.out.println("═══════════════════════════════════");
        System.out.println("Creando nuevo cliente:");
        System.out.println("  Nombre: " + nuevoCliente.getNombre());
        System.out.println("  Apellido P: " + nuevoCliente.getApellidoPaterno());
        System.out.println("  Apellido M: " + nuevoCliente.getApellidoMaterno());
        System.out.println("  DNI: " + nuevoCliente.getDni());
        System.out.println("  Teléfono: " + nuevoCliente.getTelefono());
        System.out.println("  Email: " + nuevoCliente.getEmail());

        try {
            boolean clienteCreado = clienteDAO.create(nuevoCliente);
            System.out.println("Resultado create(): " + clienteCreado);

            if (clienteCreado) {
                Thread.sleep(100);

                Cliente clienteEncontrado = clienteDAO.findByDni(dniAUsar);

                if (clienteEncontrado != null) {
                    System.out.println("✓ Cliente creado exitosamente con ID: " + clienteEncontrado.getIdCliente());
                    System.out.println("═══════════════════════════════════");
                    cargarClientes();
                    return clienteEncontrado;
                } else {
                    System.err.println("✗ Error: Cliente creado pero no encontrado al buscar por DNI: " + dniAUsar);

                    List<Cliente> todosClientes = clienteDAO.findAll();
                    System.out.println("Buscando en " + todosClientes.size() + " clientes...");

                    for (Cliente c : todosClientes) {
                        if (c.getDni().equals(dniAUsar)) {
                            System.out.println("✓ Cliente encontrado en la lista completa con ID: " + c.getIdCliente());
                            System.out.println("═══════════════════════════════════");
                            return c;
                        }
                    }
                }
            } else {
                System.err.println("✗ Error: clienteDAO.create() retornó false");
            }
        } catch (Exception e) {
            System.err.println("✗ Excepción al crear cliente: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("═══════════════════════════════════");
        mostrarAlerta("Error",
                "No se pudo crear el cliente. Verifique la consola para más detalles.",
                Alert.AlertType.ERROR);
        return null;
    }

    private boolean crearReserva(Cliente cliente, Habitacion habitacion) {
        try {
            Reserva reserva = new Reserva();
            reserva.setIdCliente(cliente.getIdCliente());
            reserva.setIdEmpleado(1);
            reserva.setIdHabitacion(habitacion.getIdHabitacion());
            reserva.setFechaReserva(LocalDate.now());
            reserva.setFechaIngreso(fechaInicioNuevaReserva.getValue());
            reserva.setFechaSalida(fechaSalidaNuevaReserva.getValue());
            reserva.setEstadoReserva("Confirmada");

            System.out.println("Creando reserva:");
            System.out.println("  - Cliente ID: " + cliente.getIdCliente());
            System.out.println("  - Habitación ID: " + habitacion.getIdHabitacion());
            System.out.println("  - Fecha Ingreso: " + reserva.getFechaIngreso());
            System.out.println("  - Fecha Salida: " + reserva.getFechaSalida());

            boolean reservaCreada = reservaDAO.create(reserva);

            if (reservaCreada) {
                System.out.println("✓ Reserva creada exitosamente");

                boolean estadoActualizado = habitacionDAO.updateEstado(
                        habitacion.getIdHabitacion(),
                        "Reservada"
                );

                if (estadoActualizado) {
                    System.out.println("✓ Estado de habitación actualizado a 'Reservada'");
                } else {
                    System.err.println("⚠ Advertencia: No se pudo actualizar el estado de la habitación");
                }

                return true;
            } else {
                System.err.println("✗ Error al crear la reserva");
            }

        } catch (Exception e) {
            System.err.println("Error al crear reserva: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    private void limpiarFormulario() {
        nombreNuevaReserva.clear();
        apellidosNuevaReserva.clear();
        DniNuevaReserva.clear();
        correoNuevaReserva.clear();
        cajaTelefNuevaReserva.clear();
        fechaInicioNuevaReserva.setValue(null);
        fechaSalidaNuevaReserva.setValue(null);
        tablaHabitaciones.getSelectionModel().clearSelection();
        listaClientes.getSelectionModel().clearSelection();
        clienteActual = null;
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}