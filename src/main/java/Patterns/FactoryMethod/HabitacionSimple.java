package Patterns.FactoryMethod;

public class HabitacionSimple implements IHabitacionTipo {
    @Override
    public String getTipo() {
        return "Simple";
    }

    @Override
    public double getPrecio() {
        return 50.0;
    }

    @Override
    public String getDescripcion() {
        return "Habitación simple con una cama.";
    }
}
