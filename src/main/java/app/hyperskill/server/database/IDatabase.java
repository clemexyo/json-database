package app.hyperskill.server.database;

public interface IDatabase {
    void set(String key, String value);

    String get(String key);

    boolean delete(String key);

}
