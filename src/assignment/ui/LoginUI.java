package assignment.ui;

import assignment.model.Professor;
import assignment.model.Student;
import assignment.service.Upyogkarta;

import javax.swing.*;
import java.awt.*;

public class LoginUI extends JFrame {

    JTextField emailField;
    JPasswordField passwordField;
    JButton login, signup, adminLogin , professorLogin;

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
        adminLogin = new JButton("Admin Login");

        add(emailLabel);
        add(emailField);
        add(passLabel);
        add(passwordField);
        add(login);
        add(signup);
        add(adminLogin);

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


        adminLogin.addActionListener(e -> {

            String pass = JOptionPane.showInputDialog("Enter Admin Password:");

            if ("admin123".equals(pass)) {
                new AdminUI();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Wrong Password!");
            }
        });

        professorLogin.addActionListener(e -> {

            String name = JOptionPane.showInputDialog("Enter Professor Name:");

            Professor professor = new Professor(name);
            new ProfessorUI(professor);
            dispose();
        });
    }
}