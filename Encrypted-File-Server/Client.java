import java.io.*;
import java.net.*;
import java.util.*;
// import java.lang.*;

// TODO: Remove; test

public class Client {

    // Client ids associated with pre-distributed keys
    private static final HashMap<String, long[]> keys;
    static {

        // TOOD: implement
        keys = new HashMap<String, long[]>();
        keys.put("asdf", new long[]{0, 1, 2, 3});
    }

    public static void main(String args[]) {
        try {
            // Get user input
            BufferedReader userReader = new BufferedReader(new InputStreamReader(System.in));

            Socket socket = new Socket("127.0.0.1", 16000);

            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            writer.println("Initial message from client");

            // System.out.println("Client: " + reader.readLine());
            // writer.println("From client");

            // To retrieve user input:
            String userInput = null;
            while ((userInput = userReader.readLine()) != null) {
                System.out.println("User input: " + userInput);

                // TODO: Encrypt user input
                // ...

                writer.println(userInput);

                // To pend on server:
                System.out.println("Client: " + reader.readLine());
            }

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
