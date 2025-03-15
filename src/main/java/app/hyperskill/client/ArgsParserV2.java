package app.hyperskill.client;

import app.hyperskill.client.validators.CommandTypeValidator;
import app.hyperskill.client.validators.NonEmptyDataValidator;
import com.beust.jcommander.Parameter;
import com.google.gson.Gson;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;

public class ArgsParserV2 {
    @Parameter(names = {"-t", "--type"}, validateWith = CommandTypeValidator.class, description = "type of the operation")
    public String type;

    @Parameter(names = { "-k", "--key" }, description = "key inside database")
    public String key;

    @Parameter(names = {"-v", "--value"}, validateWith = NonEmptyDataValidator.class, description = "value for the key")
    public String value;

    @Parameter(names = {"-in"}, description = "name of the file to read the request, this will be used if command type is -in")
    public String fileName;

    public String getType() {
        return type;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public String getFileName() {
        return fileName;
    }

    @Override
    public String toString() {
        if (fileName != null) {
            try (Reader reader = Files.newBufferedReader(Path.of(System.getProperty("user.dir") + "/src/main/java/app/hyperskill/client/data/" + this.fileName))) {
                return JsonParser.parseReader(reader).getAsJsonObject().toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new Gson().toJson(this);
    }
}
