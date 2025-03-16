package app.hyperskill.server.commands;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class ExitCommand implements ICommand{
    private static final String OK = "OK";

    @Override
    public String execute(JsonElement key, JsonElement value) {
        JsonObject response = new JsonObject();
        response.addProperty("response", OK);
        return response.toString();
    }
}
