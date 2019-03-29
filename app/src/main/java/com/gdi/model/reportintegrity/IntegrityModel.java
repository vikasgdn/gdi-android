package com.gdi.model.reportintegrity;

import java.util.ArrayList;

public class IntegrityModel {

    String name = "";
    String date = "";
    String time = "";
    String staff_name = "";
    String summary = "";
    ArrayList<IntegrityAttachment> attachments;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStaff_name() {
        return staff_name;
    }

    public void setStaff_name(String staff_name) {
        this.staff_name = staff_name;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public ArrayList<IntegrityAttachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(ArrayList<IntegrityAttachment> attachments) {
        this.attachments = attachments;
    }
}
