package com.gdi.model.executivesummary;

import com.gdi.model.ReportUrlInfo;

import java.util.ArrayList;

public class ExecutiveLocationsInfo {

    private String location_name = "";
    private String city = "";
    private String country = "";
    private String score = "";
    private String summary = "";
    ArrayList<ExecutiveAttachmentsInfo> attachments;
    ReportUrlInfo report_urls;

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public ArrayList<ExecutiveAttachmentsInfo> getAttachments() {
        return attachments;
    }

    public void setAttachments(ArrayList<ExecutiveAttachmentsInfo> attachments) {
        this.attachments = attachments;
    }

    public ReportUrlInfo getReport_urls() {
        return report_urls;
    }

    public void setReport_urls(ReportUrlInfo report_urls) {
        this.report_urls = report_urls;
    }
}
