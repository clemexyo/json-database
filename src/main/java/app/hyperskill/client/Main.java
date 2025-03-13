package app.hyperskill.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;


import com.beust.jcommander.JCommander;
import com.google.gson.*;

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
                    .build()
                    .parse(args);
            Gson gson = new GsonBuilder().create();
            Request request = new Request("", "", null);
            if(argsParserV2.fileName != null){
                try(FileReader reader = new FileReader(System.getProperty("user.dir") + "/src/main/java/app/hyperskill/client/data/" + argsParserV2.getFileName())){
                    JsonElement element = JsonParser.parseReader(reader);
                    JsonObject object = element.getAsJsonObject();

                    if(object.has("t")){
                        request.setType(object.get("t").getAsString());
                    }
                    if(object.has("type")){
                        request.setType(object.get("type").getAsString());
                    }
                    if(object.has("k")){
                        request.setKey(object.get("k").getAsString());
                    }
                    if(object.has("key")){
                        request.setKey(object.get("key").getAsString());
                    }
                    if(object.has("v")){
                        request.setValue(object.get("v").getAsString());
                    }
                    if(object.has("value")){
                        request.setValue(object.get("value").getAsString());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else{
                request = new Request(argsParserV2.getType(), argsParserV2.getKey(), argsParserV2.getValue());
            }
            System.out.printf("Sent: %s%n", gson.toJson(request));
            output.writeUTF(gson.toJson(request));
            System.out.println("Recevied: " + input.readUTF());
        } catch (IOException e) {
            System.out.println("something terrible has happened.");
        }
    }
}


