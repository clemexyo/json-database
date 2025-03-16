package app.hyperskill.server.database;

import app.hyperskill.server.utils.RequestDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.annotations.JsonAdapter;

@JsonAdapter(RequestDeserializer.class)
public class DbRequest {
    private final String commandType;
    private final JsonElement key;
    private final JsonElement value; // This can be null

    public DbRequest(String commandType, JsonElement key, JsonElement value) {
        this.commandType = commandType;
        this.key = key;
        this.value = value;
    }

    public String getCommandType() {
        return commandType;
    }

    public JsonElement getKey() {
        return key;
    }

    public JsonElement getValue() {
        return value;
    }
}
