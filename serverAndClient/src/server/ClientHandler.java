package server;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final SnakeDatabase database;

    public ClientHandler(Socket socket, SnakeDatabase db) {
        this.clientSocket = socket;
        this.database = db;
    }

    @Override
    public void run() {
        try (
                BufferedReader entrada = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter salida = new PrintWriter(clientSocket.getOutputStream(), true)) {
            System.out.println("Nuevo cliente conectado: " + clientSocket.getInetAddress());
            System.out.println("Esperando mensaje del cliente...");

            StringBuilder requestBuilder = new StringBuilder();
            String line;

            while ((line = entrada.readLine()) != null && !line.isEmpty()) {
                System.out.println("Linea leida: " + line);
                requestBuilder.append(line).append("\n");
            }

            String rawRequest = requestBuilder.toString();

            // Procesamiento de la solicitud
            RequestHandler handler = new RequestHandler(rawRequest, database);
            String response = handler.returnResponse();

            if (response == null || response.isEmpty()) {
                response = "Empty response from server";
            }

            salida.println(response);
            salida.flush();

        } catch (IOException e) {
            System.err.println("Error handling client: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Error closing client socket: " + e.getMessage());
            }
        }
    }
}
