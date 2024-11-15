import java.io.*;
import java.net.*;

public class QuizClient {
    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 1234;

    public static void main(String[] args) {
        try (
            Socket socket = new Socket(SERVER_IP, SERVER_PORT);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))
        ) {
            System.out.println("Connected to the server.");

            String serverMessage;
            while ((serverMessage = in.readLine()) != null) {
                if (serverMessage.startsWith("Question: ")) {
                    System.out.println(serverMessage);
                    System.out.print("Your answer: ");
                    String answer = userInput.readLine();
                    out.println(answer);
                } else {
                    System.out.println(serverMessage);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
