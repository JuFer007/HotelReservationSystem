package Patterns.FactoryMethod;

public class HabitacionFactory {
    public static IHabitacionTipo getHabitacion(String tipo) {
        if (tipo.equalsIgnoreCase("simple")) {
            return new HabitacionSimple();
        } else if (tipo.equalsIgnoreCase("doble")) {
            return new HabitacionDoble();
        }
        return null;
    }
}
