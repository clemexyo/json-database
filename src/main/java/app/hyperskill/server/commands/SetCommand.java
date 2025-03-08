package app.hyperskill.server.commands;

import app.hyperskill.server.database.IDatabase;

public class SetCommand implements ICommand{
    private static final String ERROR = "ERROR";
    private static final String OK = "OK";

    private final IDatabase db;

    public SetCommand(IDatabase db) {
        this.db = db;
    }

    @Override
    public String execute(String[] args) {
        if (args.length != 2){
            throw new IllegalArgumentException("Invalid arguments for delete command");
        }
        return db.set(Integer.parseInt(args[0]), args[1]) ? OK : ERROR;
    }
}
