package assignment.ui;

import assignment.model.TeachingAssistant;
import assignment.service.Upyogkarta;

import javax.swing.*;


public class TALoginUI {

    public TALoginUI() {
        JFrame frame = new JFrame("TA Login");

        JLabel label = new JLabel("Enter TA ID:");
        JTextField field = new JTextField();
        JButton login = new JButton("Login");

        label.setBounds(50, 50, 100, 30);
        field.setBounds(150, 50, 150, 30);
        login.setBounds(120, 100, 100, 30);

        frame.add(label);
        frame.add(field);
        frame.add(login);

        frame.setSize(350, 250);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        login.addActionListener(e -> {
            String id = field.getText();

            Upyogkarta service = new Upyogkarta();
            TeachingAssistant ta = service.validateTA(id);

            if (ta != null) {
                JOptionPane.showMessageDialog(frame, "TA Login Successful");
                new TAUI();
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid TA ID");
            }
        });
    }
}