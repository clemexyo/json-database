package app.hyperskill.server.utils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class JsonDbUtils {
    public static JsonElement readDbFromJsonFile(Path path) {
        try (Reader reader = Files.newBufferedReader(path)) {
            return JsonParser.parseReader(reader);
        } catch (IOException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public static void writeDbToJsonFile(JsonObject db, Path path) {
        try (Writer writer = Files.newBufferedWriter(path)) {
            new Gson().toJson(db, writer);
        } catch (IOException e){
            throw new RuntimeException(e.getMessage());
        }
    }

}
