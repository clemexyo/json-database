package app.hyperskill.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;


import com.beust.jcommander.JCommander;

public class Main {
    private static final String LOCAL_ADDRESS = "127.0.0.1";
    private static final int PORT = 23456;

    public static void main(String[] args) {
        try (Socket socket = new Socket(InetAddress.getByName(LOCAL_ADDRESS), PORT);
            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {
            System.out.println("Client started!");

            ArgsParserV2 argsParserV2 = new ArgsParserV2();
            JCommander.newBuilder()
                    .addObject(argsParserV2)
                    .args(args)
                    .build();

            String requestString = argsParserV2.toString();

            System.out.printf("Sent: %s%n", requestString);
            output.writeUTF(requestString);
            System.out.println("Received: " + input.readUTF());
        } catch (IOException e) {
            System.out.println("something terrible must have happened in the server. " + e.toString());
        }
    }
}


