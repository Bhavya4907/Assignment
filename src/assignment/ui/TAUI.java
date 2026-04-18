package assignment.ui;

import javax.swing.*;

public class TAUI {

    public TAUI() {
        JFrame frame = new JFrame("TA Dashboard");

        JButton viewGrades = new JButton("View Grades");
        JButton updateGrades = new JButton("Update Grades");

        viewGrades.setBounds(100, 80, 150, 40);
        updateGrades.setBounds(100, 140, 150, 40);

        frame.add(viewGrades);
        frame.add(updateGrades);

        frame.setSize(400, 300);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        viewGrades.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "Viewing Grades...");
        });

        updateGrades.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "Updating Grades...");
        });
    }
}