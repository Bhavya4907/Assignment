package com.cms.ui;

import com.cms.model.*;
import com.cms.ui.*;
import com.cms.data.*;
import com.cms.service.*;

import javax.swing.*;

public class UI {
    public static void main(String[] args) {
        JFrame jframe = new JFrame("Login As:");

        JButton student = new JButton("Student");
        student.setBounds(100, 100, 150, 40);

        jframe.add(student);
        jframe.setSize(400, 400);
        jframe.setLayout(null);
        jframe.setVisible(true);

        JButton admin = new JButton("Administrator");
        admin.setBounds(100, 160, 150, 40);

        jframe.add(admin);

        student.addActionListener(e -> new LoginUI());
        admin.addActionListener(e -> new AdminUI());
    }
}
