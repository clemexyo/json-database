package app.hyperskill.client;

import app.hyperskill.client.validators.CommandTypeValidator;
import app.hyperskill.client.validators.NonEmptyDataValidator;
import com.beust.jcommander.Parameter;

public class ArgsParserV2 {
    @Parameter(names = {"-t", "--type"}, validateWith = CommandTypeValidator.class, description = "type of the operation")
    public String type;

    @Parameter(names = { "-k", "--key" }, description = "key inside database")
    public String key;

    @Parameter(names = {"-v", "--value"}, validateWith = NonEmptyDataValidator.class, description = "value for the key")
    public String value;

    public String getType() {
        return type;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
