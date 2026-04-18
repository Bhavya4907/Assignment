package assignment.ui;

import assignment.service.Upyogkarta;

import javax.swing.*;

public class UI {

    public static void main(String[] args) {

        JFrame jframe = new JFrame("Login As:");

        JButton student = new JButton("Student");
        JButton admin = new JButton("Administrator");
        JButton professor = new JButton("Professor");
        JButton ta = new JButton("Teaching Assistant");

        student.setBounds(100, 80, 150, 40);
        admin.setBounds(100, 140, 150, 40);
        professor.setBounds(100, 200, 150, 40);
        ta.setBounds(100, 260, 150, 40);

        jframe.add(student);
        jframe.add(admin);
        jframe.add(professor);
        jframe.add(ta);

        jframe.setSize(400, 400);
        jframe.setLayout(null);
        jframe.setLocationRelativeTo(null);
        jframe.setVisible(true);

        student.addActionListener(e -> {
            new LoginUI();
        });

        Upyogkarta u = new Upyogkarta();
        u.registerTA("TA User", "ta1@gmail.com", "1234", "CSE");


        admin.addActionListener(e -> {

            String pass = JOptionPane.showInputDialog("Enter Admin Password:");

            if ("ridhi".equals(pass)) {
                new AdminUI();
            } else {
                JOptionPane.showMessageDialog(jframe, "Wrong Password!");
            }
        });

        professor.addActionListener(e -> {
            new ProfessorLoginUI();
        });

        ta.addActionListener(e -> {
            new TALoginUI();
        });
    }
}