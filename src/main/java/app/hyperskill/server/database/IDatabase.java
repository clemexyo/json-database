package app.hyperskill.server.database;

public interface IDatabase {
    void set(String key, String value);

    DbRecord get(String key);

    boolean delete(String key);

}
