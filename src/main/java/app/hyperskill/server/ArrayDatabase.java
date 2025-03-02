package app.hyperskill.server;

import java.util.Arrays;

public class ArrayDatabase implements IDatabase{
    private final String[] db;
    private final int size;

    public ArrayDatabase(int size) {
        this.size = size;
        this.db = new String[this.size];
        Arrays.fill(this.db, "");
    }
    @Override
    public boolean set(String newData, int index) {
        if (isValidIndex(index)){
            db[index] = newData;
            return true;
        }
        return false;
    }

    @Override
    public String get(int index) {
        if (isValidIndex(index) && !this.db[index].isEmpty()) {
            return db[index];
        }
        return null;
    }

    @Override
    public boolean delete(int index) {
        return set("", index);
    }

    private boolean isValidIndex(int index){
        return index < size && index > -1;
    }
}
