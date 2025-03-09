package app.hyperskill.server.database;

import java.util.HashMap;

public class JsonDatabase implements IDatabase{
    private final HashMap<String, String> db;

    public JsonDatabase(int size) {
        this.db = new HashMap<>(size);
    }

    @Override
    public void set(String key, String value) {
        db.put(key, value);
    }

    @Override
    public String get(String key) {
        if(db.containsKey(key)){
            return db.get(key);
        }
        return null;
    }

    @Override
    public boolean delete(String key) {
        if(db.containsKey(key)){
            db.remove(key);
            return true;
        }
        return false;
    }
}
