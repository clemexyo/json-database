package app.hyperskill.client;

import com.google.gson.annotations.Expose;

public class ClientRequest {
    @Expose
    private final String type;
    @Expose
    private final String key;
    @Expose
    private final String value;

    public ClientRequest(String type, String key, String value) {
        this.type = type;
        this.key = key;
        this.value = value;
    }

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
