package Patterns.Command;

import DAO.Implement.ReservaDAOImpl;
import DAO.Interfaces.IReservaDAO;
import Model.Reserva;

import java.time.LocalDate;

public class ModificarReservaCommand implements IReservaCommand {
    private final IReservaDAO reservaDAO;
    private final int idReserva;
    private final LocalDate nuevaFechaIngreso;
    private final LocalDate nuevaFechaSalida;
    private LocalDate fechaIngresoAnterior;
    private LocalDate fechaSalidaAnterior;

    public ModificarReservaCommand(int idReserva, LocalDate nuevaFechaIngreso, LocalDate nuevaFechaSalida) {
        this.reservaDAO = new ReservaDAOImpl();
        this.idReserva = idReserva;
        this.nuevaFechaIngreso = nuevaFechaIngreso;
        this.nuevaFechaSalida = nuevaFechaSalida;
    }

    @Override
    public boolean execute() {
        Reserva reserva = reservaDAO.read(idReserva);
        if (reserva == null) return false;

        fechaIngresoAnterior = reserva.getFechaIngreso();
        fechaSalidaAnterior = reserva.getFechaSalida();

        reserva.setFechaIngreso(nuevaFechaIngreso);
        reserva.setFechaSalida(nuevaFechaSalida);

        return reservaDAO.update(reserva);
    }

    @Override
    public boolean undo() {
        Reserva reserva = reservaDAO.read(idReserva);
        if (reserva == null) return false;

        reserva.setFechaIngreso(fechaIngresoAnterior);
        reserva.setFechaSalida(fechaSalidaAnterior);

        return reservaDAO.update(reserva);
    }
}
