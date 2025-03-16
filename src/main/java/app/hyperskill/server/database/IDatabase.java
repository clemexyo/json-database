package app.hyperskill.server.database;

import com.google.gson.JsonElement;

public interface IDatabase {
    JsonElement get(JsonElement key);

    void set(JsonElement key, JsonElement value);

    boolean delete(JsonElement key);
}
