import java.io.*;
import java.nio.file.*;
import java.net.*;
import java.util.*;

public class Client {

    // TODO: Rework parameters
    private static void sendEncryptedBytes(DataOutputStream dataOutputStream,
                                        TEALibrary teaLibrary,
                                        byte[] bytes,
                                        long[] key,
                                        long code) {
        assert key.length == 4;

        long[] longArray = new long[ bytes.length + 1 ];
        longArray[0] = code;
        for (int i = 1; i < longArray.length; ++i) {
            longArray[i] = (long) bytes[i - 1];
        }

        long[] encryptedLongArray = teaLibrary.encrypt(longArray, key);

        try {
            dataOutputStream.writeInt(encryptedLongArray.length);
            for (int i = 0; i < encryptedLongArray.length; ++i) {
                dataOutputStream.writeLong(encryptedLongArray[i]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void sendEncryptedBytesWithoutFlag(DataOutputStream dataOutputStream,
                                        TEALibrary teaLibrary,
                                        byte[] bytes,
                                        long[] key) {
        assert key.length == 4;

        long[] longArray = new long[ bytes.length ];
        for (int i = 0; i < longArray.length; ++i) {
            longArray[i] = (long) bytes[i];
        }

        long[] encryptedLongArray = teaLibrary.encrypt(longArray, key);

        try {
            dataOutputStream.writeInt(encryptedLongArray.length);
            for (int i = 0; i < encryptedLongArray.length; ++i) {
                dataOutputStream.writeLong(encryptedLongArray[i]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static long[] readLongArray(DataInputStream dataInputStream, TEALibrary teaLibrary, long[] key) {
        long[] decryptedLongArray = null;

        try {
            int length = dataInputStream.readInt();
            if (length > 0) {
                long[] tempLongArray = new long[length];
                for (int i = 0; i < length; ++i) {
                    tempLongArray[i] = dataInputStream.readLong();
                }

                // Decrypt contents
                decryptedLongArray = teaLibrary.decrypt(tempLongArray, key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return decryptedLongArray;
    }

    private static byte[] longArrayToByteArray(long[] longArray) {
        byte[] byteArray = new byte[ longArray.length ];
        for (int i = 0; i < byteArray.length; ++i) {
            byteArray[i] = (byte) longArray[i];
        }

        return byteArray;
    }

    public static void main(String args[]) {
        try {

            // Obtain login credentials
            LoginPair loginCredentials = ServerConfig.getInstance().getCredentialsByClientId();

            // TODO: Remove; test
            System.out.println("Login Credentials: User id: " + loginCredentials.getUserId() + ", key: " + Arrays.toString(loginCredentials.getKey()));

            // Load encryption library
            TEALibrary teaLibrary = new TEALibrary();
            System.loadLibrary("tea");

            // Initialize streams
            Socket socket = new Socket("127.0.0.1", 16000);
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

            // TODO: Remove; test
            // long[] key = new long[]{0, 1, 2, 3};
            long[] key = loginCredentials.getKey();

            sendEncryptedBytesWithoutFlag(dataOutputStream, teaLibrary, loginCredentials.getUserId().getBytes(), loginCredentials.getKey());

            BufferedReader userReader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Provide the name of a file to retrieve:");

            // Get user input
            String userInput = null;
            while ((userInput = userReader.readLine()) != null) {
                if (userInput.equals("|EXIT")) {
                    String finishedString = new String("FINISHED");
                    sendEncryptedBytes(dataOutputStream, teaLibrary, finishedString.getBytes(), key, ServerConfig.FIN);

                    return;
                } else {
                    sendEncryptedBytes(dataOutputStream, teaLibrary, userInput.getBytes(), key, ServerConfig.FILENAME);

                    long[] decryptedLongArray = readLongArray(dataInputStream, teaLibrary, key);
                    if (decryptedLongArray[0] == ServerConfig.FNF) {
                        System.err.println("ERROR: File not found");
                    } else if (decryptedLongArray[0] == ServerConfig.ACK) {
                        System.out.println("Acknowledgement recieved");

                        long[] decryptedFileContents = readLongArray(dataInputStream, teaLibrary, key);
                        byte[] fileContents = longArrayToByteArray(decryptedFileContents);

                        FileOutputStream fileOutputStream = new FileOutputStream(userInput + ".client.output");
                        fileOutputStream.write(fileContents);
                        fileOutputStream.close();

                        System.out.println("File found; contents written to: " + userInput + ".client.output");
                    }

                    System.out.println("Provide the name of a file to retrieve:");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
