package DAO.Interfaces;
import Model.TipoHabitacion;

public interface ITipoHabitacionDAO extends IGenericDAO<TipoHabitacion> {
    TipoHabitacion findByDescripcion(String descripcion);
}
