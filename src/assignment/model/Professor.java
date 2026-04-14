package assignment.model;

public class Professor {

    public String name;
    public String email;
    public String officeHours;
    public int dbId;

    public Professor(String name, String email) {
        this.name  = name;
        this.email = email;
    }

    @Override
    public String toString() {
        return name + " <" + email + ">";
    }
}