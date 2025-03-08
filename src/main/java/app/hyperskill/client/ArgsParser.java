package app.hyperskill.client;

import app.hyperskill.client.validators.CommandTypeValidator;
import app.hyperskill.client.validators.NonEmptyDataValidator;
import com.beust.jcommander.Parameter;

public class ArgsParser {
    @Parameter(names = {"-t", "--type"}, validateWith = CommandTypeValidator.class, description = "type of the operation")
    public String type;

    @Parameter(names = { "-i", "--index" }, description = "index inside database")
    public int index;

    @Parameter(names = {"-m", "--message"}, validateWith = NonEmptyDataValidator.class, description = "data for the operation")
    public String message;

    public String getType() {
        return type;
    }

    public int getIndex() {
        return index;
    }

    public String getMessage() {
        return message;
    }

}
