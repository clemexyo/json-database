package app.hyperskill.server.database;

public interface IDatabase {
    boolean set(int index, String value);

    String get(int index);

    boolean delete(int index);
}
