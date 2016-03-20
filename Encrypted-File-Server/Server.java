import java.io.*;
// import java.lang.*;
// import java.util.*;

public class Server {
    public static void main(String[] args) {
        TEALibrary teaLibrary = new TEALibrary();
        System.loadLibrary("tea");

        String s = "asdf";

        String encryptResult = new String(teaLibrary.encrypt(s.toCharArray()));
        System.out.println("Encryption results in java: " + encryptResult);

        String decryptResult = new String(teaLibrary.decrypt(s.toCharArray()));
        System.out.println("Decryption results in java: " + decryptResult);
    }
}
