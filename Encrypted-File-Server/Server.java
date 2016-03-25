import java.io.*;
import java.net.*;
// import java.lang.*;
// import java.util.*;

public class Server {

    // TODO: Remove; test
    private void encryptionTest() {
        TEALibrary teaLibrary = new TEALibrary();
        System.loadLibrary("tea");

        String s = "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz";

        long[] testKey = {0, 1, 2, 3};

        System.out.println("String length: " + s.length());

        String encryptResult = new String(teaLibrary.encrypt(s.toCharArray(), testKey));
        System.out.println("Encryption results in java: " + encryptResult);
        System.out.println("Encrypted string length: " + encryptResult.length());

        String decryptResult = new String(teaLibrary.decrypt(encryptResult.toCharArray(), testKey));
        System.out.println("Decryption results in java: " + decryptResult);
        System.out.println("Decrypted string length: " + decryptResult.length());
    }

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(16000);
            Socket clientSocket = serverSocket.accept();

            System.out.println("Server: Client connection received");

            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            writer.println("From server");

            String inputLine = null;
            while ((inputLine = reader.readLine()) != null) {
                System.out.println("Server: " + inputLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
