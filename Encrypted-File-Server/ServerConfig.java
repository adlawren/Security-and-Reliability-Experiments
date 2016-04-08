import java.io.*;
import java.nio.file.*;
import java.nio.charset.*;
import java.net.*;
import java.util.*;

public class ServerConfig {

    // Flags used during communication
    public static final long ACK = 0;
    public static final long FNF = 1;
    public static final long FIN = 2;
    public static final long FILENAME = 3;
    public static final long FILE_CONTENTS = 4;

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

    private HashMap<Long, String> messagePresets = null;

    public HashMap<Long, String> getMessagePresets() {
        if (messagePresets == null) {
            messagePresets = new HashMap<Long, String>();
            messagePresets.put(FNF, "File not found");
        }

        return messagePresets;
    }

    public LoginPair getCredentialsByClientId(int clientId) {

        // Get user id
        ServerConfig serverConfig = new ServerConfig();
        HashMap<String, long[]> keys = serverConfig.getLoginInfo();

        Iterator<Map.Entry<String, long[]>> it = keys.entrySet().iterator();

        Map.Entry<String, long[]> entry = null;
        int i = 0;
        while (it.hasNext()) {
            entry = it.next();
            if (i == clientId) {
                break;
            }

            ++i;
        }

        LoginPair loginPair = new LoginPair(entry.getKey(), entry.getValue());
        return loginPair;
    }

    public LoginPair getCredentialsByEncryptedUserId(long[] encryptedUserId) {

        // TODO: Implement
        // ...

        return null;
    }

    public ServerConfig() {

        // ...
    }
}
