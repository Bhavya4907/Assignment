package assignment.ui;

import assignment.data.*;
import assignment.model.*;
import assignment.service.*;

import javax.swing.*;
import java.awt.*;

public class StudentUI extends JFrame {

    Student student;

    JButton view, reg, schedule, drop, complaint, out;


    public StudentUI(Student student) {
        this.student = student;

        setTitle("Student Dashboard");

        view = new JButton("View Courses");
        schedule = new JButton("View Schedule");
        reg = new JButton("Register Course");
        drop = new JButton("Drop Course");
        complaint = new JButton("Submit Complaint");
        out = new JButton("Logout");

        setLayout(new GridLayout(6, 1, 10, 10));

        add(view);
        add(schedule);
        add(reg);
        add(drop);
        add(complaint);
        add(out);

        setSize(400, 400);
        setLocationRelativeTo(null);
        setVisible(true);

        view.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            for (Course c : SystemData.courses) {
                sb.append(c).append("\n");
            }
            JOptionPane.showMessageDialog(this, sb.toString());
        });

        schedule.addActionListener(e -> {
            loc l = new loc();

            String[] cols = {"Day", "Time", "Room"};
            String[][] data = new String[l.schedules.size()][3];

            for (int i = 0; i < l.schedules.size(); i++) {
                Schedule s = l.schedules.get(i);
                data[i][0] = s.getDay();
                data[i][1] = s.getTime();
                data[i][2] = s.getLocation();
            }

            JTable table = new JTable(data, cols);
            JFrame f = new JFrame("Schedule");
            f.add(new JScrollPane(table));
            f.setSize(500, 300);
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        });

        reg.addActionListener(e -> {

            try {
                String semInput = JOptionPane.showInputDialog(this,
                        "Enter Semester (Current: " + student.currentSemester + ")");

                int sem = Integer.parseInt(semInput);

                if (sem != student.currentSemester) {
                    JOptionPane.showMessageDialog(this,
                            "Only current semester allowed!");
                    return;
                }

                StringBuilder sb = new StringBuilder();

                for (Course c : SystemData.courses) {
                    if (c.getSemester() == sem) {
                        sb.append(c.getCode())
                                .append(" - ")
                                .append(c.getCredit())
                                .append(" credits\n");
                    }
                }

                String code = JOptionPane.showInputDialog(this,
                        "Available Courses:\n" + sb + "\nEnter course code:");

                Course selected = null;

                for (Course c : SystemData.courses) {
                    if (c.getCode().equalsIgnoreCase(code)) {
                        selected = c;
                        break;
                    }
                }

                if (selected == null) {
                    JOptionPane.showMessageDialog(this, "Invalid course!");
                    return;
                }

                if (student.registerCourse(selected)) {
                    JOptionPane.showMessageDialog(this, "Registered successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "Registration failed!");
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input!");
            }
        });


        drop.addActionListener(e -> {

            String code = JOptionPane.showInputDialog(this,
                    "Enter course code to drop:");

            Course selected = null;

            for (Course c : SystemData.courses) {
                if (c.getCode().equalsIgnoreCase(code)) {
                    selected = c;
                    break;
                }
            }

            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Invalid course!");
                return;
            }

            student.dropCourse(selected);
            JOptionPane.showMessageDialog(this, "Course dropped!");
        });


        complaint.addActionListener(e -> {
            String desc = JOptionPane.showInputDialog(this, "Enter complaint");

            if (desc != null && !desc.isEmpty()) {
                student.addComplaint(desc);
                JOptionPane.showMessageDialog(this, "Complaint submitted!");
            }
        });

        out.addActionListener(e -> {
            dispose();
        });
    }
}