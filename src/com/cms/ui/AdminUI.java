package com.cms.ui;

import com.cms.model.*;
import com.cms.ui.*;
import com.cms.data.*;
import com.cms.service.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import com.cms.model.Course;
import com.cms.service.SystemData;

public class AdminUI extends JFrame {

    JButton manageCourses;

    public AdminUI() {
        setTitle("Admin Panel");
        setLayout(new GridLayout(4,1,10,10));

        manageCourses = new JButton("Manage Courses");
        add(manageCourses);

        setSize(400,400);
        setVisible(true);

        manageCourses.addActionListener(e -> {
            String[] options = {"View","Add","Delete"};
            int choice = JOptionPane.showOptionDialog(this,"Choose",
                    "Courses",0,0,null,options,options[0]);

            if(choice==0){
                StringBuilder sb=new StringBuilder();
                for(Course c:SystemData.courses)
                    sb.append(c).append("\n");
                JOptionPane.showMessageDialog(this,sb.toString());
            }
        });
    }
}