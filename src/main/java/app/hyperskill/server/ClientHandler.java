package app.hyperskill.server;

import app.hyperskill.server.commands.ICommand;
import app.hyperskill.server.database.DbRequest;
import app.hyperskill.server.utils.RequestDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private final CommandRegistry registry;
    private final Server server;

    public ClientHandler(Socket socket, CommandRegistry registry, Server server) {
        this.socket = socket;
        this.registry = registry;
        this.server = server;
    }

    @Override
    public void run() throws RuntimeException {
        try (DataInputStream input = new DataInputStream(socket.getInputStream());
             DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(DbRequest.class, new RequestDeserializer())
                    .create();
            JsonObject request = JsonParser.parseString(input.readUTF()).getAsJsonObject();
            System.out.println("Received: " + request);

            DbRequest transaction = gson.fromJson(request, DbRequest.class);

            if (!registry.hasCommand(transaction.getCommandType())) {
                throw new IllegalArgumentException("Unknown command type: " + transaction.getCommandType());
            }

            ICommand command = registry.getCommand(transaction.getCommandType());
            String outcome = command.execute(transaction.getKey(), transaction.getValue());

            output.writeUTF(outcome);
            System.out.println("Response sent: " + outcome);
            if ("exit".equals(transaction.getCommandType())) {
                server.stop();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
