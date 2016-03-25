import java.io.*;
import java.net.*;
import java.util.*;
// import java.lang.*;

public class Client {
    public static void main(String args[]) {
        try {
            Socket socket = new Socket("127.0.0.1", 16000);

            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            System.out.println("Client: " + reader.readLine());
            writer.println("From client");

            // To pend on server:
            // String fromServer = null;
            // while ((fromServer = reader.readLine()) != null) {
            //     System.out.println("Client: " + fromServer);
            // }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
