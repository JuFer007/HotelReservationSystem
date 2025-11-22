package DAO.Interfaces;
import Model.Empleado;

public interface IEmpleadoDAO extends IGenericDAO<Empleado> {
    Empleado findByDni(String dni);
}
