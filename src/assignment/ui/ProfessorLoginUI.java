package assignment.ui;

import assignment.model.Professor;
import assignment.service.Upyogkarta;

import javax.swing.*;
import java.awt.*;

public class ProfessorLoginUI extends JFrame {

    JTextField emailField;
    JPasswordField passwordField;
    JButton login, signup;

    public ProfessorLoginUI() {

        setTitle("Professor Login");
        setSize(300, 220);
        setLayout(new GridLayout(4, 2, 10, 10));

        JLabel emailLabel = new JLabel("Email:");
        JLabel passLabel  = new JLabel("Password:");

        emailField    = new JTextField();
        passwordField = new JPasswordField();
        login         = new JButton("Login");
        signup        = new JButton("Sign Up");

        add(emailLabel);    add(emailField);
        add(passLabel);     add(passwordField);
        add(login);         add(signup);

        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);

        Upyogkarta u = new Upyogkarta();

        login.addActionListener(e -> {
            String email    = emailField.getText();
            String password = new String(passwordField.getPassword());

            Professor prof = u.validateProfessor(email, password);

            if (prof != null) {
                new ProfessorUI(prof);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Credentials");
            }
        });

        signup.addActionListener(e -> {
            String email    = JOptionPane.showInputDialog(this, "Enter Email:");
            if (email == null || email.trim().isEmpty()) return;

            String password = JOptionPane.showInputDialog(this, "Enter Password:");
            if (password == null || password.trim().isEmpty()) return;

            String name     = JOptionPane.showInputDialog(this, "Enter Full Name:");
            if (name == null || name.trim().isEmpty()) return;

            if (u.registerProfessor(email.trim(), password.trim(), name.trim())) {
                JOptionPane.showMessageDialog(this, "Registered successfully! You can now log in.");
            } else {
                JOptionPane.showMessageDialog(this, "Registration failed. Email may already be taken.");
            }
        });
    }
}