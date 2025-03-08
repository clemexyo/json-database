package app.hyperskill.client.validators;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

public class NonEmptyDataValidator implements IParameterValidator {
    @Override
    public void validate(String name, String value) throws ParameterException {
        if (value != null && value.trim().isEmpty()) {
            throw new ParameterException("Error: " + name + " cannot be empty.");
        }
    }
}
