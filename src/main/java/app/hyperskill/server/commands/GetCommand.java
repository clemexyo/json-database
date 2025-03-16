package app.hyperskill.server.commands;


import app.hyperskill.server.database.IDatabase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Objects;

public class GetCommand implements ICommand {
    private static final String ERROR = "ERROR";
    private static final String NO_SUCH_KEY = "No such key";
    private static final String OK = "OK";
    private final IDatabase db;
    private final Gson gson;

    public GetCommand(IDatabase db) {
        this.db = db;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    @Override
    public String execute(JsonElement key, JsonElement value) {
        if (key != null && value == null){
            JsonElement dbValue = db.get(key);
            JsonObject response = new JsonObject();
            if(dbValue != null){
                response.addProperty("response", OK);
                response.add("value", gson.toJsonTree(dbValue));
            }
            else{
                response.addProperty("response", ERROR);
                response.addProperty("reason", NO_SUCH_KEY);
            }
            return gson.toJson(response);
        }
        throw new IllegalArgumentException("Invalid arguments for delete command");
    }
}

