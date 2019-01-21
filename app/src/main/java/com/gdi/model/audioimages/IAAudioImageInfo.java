package com.gdi.model.audioimages;

import com.gdi.model.ReportUrlInfo;

import java.util.ArrayList;

public class IAAudioImageInfo {

    String location_name;
    String city;
    ArrayList<IASectionAudioImage> sections;
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

    public ArrayList<IASectionAudioImage> getSections() {
        return sections;
    }

    public void setSections(ArrayList<IASectionAudioImage> sections) {
        this.sections = sections;
    }

    public ReportUrlInfo getReport_urls() {
        return report_urls;
    }

    public void setReport_urls(ReportUrlInfo report_urls) {
        this.report_urls = report_urls;
    }
}
