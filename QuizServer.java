import java.io.*;
import java.net.*;
import java.util.*;

public class QuizServer {
    private static final int PORT = 1234;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is running on port " + PORT);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected.");
                handleClient(clientSocket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket) {
        List<Question> questions = Arrays.asList(
            new Question("Which is the biggest ant in the world?", "An elephant"),
            new Question("What is it that lives if it is fed, and dies if you give it a drink?", "fire"),
            new Question("What goes up but never comes down?", "age")
        );

        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            int score = 0;

            for (Question question : questions) {
                // Send the question to the client
                out.println("Question: " + question.getQuestion());

                // Receive answer from the client
                String clientAnswer = in.readLine();
                if (clientAnswer.equalsIgnoreCase(question.getAnswer())) {
                    out.println("Correct!");
                    score++;
                } else {
                    out.println("Incorrect! The correct answer is: " + question.getAnswer());
                }
            }

            // Send the final score
            out.println("Quiz finished! Your total score is: " + score + " out of " + questions.size());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static class Question {
        private final String question;
        private final String answer;

        public Question(String question, String answer) {
            this.question = question;
            this.answer = answer;
        }

        public String getQuestion() {
            return question;
        }

        public String getAnswer() {
            return answer;
        }
    }
}
