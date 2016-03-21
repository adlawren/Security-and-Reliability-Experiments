import java.io.*;
// import java.lang.*;
// import java.util.*;

public class Server {
    public static void main(String[] args) {
        TEALibrary teaLibrary = new TEALibrary();
        System.loadLibrary("tea");

        // String s = "asdfasdfasdfasdfasdfasdfasdfasdf";
        String s = "abcdefghijklmnopqrstuvwxyzabcdef";
        long[] testKey = {0, 0, 0, 0};

        // String encryptResult = new String(teaLibrary.encrypt(s.toCharArray(), testKey));
        String encryptResult = new String(teaLibrary.encrypt(s.toCharArray(), testKey));
        System.out.println("Encryption results in java: " + encryptResult);

        // String decryptResult = new String(teaLibrary.decrypt(s.toCharArray(), testKey));
        String decryptResult = new String(teaLibrary.decrypt(encryptResult.toCharArray(), testKey));
        System.out.println("Decryption results in java: " + decryptResult);
    }
}
