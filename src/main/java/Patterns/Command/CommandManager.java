package Patterns.Command;
import java.util.Stack;

public class CommandManager {
    private final Stack<IReservaCommand> history = new Stack<>();

    public boolean executeCommand(IReservaCommand command) {
        boolean result = command.execute();
        if (result) {
            history.push(command);
        }
        return result;
    }

    public boolean undoLast() {
        if (!history.isEmpty()) {
            IReservaCommand last = history.pop();
            return last.undo();
        }
        return false;
    }
}
