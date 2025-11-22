package DAO.Interfaces;
import Model.Administrador;

public interface IAdministradorDAO extends IGenericDAO<Administrador> {
    Administrador findByIdEmpleado(int idEmpleado);
    Administrador findByCorreo(String correo);
}
