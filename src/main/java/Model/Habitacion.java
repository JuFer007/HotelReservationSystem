package Model;

public class Habitacion {
    private int idHabitacion;
    private int idTipoHabitacion;
    private String numeroHabitacion;
    private String estado;

    private TipoHabitacion tipoHabitacion;

    public Habitacion() {}

    public Habitacion(int idHabitacion, int idTipoHabitacion, String numeroHabitacion, String estado) {
        this.idHabitacion = idHabitacion;
        this.idTipoHabitacion = idTipoHabitacion;
        this.numeroHabitacion = numeroHabitacion;
        this.estado = estado;
    }

    public int getIdHabitacion() { return idHabitacion; }
    public void setIdHabitacion(int idHabitacion) { this.idHabitacion = idHabitacion; }
    public int getIdTipoHabitacion() { return idTipoHabitacion; }
    public void setIdTipoHabitacion(int idTipoHabitacion) { this.idTipoHabitacion = idTipoHabitacion; }
    public String getNumeroHabitacion() { return numeroHabitacion; }
    public void setNumeroHabitacion(String numeroHabitacion) { this.numeroHabitacion = numeroHabitacion; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public TipoHabitacion getTipoHabitacion() { return tipoHabitacion; }
    public void setTipoHabitacion(TipoHabitacion tipoHabitacion) { this.tipoHabitacion = tipoHabitacion; }

    @Override
    public String toString() {
        return "Hab. " + numeroHabitacion + " - " + (tipoHabitacion != null ? tipoHabitacion.getDescripcion() : "");
    }
}
