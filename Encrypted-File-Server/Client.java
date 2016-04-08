import java.io.*;
import java.nio.file.*;
import java.net.*;
import java.util.*;

public class Client {

    private static int clientCount = 0;

    public static void main(String args[]) {
        try {

            // Load server configuration
            ServerConfig serverConfig = new ServerConfig();

            // Load encryption library
            TEALibrary teaLibrary = new TEALibrary();
            System.loadLibrary("tea");

            // Obtain login credentials
            LoginPair loginCredentials = serverConfig.getCredentialsByClientId(clientCount++);

            // TODO: Remove; test
            System.out.println("Login Credentials: User id: " + loginCredentials.getUserId() + ", key: " + Arrays.toString(loginCredentials.getKey()));

            // TODO: Remove; test
            long[] tempKey = new long[]{0, 1, 2, 3};

            BufferedReader userReader = new BufferedReader(new InputStreamReader(System.in));

            Socket socket = new Socket("127.0.0.1", 16000);

            System.out.println("Provide the name of a file to retrieve:");

            // Get user input
            String userInput = null;
            while ((userInput = userReader.readLine()) != null) {
                if (userInput.equals("|EXIT")) {
                    String finishedString = new String("FINISHED");
                    byte[] usetInputBytes = finishedString.getBytes();

                    long[] userInputLongArray = new long[ usetInputBytes.length + 1 ];
                    userInputLongArray[0] = ServerConfig.FIN;
                    for (int i = 1; i < userInputLongArray.length; ++i) {
                        userInputLongArray[i] = (long) usetInputBytes[i - 1];
                    }

                    long[] encryptedUserInputLongArray = teaLibrary.encrypt(userInputLongArray, tempKey);

                    DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    dataOutputStream.writeInt(encryptedUserInputLongArray.length);
                    for (int i = 0; i < encryptedUserInputLongArray.length; ++i) {
                        dataOutputStream.writeLong(encryptedUserInputLongArray[i]);
                    }

                    return;
                } else {
                    byte[] usetInputBytes = userInput.getBytes();

                    long[] userInputLongArray = new long[ usetInputBytes.length + 1 ];
                    userInputLongArray[0] = ServerConfig.FILENAME;
                    for (int i = 1; i < userInputLongArray.length; ++i) {
                        userInputLongArray[i] = (long) usetInputBytes[i - 1];
                    }

                    long[] encryptedUserInputLongArray = teaLibrary.encrypt(userInputLongArray, tempKey);

                    DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    dataOutputStream.writeInt(encryptedUserInputLongArray.length);
                    for (int i = 0; i < encryptedUserInputLongArray.length; ++i) {
                        dataOutputStream.writeLong(encryptedUserInputLongArray[i]);
                    }

                    // Read byte array over the socket
                    DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                    int length = dataInputStream.readInt();
                    byte[] fileContents = null;

                    if (length > 0) {
                        long[] tempLongArray = new long[length];
                        for (int i = 0; i < length; ++i) {
                            tempLongArray[i] = dataInputStream.readLong();
                        }

                        // Decrypt contents
                        long[] decryptedLongArray = teaLibrary.decrypt(tempLongArray, tempKey);

                        if (decryptedLongArray[0] == ServerConfig.FNF) {
                            System.err.println("ERROR: File not found");
                        } else {
                            fileContents = new byte[ decryptedLongArray.length ];
                            for (int i = 0; i < decryptedLongArray.length; ++i) {
                                fileContents[i] = (byte) decryptedLongArray[i];
                            }

                            FileOutputStream fileOutputStream = new FileOutputStream(userInput + ".client.output");
                            fileOutputStream.write(fileContents);
                            fileOutputStream.close();

                            System.out.println("File found; contents written to: " + userInput + ".client.output");
                        }
                    }

                    System.out.println("Provide the name of a file to retrieve:");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
