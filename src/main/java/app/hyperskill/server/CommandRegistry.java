package app.hyperskill.server;

import app.hyperskill.server.commands.*;
import app.hyperskill.server.database.IDatabase;

import java.util.HashMap;
import java.util.Map;

public class CommandRegistry {
    private final Map<String, ICommand> commands = new HashMap<>();

    public CommandRegistry(IDatabase db) {
        register("get", new GetCommand(db));
        register("set", new SetCommand(db));
        register("delete", new DeleteCommand(db));
    }

    public ICommand getCommand(String name) {
        return commands.getOrDefault(name, null);
    }

    public boolean hasCommand(String name){
        return commands.containsKey(name);
    }

    private void register(String name, ICommand command) {
        commands.put(name, command);
    }
}



