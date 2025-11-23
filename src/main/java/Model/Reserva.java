package Model;
import java.time.LocalDate;

public class Reserva {
    private int idReserva;
    private int idCliente;
    private int idEmpleado;
    private int idHabitacion;
    private LocalDate fechaReserva;
    private LocalDate fechaIngreso;
    private LocalDate fechaSalida;
    private String estadoReserva;
    private Cliente cliente;
    private Empleado empleado;
    private Habitacion habitacion;

    public Reserva() {}

    public Reserva(int idReserva, int idCliente, int idEmpleado, int idHabitacion,
                   LocalDate fechaReserva, LocalDate fechaIngreso, LocalDate fechaSalida,
                   String estadoReserva) {
        this.idReserva = idReserva;
        this.idCliente = idCliente;
        this.idEmpleado = idEmpleado;
        this.idHabitacion = idHabitacion;
        this.fechaReserva = fechaReserva;
        this.fechaIngreso = fechaIngreso;
        this.fechaSalida = fechaSalida;
        this.estadoReserva = estadoReserva;
    }

    public int getIdReserva() { return idReserva; }
    public void setIdReserva(int idReserva) { this.idReserva = idReserva; }
    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }
    public int getIdEmpleado() { return idEmpleado; }
    public void setIdEmpleado(int idEmpleado) { this.idEmpleado = idEmpleado; }
    public int getIdHabitacion() { return idHabitacion; }
    public void setIdHabitacion(int idHabitacion) { this.idHabitacion = idHabitacion; }
    public LocalDate getFechaReserva() { return fechaReserva; }
    public void setFechaReserva(LocalDate fechaReserva) { this.fechaReserva = fechaReserva; }
    public LocalDate getFechaIngreso() { return fechaIngreso; }
    public void setFechaIngreso(LocalDate fechaIngreso) { this.fechaIngreso = fechaIngreso; }
    public LocalDate getFechaSalida() { return fechaSalida; }
    public void setFechaSalida(LocalDate fechaSalida) { this.fechaSalida = fechaSalida; }
    public String getEstadoReserva() { return estadoReserva; }
    public void setEstadoReserva(String estadoReserva) { this.estadoReserva = estadoReserva; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public Empleado getEmpleado() { return empleado; }
    public void setEmpleado(Empleado empleado) { this.empleado = empleado; }
    public Habitacion getHabitacion() { return habitacion; }
    public void setHabitacion(Habitacion habitacion) { this.habitacion = habitacion; }
}
