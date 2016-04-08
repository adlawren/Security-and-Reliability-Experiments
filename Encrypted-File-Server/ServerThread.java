import java.io.*;
import java.net.*;
import java.util.*;
// import java.lang.*;
// import java.nio.charset.*;
import java.nio.file.*;

public class ServerThread extends Thread {

    private Socket clientSocket = null;

    public ServerThread(Socket initalClientSocket) {
        clientSocket = initalClientSocket;
    }

    public void run() {

        // TODO: Remove; test
        System.out.println("Server: Client connection received");

        try {
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String inputLine = null;
            while ((inputLine = reader.readLine()) != null) {

                // TODO: Remove; test
                System.out.println("Server: " + inputLine);

                File testFile = new File(inputLine);
                byte[] byteArray = Files.readAllBytes(testFile.toPath());

                // Write a byte array over a socket
                // DataOutputStream dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
                // dataOutputStream.writeInt(byteArray.length);
                // dataOutputStream.write(byteArray);

                // Write a long array over a socket
                long[] tempLongArray = new long[ byteArray.length ];
                for (int i = 0; i < tempLongArray.length; ++i) {
                    tempLongArray[i] = (long) byteArray[i];
                }

                TEALibrary teaLibrary = new TEALibrary();
                System.loadLibrary("tea");

                long[] tempKey = new long[]{0, 1, 2, 3};
                long[] encryptedLongArray = teaLibrary.encrypt(tempLongArray, tempKey);

                DataOutputStream dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
                dataOutputStream.writeInt(encryptedLongArray.length);
                for (int i = 0; i < encryptedLongArray.length; ++i) {
                    dataOutputStream.writeLong(encryptedLongArray[i]);
                }
            }

            // System.out.println("Server: Received from client: " + new String(message, Charset.forName("UTF-8")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
