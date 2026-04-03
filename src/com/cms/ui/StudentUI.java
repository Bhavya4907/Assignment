package com.cms.ui;

import com.cms.model.*;
import com.cms.ui.*;
import com.cms.data.*;
import com.cms.service.*;

import javax.swing.*;
import java.awt.*;
import com.cms.model.*;
import com.cms.service.SystemData;
import com.cms.data.loc;

public class StudentUI extends JFrame {
    Student student;

    JButton view , reg , schedule, drop , complaint , out;

    public StudentUI() {
        setTitle("Student Dashboard");

        view = new JButton("View Courses");
        schedule = new JButton("View Schedule");
        reg = new JButton("Register Course");
        drop = new JButton("Drop Course");
        complaint = new JButton("Submit Complaint");
        out = new JButton("Logout");

        setLayout(new GridLayout(6, 1, 10, 10));

        add(view); add(schedule); add(reg);
        add(drop); add(complaint); add(out);

        setSize(400, 400);
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
            String[] cols = {"Day","Time","Room"};
            String[][] data = new String[l.schedules.size()][3];

            for (int i=0;i<l.schedules.size();i++){
                Schedule s = l.schedules.get(i);
                data[i][0]=s.getDay();
                data[i][1]=s.getTime();
                data[i][2]=s.getLocation();
            }

            JTable table = new JTable(data, cols);
            JFrame f = new JFrame("Schedule");
            f.add(new JScrollPane(table));
            f.setSize(500,300);
            f.setVisible(true);
        });

        complaint.addActionListener(e->{
            String desc = JOptionPane.showInputDialog(this,"Enter complaint");
            Complaint c = new Complaint(desc);
            student.complaint.add(String.valueOf(c));
        });
    }
}