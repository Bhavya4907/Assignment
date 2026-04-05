package assignment.ui;

import assignment.model.Professor;
import assignment.service.Upyogkarta;

import javax.swing.*;
import java.awt.*;

public class ProfessorLoginUI extends JFrame {

    JTextField emailField;
    JPasswordField passwordField;
    JButton login;

    public ProfessorLoginUI() {

        setTitle("Professor Login");

        setSize(300, 200);
        setLayout(new GridLayout(3, 2, 10, 10));

        JLabel emailLabel = new JLabel("Email:");
        JLabel passLabel = new JLabel("Password:");

        emailField = new JTextField();
        passwordField = new JPasswordField();

        login = new JButton("Login");

        add(emailLabel);
        add(emailField);
        add(passLabel);
        add(passwordField);
        add(new JLabel());
        add(login);

        setLocationRelativeTo(null);
        setVisible(true);

        Upyogkarta u = new Upyogkarta();

        login.addActionListener(e -> {

            String email = emailField.getText();
            String password = new String(passwordField.getPassword());

            Professor prof = u.validateProfessor(email, password);

            if (prof != null) {
                new ProfessorUI(prof);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Credentials");
            }
        });
    }
}
