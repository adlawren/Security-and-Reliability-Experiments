import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.net.*;
import java.util.*;
// import java.lang.*;

public class Server {

    public static void main(String[] args) {
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
