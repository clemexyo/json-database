package app.hyperskill.server.commands;

import app.hyperskill.server.database.IDatabase;
import com.google.gson.JsonObject;

public class DeleteCommand implements ICommand {
    private static final String ERROR = "ERROR";
    private static final String NO_SUCH_KEY = "No such key";
    private static final String OK = "OK";
    private final IDatabase db;

    public DeleteCommand(IDatabase db) {
        this.db = db;
    }

    @Override
    public String execute(String key, String value) {
        if (key != null && value == null){
            JsonObject response = new JsonObject();
            if(db.delete(key)){
                response.addProperty("response", OK);
            }
            else{
                response.addProperty("response", ERROR);
                response.addProperty("reason", NO_SUCH_KEY);
            }
            return response.toString();
        }
        throw new IllegalArgumentException("Invalid arguments for delete command");
    }
}