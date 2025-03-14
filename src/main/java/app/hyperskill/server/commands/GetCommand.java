package app.hyperskill.server.commands;


import app.hyperskill.server.database.IDatabase;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Objects;

public class GetCommand implements ICommand {
    private static final String ERROR = "ERROR";
    private static final String NO_SUCH_KEY = "No such key";
    private static final String OK = "OK";
    private final IDatabase db;

    public GetCommand(IDatabase db) {
        this.db = db;
    }

    @Override
    public String execute(String key, String value) {
        if (key != null && value == null){
            JsonElement dbValue = db.get(key);
            JsonObject response = new JsonObject();
            if(dbValue != null){
                response.addProperty("response", OK);
                response.addProperty("value", dbValue.getAsString());
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

