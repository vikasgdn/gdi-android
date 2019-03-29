package com.gdi.model.reportdetailedsummary;

import com.gdi.model.ReportUrlInfo;

import java.util.ArrayList;

public class LocationInfo {

    String location_name = "";
    String city = "";
    String country = "";
    ReportUrlInfo report_urls;
    ArrayList<SectionGroupInfo> section_groups;
    boolean isExpand = false;

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

    public ReportUrlInfo getReport_urls() {
        return report_urls;
    }

    public void setReport_urls(ReportUrlInfo report_urls) {
        this.report_urls = report_urls;
    }

    public ArrayList<SectionGroupInfo> getSection_groups() {
        return section_groups;
    }

    public void setSection_groups(ArrayList<SectionGroupInfo> section_groups) {
        this.section_groups = section_groups;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
    }
}
