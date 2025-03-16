package app.hyperskill.server.commands;

import com.google.gson.JsonElement;

public interface ICommand {
    String execute(JsonElement key, JsonElement value);
}
