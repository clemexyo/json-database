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

            ArgsParser argsParser = new ArgsParser();
            JCommander.newBuilder()
                    .addObject(argsParser)
                    .build()
                    .parse(args);

            String toSend = String.format("%s %d", argsParser.getType(), argsParser.getIndex() - 1);
            if(!argsParser.getType().equals("exit")){
                if(argsParser.getMessage() != null){
                    toSend += " " + argsParser.getMessage();
                }
                output.writeUTF(toSend);
                System.out.println("Sent: " + toSend);
                System.out.println("Received: " + input.readUTF());
            }
            else{
                output.writeUTF("exit command");
            }
        } catch (IOException e) {
            System.out.println("something terrible has happened.");
        }
    }
}


