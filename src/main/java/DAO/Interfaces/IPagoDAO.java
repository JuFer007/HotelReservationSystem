package DAO.Interfaces;
import Model.Pago;
import java.time.LocalDate;
import java.util.List;

public interface IPagoDAO extends IGenericDAO<Pago> {
    Pago findByReserva(int idReserva);
    List<Pago> findByFechas(LocalDate fechaInicio, LocalDate fechaFin);
    List<Pago> findByMetodoPago(String metodoPago);
    double getTotalPagosByFechas(LocalDate fechaInicio, LocalDate fechaFin);
}
