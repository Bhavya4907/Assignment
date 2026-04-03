package com.cms.service;

import com.cms.model.*;
import com.cms.ui.*;
import com.cms.data.*;


import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class Upyogkarta {
    private Map<String , String> users = new HashMap<>();
    private Map<String , Student> students = new HashMap<>();

    public Upyogkarta() {
        loadUsers();
    }

    private void loadUsers() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("src/user.txt"));

            String line;

            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split(",");
                users.put(parts[0], parts[1]);
                students.put(parts[0], new Student(parts[2]));

                SystemData.students.add(new Student(parts[2]));
            }

            bufferedReader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Student validate(String email, String password) {
        if (users.containsKey(email) && users.get(email).equals(password)) {
            return students.get(email);
        }
        return null;
    }
}
