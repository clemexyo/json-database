package app.hyperskill.server.commands;

import app.hyperskill.server.database.IDatabase;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class SetCommand implements ICommand{
    private static final String ERROR = "ERROR";
    private static final String OK = "OK";

    private final IDatabase db;

    public SetCommand(IDatabase db) {
        this.db = db;
    }

    @Override
    public String execute(JsonElement key, JsonElement value) {
        JsonObject response = new JsonObject();
        if (key != null && value != null){
            db.set(key, value);
            response.addProperty("response", OK);
        }
        else{
            response.addProperty("response", ERROR);
        }
        return response.toString();
    }
}
