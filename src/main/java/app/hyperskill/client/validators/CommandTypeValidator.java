package app.hyperskill.client.validators;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

import java.util.Set;

public class CommandTypeValidator implements IParameterValidator {
    private static final Set<String> VALID_COMMANDS = Set.of("set", "get", "delete", "exit");

    @Override
    public void validate(String name, String value) throws ParameterException {
        if (!VALID_COMMANDS.contains(value)) {
            throw new ParameterException("Invalid command: " + value + ". Allowed: set, get, delete.");
        }
    }
}
