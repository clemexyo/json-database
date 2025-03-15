package app.hyperskill.server;

import app.hyperskill.server.database.JsonDatabase;
import java.io.IOException;
import java.nio.file.Path;

public class Main {
    private static final int PORT = 23456;

    public static void main(String[] args) {

        CommandRegistry registry = new CommandRegistry(
                new JsonDatabase(Path.of(System.getProperty("user.dir") + "/src/main/java/app/hyperskill/server/data/database.json")));
        try{
            Server server = new Server(PORT, registry);
            server.run();
        } catch (IOException e) {
            // throw new RuntimeException(e);
        }

    }
}

