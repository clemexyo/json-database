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
    public void set(String key, String value) {
        writeLock.lock();

        dbObject.addProperty(key, value);
        JsonDbUtils.writeDbToJsonFile(dbObject, dbPath);

        writeLock.unlock();
    }

    @Override
    public JsonElement get(String key) {
        readLock.lock();
        JsonElement dbValue = dbObject.get(key);
        readLock.unlock();

        return dbValue;
    }

    @Override
    public boolean delete(String key) {
        if(Objects.nonNull(this.get(key))){
            writeLock.lock();
            dbObject.remove(key);
            JsonDbUtils.writeDbToJsonFile(dbObject, dbPath);
            writeLock.unlock();
            return true;
        }
        return false;
    }
}
