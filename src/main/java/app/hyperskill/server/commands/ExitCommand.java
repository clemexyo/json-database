package app.hyperskill.server.commands;

import com.google.gson.JsonObject;

public class ExitCommand implements ICommand{
    private static final String OK = "OK";

    @Override
    public String execute(String key, String value) {
        JsonObject response = new JsonObject();
        response.addProperty("response", OK);
        return response.toString();
    }
}
