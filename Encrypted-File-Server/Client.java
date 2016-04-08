import java.io.*;
import java.nio.file.*;
import java.net.*;
import java.util.*;
// import java.lang.*;

class LoginPair {
    private String userId = null;
    private long[] key = null;

    public LoginPair(String id, long[] k) {
        userId = id;
        key = k;
    }

    public String getUserId() {
        return userId;
    }

    public long[] getKey() {
        return key;
    }
}

public class Client {

    private static int clientCount = 0;

    // private String userId = null;
    // private long[] userKey = null;

    private static LoginPair getCredentials() {
        int clientId = clientCount++;

        // Get user id
        ServerConfig serverConfig = new ServerConfig();
        HashMap<String, long[]> keys = serverConfig.getLoginInfo();

        Iterator it = keys.entrySet().iterator();

        Map.Entry entry = null;
        int i = 0;
        while (it.hasNext()) {
            entry = (Map.Entry) it.next();
            if (i == clientId) {
                break;
            }

            ++i;
        }

        LoginPair loginPair = new LoginPair((String) entry.getKey(), (long[]) entry.getValue());
        return loginPair;
    }

    public static void main(String args[]) {
        try {

            // Obtain login credentials
            LoginPair loginCredentials = getCredentials();

            // Get user input
            BufferedReader userReader = new BufferedReader(new InputStreamReader(System.in));

            Socket socket = new Socket("127.0.0.1", 16000);

            PrintWriter socketWriter = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // String filePath = "test_files/asdf.txt";
            //
            // File testFile = new File(filePath);
            // byte[] testBytes = Files.readAllBytes(testFile.toPath());
            //
            // // Write a byte array over a socket
            // String message = "From Client";
            // byte[] byteArray =  testBytes; // message.getBytes();
            // DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            // dataOutputStream.writeInt(byteArray.length);
            // dataOutputStream.write(byteArray);

            // writer.println("Initial message from client");

            // To retrieve user input:
            String userInput = null;
            while ((userInput = userReader.readLine()) != null) {
                System.out.println("User input: " + userInput);

                // TODO: Encrypt user input
                // ...

                socketWriter.println(userInput);

                // Reading a byte array over a socket
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                int length = dataInputStream.readInt();
                byte[] fileContents = null;

                // TODO: Fix assumption
                if (length > 0) {
                    // dataInputStream.readFully(fileContents, 0, length);

                    long[] tempLongArray = new long[length];
                    for (int i = 0; i < length; ++i) {
                        tempLongArray[i] = dataInputStream.readLong();
                    }

                    TEALibrary teaLibrary = new TEALibrary();
                    System.loadLibrary("tea");

                    long[] tempKey = new long[]{0, 1, 2, 3};
                    long[] decryptedLongArray = teaLibrary.decrypt(tempLongArray, tempKey);

                    fileContents = new byte[ decryptedLongArray.length ];
                    for (int i = 0; i < decryptedLongArray.length; ++i) {
                        fileContents[i] = (byte) decryptedLongArray[i];
                    }
                }

                FileOutputStream fileOutputStream = new FileOutputStream(userInput + ".output.client");
                fileOutputStream.write(fileContents);
                fileOutputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
