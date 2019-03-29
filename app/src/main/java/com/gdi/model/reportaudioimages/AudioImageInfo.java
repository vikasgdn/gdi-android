package com.gdi.model.reportaudioimages;

import com.gdi.model.ReportUrlInfo;

import java.util.ArrayList;

public class AudioImageInfo {

    String location_name;
    String city_name;
    ArrayList<SectionAudioImage> sections;
    ReportUrlInfo report_urls;
    boolean isExpand = false;

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

    public ArrayList<SectionAudioImage> getSections() {
        return sections;
    }

    public void setSections(ArrayList<SectionAudioImage> sections) {
        this.sections = sections;
    }

    public ReportUrlInfo getReport_urls() {
        return report_urls;
    }

    public void setReport_urls(ReportUrlInfo report_urls) {
        this.report_urls = report_urls;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
    }
}
