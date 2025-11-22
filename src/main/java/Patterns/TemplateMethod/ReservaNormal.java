package Patterns.TemplateMethod;
import Model.Reserva;
import Model.TipoHabitacion;
import java.time.temporal.ChronoUnit;

public class ReservaNormal extends ProcesoReserva {
    @Override
    protected String getTipoReserva() {
        return "RESERVA NORMAL";
    }

    @Override
    protected double calcularPrecioFinal(Reserva reserva) {
        TipoHabitacion tipo = habitacionDAO.read(reserva.getIdHabitacion()).getTipoHabitacion();
        long dias = ChronoUnit.DAYS.between(reserva.getFechaIngreso(), reserva.getFechaSalida());
        if (dias <= 0) dias = 1;
        return tipo.getPrecio() * dias;
    }
}
