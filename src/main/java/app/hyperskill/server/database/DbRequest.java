package app.hyperskill.server.database;

import app.hyperskill.server.utils.RequestDeserializer;
import com.google.gson.annotations.JsonAdapter;

@JsonAdapter(RequestDeserializer.class)
public class DbRequest {
    private final String commandType;
    private final String key;
    private final String value; // This can be null

    public DbRequest(String commandType, String key, String value) {
        this.commandType = commandType;
        this.key = key;
        this.value = value;
    }

    public String getCommandType() {
        return commandType;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
