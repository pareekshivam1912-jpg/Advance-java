import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {

        try {
            Socket socket = new Socket("localhost", 5000);

            BufferedReader serverIn = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            BufferedReader keyboard = new BufferedReader(
                    new InputStreamReader(System.in));

            PrintWriter out = new PrintWriter(
                    socket.getOutputStream(), true);

            while (true) {

                System.out.print("You: ");
                String message = keyboard.readLine();

                out.println(message);

                if (message.equalsIgnoreCase("exit")) {
                    System.out.println("You left the chat.");
                    break;
                }

                String reply = serverIn.readLine();

                if (reply == null ||
                    reply.equalsIgnoreCase("exit")) {

                    System.out.println("Server ended the chat.");
                    break;
                }

                System.out.println("Server: " + reply);
            }

            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}