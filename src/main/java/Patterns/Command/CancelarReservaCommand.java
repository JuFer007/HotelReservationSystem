package Patterns.Command;
import DAO.Implement.ReservaDAOImpl;
import DAO.Interfaces.IReservaDAO;
import Model.Reserva;

public class CancelarReservaCommand implements IReservaCommand{
    private final IReservaDAO reservaDAO;
    private final int idReserva;
    private String estadoAnterior;

    public CancelarReservaCommand(int idReserva) {
        this.reservaDAO = new ReservaDAOImpl();
        this.idReserva = idReserva;
    }

    @Override
    public boolean execute() {
        Reserva reserva = reservaDAO.read(idReserva);
        if (reserva == null) return false;

        estadoAnterior = reserva.getEstadoReserva();
        reserva.setEstadoReserva("CANCELADA");
        return reservaDAO.update(reserva);
    }

    @Override
    public boolean undo() {
        Reserva reserva = reservaDAO.read(idReserva);
        if (reserva == null) return false;

        reserva.setEstadoReserva(estadoAnterior);
        return reservaDAO.update(reserva);
    }
}
