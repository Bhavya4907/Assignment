package assignment.ui;

import assignment.model.Professor;
import assignment.model.Student;
import assignment.service.Upyogkarta;

import javax.swing.*;
import java.awt.*;

public class LoginUI extends JFrame {

    JTextField emailField;
    JPasswordField passwordField;
    JButton login, signup;

    public LoginUI() {

        setTitle("Login");
        setSize(350, 250);
        setLayout(new GridLayout(4, 2, 10, 10));

        JLabel emailLabel = new JLabel("Email:");
        JLabel passLabel = new JLabel("Password:");

        emailField = new JTextField();
        passwordField = new JPasswordField();

        login = new JButton("Login");
        signup = new JButton("Sign Up");

        add(emailLabel);
        add(emailField);
        add(passLabel);
        add(passwordField);
        add(login);
        add(signup);

        setLocationRelativeTo(null);
        setVisible(true);

        Upyogkarta u = new Upyogkarta();

        login.addActionListener(e -> {

            String email = emailField.getText();
            String password = new String(passwordField.getPassword());

            Student student = u.validateStudent(email, password);

            if (student != null) {
                new StudentUI(student);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Credentials");
            }
        });


        signup.addActionListener(e -> {

            String email = JOptionPane.showInputDialog("Enter Email:");
            String password = JOptionPane.showInputDialog("Enter Password:");
            String branch = JOptionPane.showInputDialog("Enter Branch:");

            if (u.registerStudent(email, password, branch)) {
                JOptionPane.showMessageDialog(this, "Registered Successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Error!");
            }
        });
    }
}