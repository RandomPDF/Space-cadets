package Challenge4;

import java.io.*;
import java.net.*;

public class EchoClient {
  public static void main(String[] args) {
    try {
      Socket clientSocket =
          new Socket(
              args[0],
              Integer.parseInt(args[1])); // Connect to the server on localhost and port 12345

      PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
      BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
      BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

      String userInput;
      while ((userInput = stdIn.readLine()) != null) {
        out.println(userInput);
        System.out.println("Client received: " + in.readLine());
      }

      clientSocket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
