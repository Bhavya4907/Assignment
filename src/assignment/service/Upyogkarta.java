package assignment.service;

import assignment.data.*;
import assignment.model.*;
import assignment.ui.*;
import assignment.service.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class Upyogkarta {

    public Student validateStudent(String email, String password) {

        try {
            Connection connection = DBConnection.getConnection();

            String query = "SELECT * FROM users WHERE email=? AND password=? AND role='student'";
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return new Student(resultSet.getString("branch"));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return null;
    }

    public boolean registerStudent(String email, String password, String branch) {
        try {
            Connection connection = DBConnection.getConnection();

            String query = "INSERT INTO users(email, password, role, branch) VALUES (?, ?, 'student', ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, branch);

            preparedStatement.executeUpdate();
            return true;
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public Professor validateProfessor(String email, String password) {

        try {
            Connection con = DBConnection.getConnection();

            String query = "SELECT * FROM users WHERE email=? AND password=? AND role='professor'";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Professor(rs.getString("email")); // using email as name
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}


