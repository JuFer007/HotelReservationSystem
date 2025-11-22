package Model;

public class Recepcionista extends Empleado {
    private int idRecepcionista;
    private int idEmpleado;
    private String turnoTrabajo;

    private Empleado empleado;

    public Recepcionista() {}

    public Recepcionista(int idRecepcionista, int idEmpleado, String turnoTrabajo) {
        this.idRecepcionista = idRecepcionista;
        this.idEmpleado = idEmpleado;
        this.turnoTrabajo = turnoTrabajo;
    }

    public int getIdRecepcionista() { return idRecepcionista; }
    public void setIdRecepcionista(int idRecepcionista) { this.idRecepcionista = idRecepcionista; }
    public int getIdEmpleado() { return idEmpleado; }
    public void setIdEmpleado(int idEmpleado) { this.idEmpleado = idEmpleado; }
    public String getTurnoTrabajo() { return turnoTrabajo; }
    public void setTurnoTrabajo(String turnoTrabajo) { this.turnoTrabajo = turnoTrabajo; }
    public Empleado getEmpleado() { return empleado; }
    public void setEmpleado(Empleado empleado) { this.empleado = empleado; }
}
