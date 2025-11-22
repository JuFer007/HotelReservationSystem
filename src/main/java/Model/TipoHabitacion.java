package Model;

public class TipoHabitacion {
    private int idTipoHabitacion;
    private String descripcion;
    private double precio;

    public TipoHabitacion() {}

    public TipoHabitacion(int idTipoHabitacion, String descripcion, double precio) {
        this.idTipoHabitacion = idTipoHabitacion;
        this.descripcion = descripcion;
        this.precio = precio;
    }

    public int getIdTipoHabitacion() { return idTipoHabitacion; }
    public void setIdTipoHabitacion(int idTipoHabitacion) { this.idTipoHabitacion = idTipoHabitacion; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    @Override
    public String toString() { return descripcion + " - S/" + precio; }
}
