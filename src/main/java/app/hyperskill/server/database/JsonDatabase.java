package app.hyperskill.server.database;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class JsonDatabase implements IDatabase{
    private final List<DbRecord> db;
    private final Path path;
    private final Lock readLock;
    private final Lock writeLock;

    public JsonDatabase(Path path) {
        ReadWriteLock lock = new ReentrantReadWriteLock();
        readLock = lock.readLock();
        writeLock = lock.writeLock();
        Gson gson = new Gson();
        File dbFile = path.toFile();
        try{
            boolean created = dbFile.createNewFile();
            if(created){
                this.db = new ArrayList<>();
                this.path = path;
                try(FileWriter writer = new FileWriter(path.toFile())){
                    writer.write("[]");
                }
            }
            else{
                try(Reader reader = new FileReader(path.toFile())){
                    Type listType = new TypeToken<List<DbRecord>>() {}.getType();
                    this.db = gson.fromJson(reader, listType);
                    this.path = path;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("something went wrong during db creation");
        }

    }

    @Override
    public void set(String key, String value) {
        readLock.lock();
        boolean updated = false;
        for(int i = 0; i < db.size(); i++){
           if(db.get(i).key().equals(key)){
               db.set(i, new DbRecord(key, value));
               updated = true;
               break;
           }
        }
        readLock.unlock();
        if(!updated){
            writeLock.lock();
            db.add(new DbRecord(key, value));
            writeLock.unlock();
        }

        writeToFile();
    }

    @Override
    public DbRecord get(String key) {
        readLock.lock();
        Optional<DbRecord> found = db.stream().filter(dbRecord -> dbRecord.key().equals(key)).findFirst();
        readLock.unlock();
        return found.orElse(null);
    }

    @Override
    public boolean delete(String key) {
        readLock.lock();
        boolean updated = false;
        for(int i = 0; i < db.size(); i++){
            if(db.get(i).key().equals(key)){
                db.remove(i);
                updated = true;
                break;
            }
        }
        readLock.unlock();
        if(updated){
            writeToFile();
        }
        return updated;
    }

    private void writeToFile() {
        writeLock.lock();
        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter(path.toFile())) {
            gson.toJson(db, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        writeLock.unlock();
    }
}
