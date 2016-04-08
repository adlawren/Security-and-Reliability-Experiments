import java.io.*;
import java.nio.file.*;
import java.nio.charset.*;
import java.net.*;
import java.util.*;
// import java.lang.*;

public class ServerConfig {

    // Client ids associated with pre-distributed keys
    private HashMap<String, long[]> keys = null;

    public HashMap<String, long[]> getLoginInfo() {
        if (keys == null) {
            keys = new HashMap<String, long[]>();
            keys.put("Client1", new long[]{0, 0, 0, 0});
            keys.put("Client2", new long[]{1, 0, 0, 0});
            keys.put("Client3", new long[]{2, 0, 0, 0});
            keys.put("Client4", new long[]{3, 0, 0, 0});
            keys.put("Client5", new long[]{4, 0, 0, 0});
            keys.put("Client6", new long[]{5, 0, 0, 0});
            keys.put("Client7", new long[]{6, 0, 0, 0});
            keys.put("Client8", new long[]{7, 0, 0, 0});
            keys.put("Client9", new long[]{8, 0, 0, 0});
            keys.put("Client10", new long[]{9, 0, 0, 0});
        }

        return keys;
    }

    public ServerConfig() {

        // ...
    }
}
