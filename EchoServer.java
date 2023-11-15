package Challenge4;

import java.io.*;
import java.net.*;

public class EchoServer {
  public static void main(String[] args) {
    try {
      ServerSocket serverSocket =
          new ServerSocket(Integer.parseInt(args[0])); // Use any available port

      System.out.println("Server waiting for client on port " + serverSocket.getLocalPort());

      Socket server = serverSocket.accept();
      System.out.println("Just connected to " + server.getRemoteSocketAddress());

      BufferedReader in = new BufferedReader(new InputStreamReader(server.getInputStream()));
      PrintWriter out = new PrintWriter(server.getOutputStream(), true);

      String inputLine;
      while ((inputLine = in.readLine()) != null) {
        System.out.println("Server received: " + inputLine);
        out.println("Server echoed: " + inputLine);
      }

      server.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
