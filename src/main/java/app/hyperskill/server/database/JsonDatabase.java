package app.hyperskill.server.database;

import app.hyperskill.server.utils.JsonDbUtils;
import com.google.gson.*;

import java.io.*;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class JsonDatabase implements IDatabase{
    private final JsonObject dbObject;
    private final Path dbPath;
    private final Lock readLock;
    private final Lock writeLock;

    public JsonDatabase(Path path) {
        ReadWriteLock lock = new ReentrantReadWriteLock();
        readLock = lock.readLock();
        writeLock = lock.writeLock();

        File dbFile = path.toFile();
        if(!dbFile.exists() || dbFile.length() == 0){
            JsonDbUtils.writeDbToJsonFile(new JsonObject(), path);
        }

        this.dbObject = JsonDbUtils.readDbFromJsonFile(path).getAsJsonObject();
        this.dbPath = path;
    }

    @Override
    public JsonElement get(JsonElement key) {
        readLock.lock();

        JsonElement dbValue = null;
        if(key.isJsonArray()){ // recursive call to the final value
            dbValue = getRecursive(dbObject, key.getAsJsonArray(), 0);
        }
        else if(key.isJsonPrimitive()){ // json primitive
            dbValue = dbObject.get(key.toString());
        }

        readLock.unlock();
        return dbValue;
    }

    @Override
    public void set(JsonElement key, JsonElement value) {
        writeLock.lock();

        if(key.isJsonArray()){
            JsonArray keys = key.getAsJsonArray();
            JsonElement currentElement = dbObject;

            // Traverse through the keys array (except the last key)
            for (int i = 0; i < keys.size() - 1; i++) {
                String keyString = keys.get(i).getAsString();

                // If the current element is not a JsonObject, create one
                if (currentElement.isJsonObject()) {
                    JsonObject currentObject = currentElement.getAsJsonObject();
                    // If the key doesn't exist, create a new JsonObject
                    if (!currentObject.has(keyString)) {
                        currentObject.add(keyString, new JsonObject());
                    }
                    // Move to the next nested JsonObject
                    currentElement = currentObject.get(keyString);
                } else if (currentElement.isJsonArray()) {
                    JsonArray currentArray = currentElement.getAsJsonArray();
                    // Handle array index (if the key corresponds to an index)
                    int index = Integer.parseInt(keys.get(i).getAsString()); // Assumes key is an index in string form
                    if (index >= currentArray.size()) {
                        // If the index is out of bounds, add null elements to the array
                        for (int j = currentArray.size(); j <= index; j++) {
                            currentArray.add(new JsonObject());
                        }
                    }
                    // Move to the next element at the index
                    currentElement = currentArray.get(index);
                }
            }

            // Set the final value for the last key
            String finalKey = keys.get(keys.size() - 1).getAsString();
            if (currentElement.isJsonObject()) {
                currentElement.getAsJsonObject().add(finalKey, value);
            }
        }
        else if(key.isJsonPrimitive()){
            dbObject.addProperty(key.toString(), value.toString());
        }

        JsonDbUtils.writeDbToJsonFile(dbObject, dbPath);
        writeLock.unlock();
    }

    @Override
    public boolean delete(JsonElement key) {
        if(Objects.nonNull(this.get(key))){
            writeLock.lock();
            if(key.isJsonArray()){
                JsonArray keys = key.getAsJsonArray();
                JsonElement currentElement = dbObject;
                JsonObject parentObject = null;
                String parentKey = null;

                // Traverse through the keys array (except the last key)
                for (int i = 0; i < keys.size() - 1; i++) {
                    String keyString = keys.get(i).getAsString();

                    // If the current element is not a JsonObject, we can't proceed
                    if (currentElement.isJsonObject()) {
                        JsonObject currentObject = currentElement.getAsJsonObject();
                        // If the key doesn't exist, return false (key path is invalid)
                        if (!currentObject.has(keyString)) {
                            writeLock.unlock();
                            return false;
                        }
                        // Move to the next nested JsonObject
                        currentElement = currentObject.get(keyString);
                    } else if (currentElement.isJsonArray()) {
                        JsonArray currentArray = currentElement.getAsJsonArray();
                        // Handle array index (if the key corresponds to an index)
                        int index = Integer.parseInt(keys.get(i).getAsString()); // Assumes key is an index in string form
                        if (index >= currentArray.size()) {
                            writeLock.unlock();
                            return false; // Index out of bounds
                        }
                        // Move to the next element at the index
                        currentElement = currentArray.get(index);
                    }
                }

                // Now, we're at the element just before the final key
                parentObject = currentElement.getAsJsonObject();
                parentKey = keys.get(keys.size() - 1).getAsString();

                // If the key exists, remove it
                if (parentObject != null && parentObject.has(parentKey)) {
                    parentObject.remove(parentKey);
                    JsonDbUtils.writeDbToJsonFile(dbObject, dbPath);
                    writeLock.unlock();
                    return true; // Successfully deleted
                }
            }
            else if(key.isJsonPrimitive()){
                dbObject.remove(key.toString());
            }

            JsonDbUtils.writeDbToJsonFile(dbObject, dbPath);
            writeLock.unlock();
            return true;
        }
        return false;
    }

    private JsonElement getRecursive(JsonElement currentElement, JsonArray keys, int index) {
        // Base case: If the index is beyond the keys array, return the current element
        if (index >= keys.size()) {
            return currentElement;
        }

        // Get the current key to check at this level
        String key = keys.get(index).getAsString();

        // If the current element is a JsonObject, we look for the key in it
        if (currentElement.isJsonObject()) {
            JsonObject currentObject = currentElement.getAsJsonObject();
            // Get the next element from the object using the key
            JsonElement nextElement = currentObject.get(key);

            // If the key doesn't exist, return null
            if (nextElement == null) {
                return null;
            }

            // Recur with the next element and increment the index
            return getRecursive(nextElement, keys, index + 1);
        }

        // If the current element is a JsonArray, and we're at the last key in the keys array
        // just return the entire array, as the array is the value in this case.
        else if (currentElement.isJsonArray()) {
            // If we are at the last key in the keys array, return the entire array
            if (index == keys.size() - 1) {
                return currentElement;  // Return the entire array as the value
            }

            // Otherwise, treat the key as an index and retrieve the element at that index
            int arrayIndex = Integer.parseInt(key);  // Convert key to an integer index
            JsonElement nextElement = currentElement.getAsJsonArray().get(arrayIndex);

            // If the index is out of bounds, return null
            if (nextElement == null) {
                return null;
            }

            // Recur with the next element and increment the index
            return getRecursive(nextElement, keys, index + 1);
        }

        // If currentElement is neither a JsonObject nor JsonArray, return null
        return null;
    }


}
