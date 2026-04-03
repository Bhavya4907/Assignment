package com.cms.ui;

import com.cms.model.*;
import com.cms.ui.*;
import com.cms.data.*;
import com.cms.service.*;

import javax.swing.*;
import java.awt.*;
import com.cms.service.Upyogkarta;
import com.cms.model.Student;

public class LoginUI extends JFrame {
    JTextField emailField;
    JPasswordField passwordField;
    JButton login;

    public LoginUI(){
        setTitle("Student Login");
        setSize(300, 200);
        setLayout(new GridLayout(3, 2, 10, 10));

        JLabel emailLabel = new JLabel("Email :");
        JLabel passLabel = new JLabel("Password :");

        emailField = new JTextField();
        passwordField = new JPasswordField();
        login = new JButton("Login");

        add(emailLabel); add(emailField);
        add(passLabel); add(passwordField);
        add(new JLabel()); add(login);

        setLocationRelativeTo(null);
        setVisible(true);

        login.addActionListener(e -> {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());

            Upyogkarta u = new Upyogkarta();
            Student student = u.validate(email, password);

            if (student != null) new StudentUI();
            else JOptionPane.showMessageDialog(this, "Invalid Credentials");
        });
    }
}