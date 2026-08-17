import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) {

        try {
            ServerSocket serverSocket = new ServerSocket(5000);

            System.out.println("Server started...");
            System.out.println("Waiting for client...");

            Socket socket = serverSocket.accept();

            System.out.println("Client Connected: "
                    + socket.getInetAddress());

            BufferedReader clientIn = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            BufferedReader keyboard = new BufferedReader(
                    new InputStreamReader(System.in));

            PrintWriter out = new PrintWriter(
                    socket.getOutputStream(), true);

            String message;

            while (true) {

                message = clientIn.readLine();

                if (message == null ||
                    message.equalsIgnoreCase("exit")) {

                    System.out.println("Client left the chat.");
                    break;
                }

                System.out.println("Client: " + message);

                System.out.print("Server: ");
                String reply = keyboard.readLine();

                out.println(reply);

                if (reply.equalsIgnoreCase("exit")) {
                    System.out.println("Server ended the chat.");
                    break;
                }
            }

            socket.close();
            serverSocket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}