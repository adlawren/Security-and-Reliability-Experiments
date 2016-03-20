import java.io.*;
// import java.lang.*;
// import java.util.*;

public class Server {
    public static void main(String[] args) {
        TEALibrary teaLibrary = new TEALibrary();
        System.loadLibrary("tea");

        // TODO: Use string later
        String s = "asdf";

        char[] charArray = {'a', 's', 'd', 'f'};

        // char[] encryptResult = teaLibrary.encrypt(charArray);
        // teaLibrary.encrypt(s);
        String encryptResult = teaLibrary.encrypt(s);

        System.out.println("Encryption results in java: size: " +
            encryptResult.length() +
            " Contents: " +
            encryptResult);

        char[] decryptResult = teaLibrary.decrypt(charArray);

        System.out.println("Decryption results in java: ");
        for (int i = 0; i < decryptResult.length; ++i) {
            System.out.print(decryptResult[i]);
        }

        System.out.println("");
    }
}
