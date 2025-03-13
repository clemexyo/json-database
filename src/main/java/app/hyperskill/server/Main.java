package app.hyperskill.server;

import app.hyperskill.server.commands.ICommand;
import app.hyperskill.server.database.JsonDatabase;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;

import java.io.*;
import java.net.*;
import java.nio.file.Path;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {
    private static final String LOCAL_ADDRESS = "127.0.0.1";
    private static final int PORT = 23456;
    private static final int THREAD_POOL_SIZE = 4;
    static final AtomicBoolean isRunning = new AtomicBoolean(true);

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        CommandRegistry registry = new CommandRegistry(
                new JsonDatabase(Path.of(System.getProperty("user.dir") + "/src/main/java/app/hyperskill/server/data/database.json"))
        );

        System.out.println("Server started!");

        try (ServerSocket serverSocket = new ServerSocket(PORT, 50, InetAddress.getByName(LOCAL_ADDRESS))) {
            while (isRunning.get()) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    executor.submit(new ClientHandler(clientSocket, registry, serverSocket));
                } catch (SocketException e) {
                    if (!isRunning.get()) {
                        System.out.println("Server is shutting down.");
                    } else {
                        System.err.println("Socket exception: " + e.getMessage());
                    }
                } catch (IOException e) {
                    System.err.println("Failed to accept client connection: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        } finally {
            executor.shutdown();
            try {
                if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }
}

class ClientHandler implements Runnable {
    private final Socket socket;
    private final CommandRegistry registry;
    private final ServerSocket serverSocket;

    public ClientHandler(Socket socket, CommandRegistry registry, ServerSocket serverSocket) {
        this.socket = socket;
        this.registry = registry;
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        try (DataInputStream input = new DataInputStream(socket.getInputStream());
             DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {

            JsonObject request = JsonParser.parseString(input.readUTF()).getAsJsonObject();
            System.out.println("Received: " + request);

            String type = request.get("type").getAsString();
            String key = request.has("key") ? request.get("key").getAsString() : null;
            String value = request.has("value") ? request.get("value").getAsString() : null;

            if ("exit".equals(type)) {
                Main.isRunning.set(false);
                serverSocket.close();
                output.writeUTF("{\"response\":\"Server is shutting down.\"}");
                System.out.println("Server shutdown initiated by client request.");
                return;
            }

            if (!registry.hasCommand(type)) {
                throw new IllegalArgumentException("Unknown command type: " + type);
            }

            ICommand command = registry.getCommand(type);
            String outcome = command.execute(key, value);

            output.writeUTF(outcome);
            System.out.println("Response sent: " + outcome);

        } catch (IOException e) {
            System.err.println("Client communication error: " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("Failed to close client socket: " + e.getMessage());
            }
        }
    }
}
