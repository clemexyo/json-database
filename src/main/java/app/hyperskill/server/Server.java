package app.hyperskill.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private final int port;
    private final CommandRegistry registry;
    private final ExecutorService service;
    private final ServerSocket serverSocket;

    public Server(int port, CommandRegistry registry) throws IOException {
        this.port = port;
        this.registry = registry;
        this.serverSocket = new ServerSocket(port);
        service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    public void run() {
        System.out.println("Server started!");
        try {
            while (true) {
                service.submit(new ClientHandler(serverSocket.accept(), registry, this));
            }
        } catch (IOException e) {
            // e.printStackTrace();
        }
    }

    public void stop() {
        try {
            serverSocket.close();
        } catch (IOException e) {

        }
        service.shutdown();
    }
}
