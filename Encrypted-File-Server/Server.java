import java.io.*;
// import java.lang.*;
// import java.util.*;

public class Server {
    public static void main(String[] args) {
        TEALibrary teaLibrary = new TEALibrary();
        System.loadLibrary("tea");

        // String s = "abcdefghijklmnopqrstuvwxyzabcdef";
        // String s = "abcdefghijklmnopqrstuvwxyzabcdefghij"; // Test
        String s = "abcdefghijklmnopqrstuvwxyzabcdefghijkl"; // Test
        long[] testKey = {0, 1, 2, 3};

        System.out.println("String length: " + s.length());

        String encryptResult = new String(teaLibrary.encrypt(s.toCharArray(), testKey));
        System.out.println("Encryption results in java: " + encryptResult);
        System.out.println("Encrypted string length: " + encryptResult.length());

        String decryptResult = new String(teaLibrary.decrypt(encryptResult.toCharArray(), testKey));
        System.out.println("Decryption results in java: " + decryptResult);
        System.out.println("Decrypted string length: " + decryptResult.length());

        System.out.println("Decryption breakdown:");
        for (char c : decryptResult.toCharArray()) {
            System.out.println(c);
        }
    }
}
