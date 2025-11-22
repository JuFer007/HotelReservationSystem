package Patterns.Command;

public interface IReservaCommand {
    boolean execute();
    boolean undo();
}
