import java.io.*;
import java.nio.file.*;
import java.net.*;
import java.util.*;
// import java.lang.*;

public class Server {

    // Client ids associated with pre-distributed keys
    private static final HashMap<String, long[]> keys;
    static {

        // TOOD: implement
        keys = new HashMap<String, long[]>();
        keys.put("asdf", new long[]{0, 1, 2, 3});
    }

    // TODO: Remove; test
    // private static void encryptionTest() {
    //     TEALibrary teaLibrary = new TEALibrary();
    //     System.loadLibrary("tea");
    //
    //     String s = "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz";
    //
    //     long[] testKey = {0, 1, 2, 3};
    //
    //     System.out.println("Plaintext string length: " + s.length());
    //
    //     String encryptResult = new String(teaLibrary.encrypt(s.toCharArray(), testKey));
    //     System.out.println("Encryption results in java: " + encryptResult);
    //     System.out.println("Encrypted string length: " + encryptResult.length());
    //
    //     String decryptResult = new String(teaLibrary.decrypt(encryptResult.toCharArray(), testKey));
    //     System.out.println("Decryption results in java: " + decryptResult);
    //     System.out.println("Decrypted string length: " + decryptResult.length());
    //
    //     assert s.equals(decryptResult);
    // }

    public static void main(String[] args) {
        try {

            // To read and write file as an array of bytes
            // String filePath = "test_files/asdf.txt";
            String filePath = "test_files/UofA_Eng_logo.jpg";

            FileInputStream fileInputStream = null;

            File testFile = new File(filePath);
            byte[] testBytes = Files.readAllBytes(testFile.toPath());

            FileOutputStream fileOutputStream = new FileOutputStream(filePath + ".output");
            fileOutputStream.write(testBytes);
            fileOutputStream.close();

            // Sending a byte array over a socket
            // ServerSocket serverSocket = new ServerSocket(16000);
            // Socket clientSocket = serverSocket.accept();
            //
            // System.out.println("Server: Client connection received");
            //
            // PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
            // BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            //
            // // writer.println("From server");
            //
            // System.out.println("Server: " + reader.readLine());
            // // writer.println("From server");
            //
            // String inputLine = null;
            // while ((inputLine = reader.readLine()) != null) {
            //     System.out.println("Server: " + inputLine);
            //
            //     writer.println("Message received: " + inputLine);
            // }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
