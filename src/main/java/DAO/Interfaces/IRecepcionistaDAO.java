package DAO.Interfaces;
import Model.Recepcionista;
import java.util.List;

public interface IRecepcionistaDAO extends IGenericDAO<Recepcionista> {
    Recepcionista findByIdEmpleado(int idEmpleado);
    List<Recepcionista> findByTurno(String turno);
}
