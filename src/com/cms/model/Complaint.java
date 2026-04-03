package com.cms.model;

import com.cms.model.*;
import com.cms.ui.*;
import com.cms.data.*;
import com.cms.service.*;

public class Complaint {
    private String description;
    private String status;

    public Complaint(String description) {
        this.description = description;
        this.status = "Pending"; // default
    }

    public String getDescription() { return description; }
    public String getStatus() { return status; }

    public void setStatus(String status) {
        this.status = status;
    }

    public String toString() {
        return description + " [" + status + "]";
    }
}
