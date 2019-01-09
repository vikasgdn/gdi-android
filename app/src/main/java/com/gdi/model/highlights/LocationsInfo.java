package com.gdi.model.highlights;

import com.gdi.model.ReportUrlInfo;

import java.util.ArrayList;

public class LocationsInfo {

    String location_name = "";
    String city = "";
    String country = "";
    ArrayList<QuestionsInfo> questions;
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

    public ArrayList<QuestionsInfo> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<QuestionsInfo> questions) {
        this.questions = questions;
    }

    public ReportUrlInfo getReport_urls() {
        return report_urls;
    }

    public void setReport_urls(ReportUrlInfo report_urls) {
        this.report_urls = report_urls;
    }
}
