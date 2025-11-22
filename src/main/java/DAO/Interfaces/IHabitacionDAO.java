package DAO.Interfaces;
import Model.Habitacion;
import java.util.List;

public interface IHabitacionDAO extends IGenericDAO<Habitacion> {
    Habitacion findByNumero(String numeroHabitacion);
    List<Habitacion> findByEstado(String estado);
    List<Habitacion> findByTipo(int idTipoHabitacion);
    List<Habitacion> findDisponibles();
    boolean updateEstado(int idHabitacion, String estado);
}
