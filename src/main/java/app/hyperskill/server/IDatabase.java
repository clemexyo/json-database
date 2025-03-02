package app.hyperskill.server;

public interface IDatabase {
    boolean set(String value, int index);

    String get(int index);

    boolean delete(int index);
}
