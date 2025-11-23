package Controller;

import DAO.Implement.HabitacionDAOImpl;
import Model.Habitacion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class HabitacionesController implements Initializable {

    @FXML private TableView<Habitacion> tablaHabitaciones;
    @FXML private TableColumn<Habitacion, String> columnNumeroHabitacion;
    @FXML private TableColumn<Habitacion, String> columnTipoHabitacion;
    @FXML private TableColumn<Habitacion, String> columnEstadoHabitacion;
    @FXML private TableColumn<Habitacion, Integer> columnCodigoHabitacion;
    @FXML private ComboBox<String> comboBoxEstado;

    private final HabitacionDAOImpl habitacionDAO = new HabitacionDAOImpl();

    private ObservableList<Habitacion> listaHabitaciones;
    private List<Habitacion> todasLasHabitaciones; // Para filtrado

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configurarTabla();
        configurarComboBox();
        cargarHabitaciones();
        configurarEventos();
    }

    private void configurarTabla() {
        columnCodigoHabitacion.setCellValueFactory(new PropertyValueFactory<>("idHabitacion"));
        columnNumeroHabitacion.setCellValueFactory(new PropertyValueFactory<>("numeroHabitacion"));

        columnTipoHabitacion.setCellValueFactory(cellData -> {
            Habitacion hab = cellData.getValue();
            if (hab.getTipoHabitacion() != null) {
                return new javafx.beans.property.SimpleStringProperty(
                        hab.getTipoHabitacion().getDescripcion()
                );
            }
            return new javafx.beans.property.SimpleStringProperty("N/A");
        });

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
                            setStyle("-fx-background-color: #90EE90; -fx-text-fill: #006400; -fx-font-weight: bold; -fx-alignment: CENTER;");
                            break;
                        case "Ocupada":
                            setStyle("-fx-background-color: #FFB6C1; -fx-text-fill: #8B0000; -fx-font-weight: bold; -fx-alignment: CENTER;");
                            break;
                        case "Reservada":
                            setStyle("-fx-background-color: #FFD700; -fx-text-fill: #8B4513; -fx-font-weight: bold; -fx-alignment: CENTER;");
                            break;
                        case "Mantenimiento":
                            setStyle("-fx-background-color: #D3D3D3; -fx-text-fill: #505050; -fx-font-weight: bold; -fx-alignment: CENTER;");
                            break;
                        default:
                            setStyle("-fx-alignment: CENTER;");
                    }
                }
            }
        });
    }

    private void configurarComboBox() {
        ObservableList<String> estados = FXCollections.observableArrayList(
                "Todas",
                "Disponible",
                "Ocupada",
                "Reservada",
                "Mantenimiento"
        );
        comboBoxEstado.setItems(estados);
        comboBoxEstado.setValue("Todas");
    }

    private void cargarHabitaciones() {
        todasLasHabitaciones = habitacionDAO.findAll();
        listaHabitaciones = FXCollections.observableArrayList(todasLasHabitaciones);
        tablaHabitaciones.setItems(listaHabitaciones);

        System.out.println("Habitaciones cargadas: " + todasLasHabitaciones.size());

        mostrarEstadisticas();
    }

    private void configurarEventos() {
        comboBoxEstado.setOnAction(event -> filtrarPorEstado());

        tablaHabitaciones.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                mostrarDetallesHabitacion(newVal);
            }
        });
    }

    @FXML
    private void filtrarPorEstado() {
        String estadoSeleccionado = comboBoxEstado.getValue();

        if (estadoSeleccionado == null || estadoSeleccionado.equals("Todas")) {
            listaHabitaciones.setAll(todasLasHabitaciones);
        } else {
            List<Habitacion> habitacionesFiltradas = habitacionDAO.findByEstado(estadoSeleccionado);
            listaHabitaciones.setAll(habitacionesFiltradas);

            System.out.println("Filtrado: " + habitacionesFiltradas.size() +
                    " habitaciones con estado '" + estadoSeleccionado + "'");
        }
    }

    private void mostrarDetallesHabitacion(Habitacion habitacion) {
        System.out.println("═══════════════════════════════════");
        System.out.println("DETALLES DE LA HABITACIÓN:");
        System.out.println("  ID: " + habitacion.getIdHabitacion());
        System.out.println("  Número: " + habitacion.getNumeroHabitacion());
        System.out.println("  Estado: " + habitacion.getEstado());

        if (habitacion.getTipoHabitacion() != null) {
            System.out.println("  Tipo: " + habitacion.getTipoHabitacion().getDescripcion());
            System.out.println("  Precio: S/ " + habitacion.getTipoHabitacion().getPrecio());
        }
        System.out.println("═══════════════════════════════════");
    }

    private void mostrarEstadisticas() {
        int disponibles = 0;
        int ocupadas = 0;
        int reservadas = 0;
        int mantenimiento = 0;

        for (Habitacion hab : todasLasHabitaciones) {
            switch (hab.getEstado()) {
                case "Disponible":
                    disponibles++;
                    break;
                case "Ocupada":
                    ocupadas++;
                    break;
                case "Reservada":
                    reservadas++;
                    break;
                case "Mantenimiento":
                    mantenimiento++;
                    break;
            }
        }

        System.out.println("═══════════════════════════════════");
        System.out.println("ESTADÍSTICAS DE HABITACIONES:");
        System.out.println("  Total: " + todasLasHabitaciones.size());
        System.out.println("  Disponibles: " + disponibles);
        System.out.println("  Ocupadas: " + ocupadas);
        System.out.println("  Reservadas: " + reservadas);
        System.out.println("  Mantenimiento: " + mantenimiento);
        System.out.println("═══════════════════════════════════");
    }

    public void recargarHabitaciones() {
        cargarHabitaciones();
        filtrarPorEstado(); // Aplicar filtro actual
    }
}