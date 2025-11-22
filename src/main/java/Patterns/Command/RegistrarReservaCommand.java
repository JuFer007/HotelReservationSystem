package Patterns.Command;
import DAO.Implement.ReservaDAOImpl;
import DAO.Interfaces.IReservaDAO;
import Model.Reserva;

public class RegistrarReservaCommand implements IReservaCommand {
    private final IReservaDAO reservaDAO;
    private final Reserva reserva;
    private int idReservaCreada;

    public RegistrarReservaCommand(Reserva reserva) {
        this.reservaDAO = new ReservaDAOImpl();
        this.reserva = reserva;
    }

    @Override
    public boolean execute() {
        boolean resultado = reservaDAO.create(reserva);
        if (resultado) {
            idReservaCreada = reserva.getIdReserva();
        }
        return resultado;
    }

    @Override
    public boolean undo() {
        if (idReservaCreada > 0) {
            return reservaDAO.delete(idReservaCreada);
        }
        return false;
    }
}
