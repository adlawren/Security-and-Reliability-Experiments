import java.io.*;
import java.nio.file.*;
import java.nio.charset.*;
import java.net.*;
import java.util.*;

public class ServerConfig {

    private ServerConfig() {}

    private static ServerConfig instance = new ServerConfig();

    public static ServerConfig getInstance() {
        return instance;
    }


    // Flags used during communication
    public static final long ACK = 0;
    public static final long FNF = 1;
    public static final long FIN = 2;
    public static final long FILENAME = 3;
    public static final long FILE_CONTENTS = 4;
    public static final long ACCESS_GRANTED = 5;
    public static final long ACCESS_DENIED = 6;

    // Preset messages used during communication
    private HashMap<Long, String> messagePresets = null;

    public HashMap<Long, String> getMessagePresets() {
        if (messagePresets == null) {
            messagePresets = new HashMap<Long, String>();
            messagePresets.put(FNF, "File not found");
            messagePresets.put(ACK, "Acknowledged");
            messagePresets.put(ACCESS_GRANTED, "Access granted");
            messagePresets.put(ACCESS_DENIED, "Access denied");
        }

        return messagePresets;
    }

    // Filename
    private static final String filename = "server.keys";

    public LoginPair getCredentialsByClientId() {
        String newUserId = UUID.randomUUID().toString();

        long[] newKey = new long[4];

        Random random = new Random();
        for (int i = 0; i < newKey.length; ++i) {
            newKey[i] = random.nextLong();
        }

        String toAdd = newUserId + ":" + Arrays.toString(newKey);

        // Append to file if it exists
        try {
            File file = new File(filename);

            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filename, true));
            bufferedWriter.write(toAdd + "\n");
            bufferedWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // LoginPair loginPair = new LoginPair(entry.getKey(), entry.getValue());
        LoginPair loginPair = new LoginPair(newUserId, newKey);
        return loginPair;
    }

    public LoginPair getCredentialsByEncryptedUserId(long[] encryptedUserId) {

        // Load encryption library
        TEALibrary teaLibrary = new TEALibrary();
        System.loadLibrary("tea");

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));

            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                String[] strings = line.split(":");
                strings[1] = strings[1].replace("[", "");
                strings[1] = strings[1].replace("]", "");
                strings[1] = strings[1].replaceAll(" ", "");

                String[] longStrings = strings[1].split(",");

                long[] nextKey = new long[4];
                for (int i = 0; i < 4; ++i) {
                    nextKey[i] = Long.parseLong(longStrings[i]);
                }

                long[] decryptedUserId = teaLibrary.decrypt(encryptedUserId, nextKey);

                byte[] decryptedUserIdByteArray = new byte[ decryptedUserId.length ];
                for (int i = 0; i < decryptedUserIdByteArray.length; ++i) {
                    decryptedUserIdByteArray[i] = (byte) decryptedUserId[i];
                }

                byte[] nextIdBytes = strings[0].getBytes();

                if (decryptedUserIdByteArray.length > 0) {
                    boolean areIdsEqual = true;
                    for (int i = 0; i < decryptedUserIdByteArray.length; ++i) {
                        if (decryptedUserId[i] != nextIdBytes[i]) {
                            areIdsEqual = false;
                            break;
                        }
                    }

                    if (areIdsEqual) {
                        String decryptedUserIdString = new String(decryptedUserIdByteArray, Charset.forName("UTF-8"));
                        LoginPair loginPair = new LoginPair(decryptedUserIdString, nextKey);
                        return loginPair;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
