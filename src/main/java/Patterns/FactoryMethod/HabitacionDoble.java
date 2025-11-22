package Patterns.FactoryMethod;

public class HabitacionDoble implements IHabitacionTipo {
    @Override
    public String getTipo() {
        return "Doble";
    }

    @Override
    public double getPrecio() {
        return 90.0;
    }

    @Override
    public String getDescripcion() {
        return "Habitación doble con dos camas.";
    }
}
