package app.hyperskill.client.validators;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

public class PositiveIndexValidator implements IParameterValidator {
    @Override
    public void validate(String name, String value) throws ParameterException {
        int index = Integer.parseInt(value);
        if (index < 0) {
            throw new ParameterException("Error: " + name + " must be a non-negative integer.");
        }
    }
}
