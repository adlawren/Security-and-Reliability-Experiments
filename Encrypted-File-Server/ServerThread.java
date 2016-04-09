import java.io.*;
import java.net.*;
import java.util.*;
import java.nio.charset.*;
import java.nio.file.*;

public class ServerThread extends Thread {

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

    private static long[] readEncryptedLongArray(DataInputStream dataInputStream, TEALibrary teaLibrary, long[] key) {
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

    private static long[] readLongArray(DataInputStream dataInputStream) {
        long[] tempLongArray = null;

        try {
            int length = dataInputStream.readInt();
            if (length > 0) {
                tempLongArray = new long[length];
                for (int i = 0; i < length; ++i) {
                    tempLongArray[i] = dataInputStream.readLong();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tempLongArray;
    }

    private static byte[] longArrayToByteArray(long[] longArray) {
        byte[] byteArray = new byte[ longArray.length ];
        for (int i = 0; i < byteArray.length; ++i) {
            byteArray[i] = (byte) longArray[i];
        }

        return byteArray;
    }

    private void sendFileNotFoundMessage(DataOutputStream dataOutputStream,
                                        TEALibrary teaLibrary,
                                        long[] key,
                                        ServerConfig serverConfig) {
        String messagePreset = serverConfig.getMessagePresets().get(ServerConfig.FNF);
        byte[] byteArray = messagePreset.getBytes();

        sendEncryptedBytes(dataOutputStream, teaLibrary, byteArray, key, ServerConfig.FNF);
    }

    private void sendAckMessage(DataOutputStream dataOutputStream,
                                TEALibrary teaLibrary,
                                long[] key,
                                ServerConfig serverConfig) {
        String messagePreset = serverConfig.getMessagePresets().get(ServerConfig.ACK);
        byte[] byteArray = messagePreset.getBytes();

        sendEncryptedBytes(dataOutputStream, teaLibrary, byteArray, key, ServerConfig.ACK);
    }

    private Socket clientSocket = null;

    public ServerThread(Socket initalClientSocket) {
        clientSocket = initalClientSocket;
    }

    public void run() {

        // Load server settings
        ServerConfig serverConfig = ServerConfig.getInstance();

        // Load encryption library
        TEALibrary teaLibrary = new TEALibrary();
        System.loadLibrary("tea");

        // TODO: Add proper credential selection
        long[] tempKey = new long[]{0, 1, 2, 3};

        System.out.println("[Server] Client connection received");

        try {

            // Initialize streams
            DataInputStream dataInputStream = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());

            // TODO: Retrieve encrypted user id
            // long[] userIdLongArray = readEncryptedLongArray(dataInputStream, teaLibrary, tempKey);
            // byte[] userIdByteArray = longArrayToByteArray(userIdLongArray);
            // String userId = new String(userIdByteArray, Charset.forName("UTF-8"));
            // System.out.println("[Server] User id: " + userId);

            long[] encryptedUserIdLongArray = readLongArray(dataInputStream);
            LoginPair loginPair = ServerConfig.getInstance().getCredentialsByEncryptedUserId(encryptedUserIdLongArray);

            if (loginPair == null) {
                System.err.println("[Server] ERROR: Login credentials could not be retrieved");
                return;
            }

            // TODO: Remove; test
            System.out.println("[Server] Processed credentials: userid: " + loginPair.getUserId() + ", key: " + Arrays.toString(loginPair.getKey()));

            Integer length = null;
            while ((length = dataInputStream.readInt()) != null) {
                byte[] message = null;

                if (length > 0) {
                    long[] tempLongArray = new long[length];
                    for (int i = 0; i < length; ++i) {
                        tempLongArray[i] = dataInputStream.readLong();
                    }

                    // Decrypt contents
                    long[] decryptedLongArray = teaLibrary.decrypt(tempLongArray, tempKey);

                    long messageType = decryptedLongArray[0];
                    if (messageType == ServerConfig.FILENAME) {
                        message = new byte[ decryptedLongArray.length - 1 ];
                        for (int i = 1; i < decryptedLongArray.length; ++i) {
                            message[i - 1] = (byte) decryptedLongArray[i];
                        }
                    } else if (messageType == ServerConfig.FIN) {
                        System.out.println("[Server] Recieved finished message; closing connection");
                        return;
                    }
                }

                String inputLine = new String(message, Charset.forName("UTF-8"));

                File file = new File(inputLine);
                if (file.exists()) {
                    sendAckMessage(dataOutputStream, teaLibrary, tempKey, serverConfig);

                    byte[] byteArray = Files.readAllBytes(file.toPath());

                    sendEncryptedBytesWithoutFlag(dataOutputStream, teaLibrary, byteArray, tempKey);
                } else {
                    sendFileNotFoundMessage(dataOutputStream, teaLibrary, tempKey, serverConfig);
                }
            }
        } catch(EOFException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
