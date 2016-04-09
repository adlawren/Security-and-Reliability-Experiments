import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.net.*;
import java.util.*;
// import java.lang.*;

public class Server {

    // TODO: Remove; test
    private static void encryptionTest() {
        TEALibrary teaLibrary = new TEALibrary();
        System.loadLibrary("tea");

        long[] testKey = {0, 1, 2, 3};

        int value_count = 100;
        long[] testLongArray = new long[value_count];

        for (int i = 0; i < value_count; ++i) {
            testLongArray[i] = (long) i;
        }

        long[] encryptedLongs = teaLibrary.encrypt(testLongArray, testKey);

        System.out.println("Encrypted longs:");
        for (int i = 0; i < encryptedLongs.length; ++i) {
            System.out.println("Next long: " + encryptedLongs[i]);
        }

        long[] decryptedLongs = teaLibrary.decrypt(encryptedLongs, testKey);

        System.out.println("Decrypted longs:");
        for (int i = 0; i < decryptedLongs.length; ++i) {
            System.out.println("Next long: " + decryptedLongs[i]);
        }

        assert testLongArray.length == decryptedLongs.length;

        for (int i = 0; i < testLongArray.length; ++i) {
            if (testLongArray[i] != decryptedLongs[i]) {
                System.out.println("ERROR: Arrays unequal");
                break;
            }
        }
    }

    public static void main(String[] args) {
        // encryptionTest();

        try {

            ServerSocket serverSocket = new ServerSocket(16000);

            Socket clientSocket = null;
            while (true) {
                clientSocket = serverSocket.accept();
                ServerThread serverThread = new ServerThread(clientSocket);
                serverThread.start();
                // break; // TODO: Remove; used for testing
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
