package Model;

public class Administrador extends Empleado {
    private int idAdministrador;
    private int idEmpleado;
    private String correoElectronico;

    private Empleado empleado;

    public Administrador() {}

    public Administrador(int idAdministrador, int idEmpleado, String correoElectronico) {
        this.idAdministrador = idAdministrador;
        this.idEmpleado = idEmpleado;
        this.correoElectronico = correoElectronico;
    }

    public int getIdAdministrador() { return idAdministrador; }
    public void setIdAdministrador(int idAdministrador) { this.idAdministrador = idAdministrador; }
    public int getIdEmpleado() { return idEmpleado; }
    public void setIdEmpleado(int idEmpleado) { this.idEmpleado = idEmpleado; }
    public String getCorreoElectronico() { return correoElectronico; }
    public void setCorreoElectronico(String correoElectronico) { this.correoElectronico = correoElectronico; }
    public Empleado getEmpleado() { return empleado; }
    public void setEmpleado(Empleado empleado) { this.empleado = empleado; }
}
