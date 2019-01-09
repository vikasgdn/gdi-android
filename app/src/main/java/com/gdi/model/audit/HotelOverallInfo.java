package com.gdi.model.audit;

import com.gdi.model.ReportUrlInfo;

public class HotelOverallInfo {

    String score = "";
    String city = "";
    String country = "";
    ReportUrlInfo report_urls;

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
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

    public ReportUrlInfo getReport_urls() {
        return report_urls;
    }

    public void setReport_urls(ReportUrlInfo report_urls) {
        this.report_urls = report_urls;
    }
}
