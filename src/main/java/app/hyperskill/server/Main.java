package app.hyperskill.server;

import app.hyperskill.server.commands.ICommand;
import app.hyperskill.server.database.ArrayDatabase;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class Main {
    private static final String LOCAL_ADDRESS = "127.0.0.1";
    private static final int PORT = 23456;
    // private static final Pattern PATTERN = Pattern.compile("^(set|get|delete) (\\d+)(?: (.+))?$");


    public static void main(String[] args) {
        CommandRegistry registry = new CommandRegistry(new ArrayDatabase(1000));

        // String[] test = "get 1".split(" ", 3);
        // DbController dbController = new DbController(new ArrayDatabase(1000), scanner, pattern);
        // dbController.run();
        System.out.println("Server started!");
        while(true){
            try(ServerSocket server = new ServerSocket(PORT, 50, InetAddress.getByName(LOCAL_ADDRESS))){
                Socket socket = server.accept();
                DataInputStream input = new DataInputStream(socket.getInputStream());
                String msg = input.readUTF();
                if("exit command".equals(msg)){
                    break;
                }
                System.out.println("Received: " + msg);
                DataOutputStream output  = new DataOutputStream(socket.getOutputStream());
                String[] inputs = msg.split(" ", 3);
                if(!registry.hasCommand(inputs[0])){
                    throw new IllegalArgumentException();
                }
                ICommand command = registry.getCommand(inputs[0]);
                String outcome = command.execute(Arrays.copyOfRange(inputs, 1, inputs.length));
                output.writeUTF(outcome);
                System.out.println(outcome);
            } catch (IOException e) {
                break;
            }
        }

    }
}



