package assignment.service;

import assignment.model.*;

import java.sql.*;

public class Upyogkarta {

    public Student validateStudent(String email, String password) {
        try (Connection con = DBConnection.getConnection()) {
            String query = "SELECT * FROM users WHERE email=? AND password=? AND role='student'";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String name   = rs.getString("name") != null ? rs.getString("name") : email;
                String id     = String.valueOf(rs.getInt("id"));
                String branch = rs.getString("branch");

                Student student = new Student(name, id, branch);
                student.dbId  = rs.getInt("id");
                student.email = email;

                Registration.restoreStudentRegistrations(student);
                return student;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean registerStudent(String email, String password, String branch) {
        try (Connection con = DBConnection.getConnection()) {
            String query = "INSERT INTO users(email, password, role, branch) VALUES (?, ?, 'student', ?)";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, email);
            ps.setString(2, password);
            ps.setString(3, branch);
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Professor validateProfessor(String email, String password) {
        try (Connection con = DBConnection.getConnection()) {
            String query = "SELECT * FROM users WHERE email=? AND password=? AND role='professor'";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name") != null ? rs.getString("name") : email;
                Professor prof = new Professor(name, email);
                prof.dbId = rs.getInt("id");
                return prof;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean registerProfessor(String email, String password, String name) {
        try (Connection con = DBConnection.getConnection()) {
            String query = "INSERT INTO users(name, email, password, role) VALUES (?, ?, ?, 'professor')";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void loadAllStudents() {
        SystemData.students.clear();
        try (Connection con = DBConnection.getConnection()) {
            String query = "SELECT * FROM users WHERE role='student'";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String name   = rs.getString("name") != null ? rs.getString("name") : rs.getString("email");
                String id     = String.valueOf(rs.getInt("id"));
                String branch = rs.getString("branch");
                String email  = rs.getString("email");

                Student s = new Student(name, id, branch);
                s.dbId  = rs.getInt("id");
                s.email = email;

                Registration.restoreStudentRegistrations(s);
                SystemData.students.add(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public TeachingAssistant validateTA(String id) {
        try (Connection con = DBConnection.getConnection()) {

            String query = "SELECT * FROM users WHERE id=? AND role='ta'";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, Integer.parseInt(id));

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String name   = rs.getString("name");
                String branch = rs.getString("branch");

                TeachingAssistant ta = new TeachingAssistant(name, id, branch);

                ta.dbId  = rs.getInt("id");
                ta.email = rs.getString("email");

                return ta;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean registerTA(String name, String email, String password, String branch) {
        try (Connection con = DBConnection.getConnection()) {

            String query = "INSERT INTO users(name, email, password, role, branch) VALUES (?, ?, ?, 'ta', ?)";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.setString(4, branch);

            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}