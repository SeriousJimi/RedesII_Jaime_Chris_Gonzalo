package server;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class Server {
    private static final int PORT = 1234;
    
    public static void main(String[] args) {
        SnakeDatabase database = new SnakeDatabase();
        ExecutorService pool = Executors.newFixedThreadPool(10); // Hasta 10 clientes
        System.out.println("Servidor iniciado en puerto " + PORT);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Nuevo cliente conectado: " + clientSocket.getInetAddress());
                pool.execute(new ClientHandler(clientSocket, database));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}