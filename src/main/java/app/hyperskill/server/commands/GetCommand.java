package app.hyperskill.server.commands;

import app.hyperskill.server.database.IDatabase;

import java.util.Objects;

public class GetCommand implements ICommand {
    private static final String ERROR = "ERROR";
    private final IDatabase db;

    public GetCommand(IDatabase db) {
        this.db = db;
    }

    @Override
    public String execute(String[] args) {
        if (args.length != 1){
            throw new IllegalArgumentException("Invalid arguments for delete command");
        }
        return Objects.requireNonNullElse(db.get(Integer.parseInt(args[0])), ERROR);
    }
}

