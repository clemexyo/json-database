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

public class Main {
    private static final String LOCAL_ADDRESS = "127.0.0.1";
    private static final int PORT = 23456;


    public static void main(String[] args) {
        CommandRegistry registry = new CommandRegistry(new JsonDatabase(Path.of(System.getProperty("user.dir") + "/src/main/java/app/hyperskill/server/data/database.json")));

        System.out.println("Server started!");
        String type = "PLACEHOLDER";
        while(!"exit".equals(type)){
            try(ServerSocket server = new ServerSocket(PORT, 50, InetAddress.getByName(LOCAL_ADDRESS))){
                // client connect
                Socket socket = server.accept();

                // get request from client
                DataInputStream input = new DataInputStream(socket.getInputStream());
                JsonObject request = JsonParser.parseString(input.readUTF()).getAsJsonObject();
                System.out.println("Received: " + request);

                // perform request
                DataOutputStream output  = new DataOutputStream(socket.getOutputStream());
                type = request.get("type").getAsString();
                String key = request.has("key") ? request.get("key").getAsString() : null;
                String value = request.has("value") ? request.get("value").getAsString() : null;
                if(!registry.hasCommand(type)){
                    throw new IllegalArgumentException();
                }
                ICommand command = registry.getCommand(type);
                String outcome = command.execute(key, value);

                // send response
                output.writeUTF(outcome);
                System.out.println(outcome);
            } catch (IOException e) {
                break;
            }
        }

    }
}



