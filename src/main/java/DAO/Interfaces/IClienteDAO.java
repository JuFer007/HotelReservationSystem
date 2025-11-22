package DAO.Interfaces;
import Model.Cliente;
import java.util.List;

public interface IClienteDAO extends IGenericDAO<Cliente> {
    Cliente findByDni(String dni);
    List<Cliente> findByNombre(String nombre);
}
