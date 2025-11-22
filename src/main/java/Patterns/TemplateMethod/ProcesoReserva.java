package Patterns.TemplateMethod;
import DAO.Implement.ClienteDAOImpl;
import DAO.Implement.HabitacionDAOImpl;
import DAO.Implement.ReservaDAOImpl;
import DAO.Interfaces.IClienteDAO;
import DAO.Interfaces.IHabitacionDAO;
import DAO.Interfaces.IReservaDAO;
import Model.Habitacion;
import Model.Reserva;

public abstract class ProcesoReserva {
    protected IReservaDAO reservaDAO = new ReservaDAOImpl();
    protected IHabitacionDAO habitacionDAO = new HabitacionDAOImpl();
    protected IClienteDAO clienteDAO = new ClienteDAOImpl();

    // TEMPLATE METHOD
    public final boolean procesarReserva(Reserva reserva) {
        System.out.println("\n=== " + getTipoReserva() + " ===");

        if (!validarCliente(reserva)) {
            System.err.println("Cliente inválido");
            return false;
        }

        if (!verificarDisponibilidad(reserva)) {
            System.err.println("Habitación no disponible");
            return false;
        }

        if (!aplicarReglasEspecificas(reserva)) {
            System.err.println("No cumple reglas específicas");
            return false;
        }

        double precio = calcularPrecioFinal(reserva);
        System.out.println("Precio: S/ " + precio);

        if (!registrarReserva(reserva)) {
            System.err.println("Error al registrar");
            return false;
        }

        actualizarEstadoHabitacion(reserva);
        notificar(reserva);

        System.out.println("✓ Proceso completado\n");
        return true;
    }

    // Métodos concretos
    protected boolean validarCliente(Reserva reserva) {
        return clienteDAO.read(reserva.getIdCliente()) != null;
    }

    protected boolean verificarDisponibilidad(Reserva reserva) {
        Habitacion hab = habitacionDAO.read(reserva.getIdHabitacion());
        return hab != null && "DISPONIBLE".equals(hab.getEstado());
    }

    protected boolean registrarReserva(Reserva reserva) {
        return reservaDAO.create(reserva);
    }

    protected void actualizarEstadoHabitacion(Reserva reserva) {
        Habitacion hab = habitacionDAO.read(reserva.getIdHabitacion());
        if (hab != null) {
            hab.setEstado("OCUPADA");
            habitacionDAO.update(hab);
        }
    }

    // Métodos abstractos
    protected abstract String getTipoReserva();
    protected abstract double calcularPrecioFinal(Reserva reserva);

    // Hook methods
    protected boolean aplicarReglasEspecificas(Reserva reserva) {
        return true;
    }

    protected void notificar(Reserva reserva) {
        System.out.println("Notificación enviada");
    }
}
