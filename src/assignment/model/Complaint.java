package assignment.model;

import java.time.LocalDateTime;

public class Complaint {

    private String description;
    private String status;
    private LocalDateTime date; // for tem

    public Complaint(String description) {
        this.description = description;
        this.status = "Pending";
        this.date = LocalDateTime.now();
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return description + " [" + status + "] - " + date;
    }
}