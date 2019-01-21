package com.gdi.model.backhouse;

import com.gdi.model.ReportUrlInfo;

import java.util.ArrayList;

public class BackHouseInfo {

    String location_name;
    String city_name;
    ReportUrlInfo report_urls;
    ArrayList<BackHouseQuestion> questions;

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public ReportUrlInfo getReport_urls() {
        return report_urls;
    }

    public void setReport_urls(ReportUrlInfo report_urls) {
        this.report_urls = report_urls;
    }

    public ArrayList<BackHouseQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<BackHouseQuestion> questions) {
        this.questions = questions;
    }
}
