package client;

import javax.swing.JOptionPane;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.text.ParseException;

public class ErrorHandler {

    /**
     * Maneja excepciones mostrando mensaje genérico según el tipo.
     */
    public static void handle(Exception e) {
        handle(e, null);
    }

    /**
     * Maneja excepciones mostrando mensaje personalizado si se proporciona.
     *
     * @param e       La excepción capturada.
     * @param context Mensaje personalizado (opcional).
     */
    public static void handle(Exception e, String context) {
        String defaultMessage;

         if (e instanceof NullPointerException) {
            defaultMessage = "An unexpected internal error occurred.";
        } else if (e instanceof IOException) {
            defaultMessage = "Input/output error occurred.";
        } else if (e instanceof ConnectException) {
            defaultMessage = "Could not connect to the server.";
        } else if (e instanceof SocketTimeoutException) {
            defaultMessage = "The connection to the server timed out.";
        } else if (e instanceof IllegalArgumentException) {
            defaultMessage = "Invalid argument provided.";
        } else if (e instanceof ParseException) {
            defaultMessage = "Failed to parse server response.";
        } else {
            defaultMessage = "An unknown error occurred.";
        }

        String messageToShow = context != null ? context : defaultMessage;
        System.err.println("[" + e.getClass().getSimpleName() + "] " + e.getMessage());
        showMessage(messageToShow + "\n" + e.getMessage(), "Error");
    }

    private static void showMessage(String message, String title) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
    }
}
