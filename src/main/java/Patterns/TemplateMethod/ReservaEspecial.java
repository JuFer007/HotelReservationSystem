package Patterns.TemplateMethod;
import Model.Reserva;
import Model.TipoHabitacion;
import java.time.temporal.ChronoUnit;

public class ReservaEspecial extends ProcesoReserva {
    private double descuento = 0.15;

    public ReservaEspecial(double descuento) {
        this.descuento = descuento;
    }

    @Override
    protected String getTipoReserva() {
        return "RESERVA ESPECIAL (CON DESCUENTO)";
    }

    @Override
    protected double calcularPrecioFinal(Reserva reserva) {
        TipoHabitacion tipo = habitacionDAO.read(reserva.getIdHabitacion()).getTipoHabitacion();
        long dias = ChronoUnit.DAYS.between(reserva.getFechaIngreso(), reserva.getFechaSalida());
        if (dias <= 0) dias = 1;

        double precioTotal = tipo.getPrecio() * dias;
        return precioTotal * (1 - descuento);
    }

    @Override
    protected void notificar(Reserva reserva) {
        super.notificar(reserva);
        System.out.println("Código de descuento aplicado");
    }
}
