package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientGUI extends JFrame {
    private JComboBox<String> methodComboBox;
    private JTextArea bodyArea;
    private JTextArea headArea;
    private JScrollPane bodyScrollPane;
    private JTextArea responseArea;
    private JButton sendButton;
    private JLabel bodyHelpLabel;

    public ClientGUI() {
        setTitle("HTTP Client");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel topPanel = new JPanel(new GridLayout(2, 2));
        topPanel.add(new JLabel("Method:"));
        String[] methods = { "GET", "POST", "HEAD", "PUT", "DELETE" };
        methodComboBox = new JComboBox<>(methods);
        topPanel.add(methodComboBox);

        bodyHelpLabel = new JLabel(" ");
        topPanel.add(new JLabel("Instructions:"));
        topPanel.add(bodyHelpLabel);

        panel.add(topPanel, BorderLayout.NORTH);

        bodyArea = new JTextArea();
        bodyScrollPane = new JScrollPane(bodyArea);
        panel.add(bodyScrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());

        sendButton = new JButton("Send");
        bottomPanel.add(sendButton, BorderLayout.NORTH);

        responseArea = new JTextArea();
        responseArea.setEditable(false);
        bottomPanel.add(new JScrollPane(responseArea), BorderLayout.CENTER);

        panel.add(bottomPanel, BorderLayout.SOUTH);

        methodComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateBodyVisibility();
            }
        });

        updateBodyVisibility();

        setContentPane(panel);
    }

    private void updateBodyVisibility() {
        String selected = methodComboBox.getSelectedItem().toString();
        boolean needsBody = true;
        bodyScrollPane.setVisible(needsBody);
        bodyArea.setText("");

        switch (selected) {
            case "GET":
                bodyHelpLabel.setText("Optional: Attribute,value");
                bodyArea.setText("");
                break;
            case "POST":
                bodyHelpLabel.setText("Format: Snake,name,attr1,attr2");
                bodyArea.setText("Snake,Bob,big,green");
                break;
            case "PUT":
                bodyHelpLabel.setText("Format: Snake,name,newAttr1,newAttr2");
                bodyArea.setText("Snake,Bob,small,red");
                break;
            case "DELETE":
                bodyHelpLabel.setText("Format: Attribute,name");
                bodyArea.setText("Attribute,Bob");
                break;
            case "HEAD":
                bodyHelpLabel.setText("Optional: Attribute,value");
                bodyArea.setText("");
                break;
        }

        revalidate();
        repaint();
    }

    public JComboBox<String> getMethodComboBox() {
        return methodComboBox;
    }

    public JTextArea getBodyArea() {
        return bodyArea;
    }

    public JTextArea getResponseArea() {
        return responseArea;
    }

    public JButton getSendButton() {
        return sendButton;
    }
}
