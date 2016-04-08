import java.io.*;
import java.net.*;
import java.util.*;
import java.nio.charset.*;
import java.nio.file.*;

public class ServerThread extends Thread {

    private Socket clientSocket = null;

    public ServerThread(Socket initalClientSocket) {
        clientSocket = initalClientSocket;
    }

    public void run() {

        // Load server settings
        ServerConfig serverConfig = new ServerConfig();

        // Load encryption library
        TEALibrary teaLibrary = new TEALibrary();
        System.loadLibrary("tea");

        // TODO: Add proper credential selection
        long[] tempKey = new long[]{0, 1, 2, 3};

        System.out.println("[Server] Client connection received");

        try {

            // Read byte array over the socket
            DataInputStream dataInputStream = new DataInputStream(clientSocket.getInputStream());

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
                    byte[] byteArray = Files.readAllBytes(file.toPath());

                    // Write long array over the socket
                    long[] tempLongArray = new long[ byteArray.length ];
                    for (int i = 0; i < tempLongArray.length; ++i) {
                        tempLongArray[i] = (long) byteArray[i];
                    }

                    // Encrypt contents
                    long[] encryptedLongArray = teaLibrary.encrypt(tempLongArray, tempKey);

                    DataOutputStream dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
                    dataOutputStream.writeInt(encryptedLongArray.length);
                    for (int i = 0; i < encryptedLongArray.length; ++i) {
                        dataOutputStream.writeLong(encryptedLongArray[i]);
                    }
                } else {
                    String messagePreset = serverConfig.getMessagePresets().get(ServerConfig.FNF);
                    byte[] byteArray = messagePreset.getBytes();

                    // Write long array over the socket
                    long[] tempLongArray = new long[ byteArray.length + 1 ];
                    tempLongArray[0] = ServerConfig.FNF;
                    for (int i = 1; i < tempLongArray.length; ++i) {
                        tempLongArray[i] = (long) byteArray[i - 1];
                    }

                    // Encrypt contents
                    long[] encryptedLongArray = teaLibrary.encrypt(tempLongArray, tempKey);

                    DataOutputStream dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
                    dataOutputStream.writeInt(encryptedLongArray.length);
                    for (int i = 0; i < encryptedLongArray.length; ++i) {
                        dataOutputStream.writeLong(encryptedLongArray[i]);
                    }
                }
            }
        } catch(EOFException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
