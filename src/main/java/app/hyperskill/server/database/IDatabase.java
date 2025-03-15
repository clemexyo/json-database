package app.hyperskill.server.database;

import com.google.gson.JsonElement;

public interface IDatabase {
    void set(String key, String value);

    JsonElement get(String key);

    boolean delete(String key);

}
