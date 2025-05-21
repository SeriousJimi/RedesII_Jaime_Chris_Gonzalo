package client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.net.Socket;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ClientGUI gui = new ClientGUI();
            gui.setVisible(true);

            gui.getSendButton().addActionListener((ActionEvent event) -> {
                try {
                    String host = "localhost";
                    int port = 1234;

                    String method = gui.getMethodComboBox().getSelectedItem().toString();
                    String path = "/snake";
                    String[] headers = { "Content-Type: text/plain" };

                    String body = "";
                    
                    body = gui.getBodyArea().getText().trim();

                    RequestCreator creator = new RequestCreator();
                    String request = creator.makeRequest(method, path, headers, body);

                    Socket socket = new Socket(host, port);
                    BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);

                    salida.println(request);
                    salida.println(); // línea vacía para indicar fin del mensaje
                    salida.flush();

                    StringBuilder rawResponse = new StringBuilder();
                    String line;
                    while ((line = entrada.readLine()) != null) {
                        rawResponse.append(line).append("\r\n");
                    }

                    gui.getResponseArea().setText(rawResponse.toString());

                    socket.close();

                } catch (Exception e) {
                    gui.getResponseArea().setText("Error: " + e.getMessage());
                }
            });
        });
    }
}
