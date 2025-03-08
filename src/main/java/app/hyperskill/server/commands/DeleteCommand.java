package app.hyperskill.server.commands;

import app.hyperskill.server.database.IDatabase;

public class DeleteCommand implements ICommand {
    private static final String ERROR = "ERROR";
    private static final String OK = "OK";
    private final IDatabase db;

    public DeleteCommand(IDatabase db) {
        this.db = db;
    }

    @Override
    public String execute(String[] args) {
        if (args.length != 1){
            throw new IllegalArgumentException("Invalid arguments for delete command");
        }
        return db.delete(Integer.parseInt(args[0])) ? OK : ERROR;
    }
}