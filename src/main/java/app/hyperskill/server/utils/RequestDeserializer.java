package app.hyperskill.server.utils;

import app.hyperskill.server.database.DbRequest;
import com.google.gson.*;

import java.lang.reflect.Type;

public class RequestDeserializer implements JsonDeserializer<DbRequest> {
    @Override
    public DbRequest deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        // Extract values, supporting both key naming conventions
        String commandType = jsonObject.has("t") ? jsonObject.get("t").getAsString() :
                jsonObject.has("type") ? jsonObject.get("type").getAsString() : null;

        JsonElement key = jsonObject.has("k") ? jsonObject.get("k") :
                jsonObject.has("key") ? jsonObject.get("key") : null;

        JsonElement value = jsonObject.has("v") ? (jsonObject.get("v").isJsonNull() ? null : jsonObject.get("v")) :
                jsonObject.has("value") ? (jsonObject.get("value").isJsonNull() ? null : jsonObject.get("value")) : null;

        if (commandType == null) {
            throw new JsonParseException("Missing required fields: commandType and key");
        }

        return new DbRequest(commandType, key, value);
    }
}
