package DAO.Interfaces;
import Model.Reserva;
import java.time.LocalDate;
import java.util.List;

public interface IReservaDAO extends IGenericDAO<Reserva> {
    List<Reserva> findByCliente(int idCliente);
    List<Reserva> findByHabitacion(int idHabitacion);
    List<Reserva> findByEstado(String estado);
    List<Reserva> findByFechas(LocalDate fechaInicio, LocalDate fechaFin);
    boolean updateEstado(int idReserva, String estado);
}
