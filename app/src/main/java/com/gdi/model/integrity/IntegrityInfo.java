package com.gdi.model.integrity;

import com.gdi.model.ReportUrlInfo;

import java.util.ArrayList;

public class IntegrityInfo {

    String brand_name = "";
    String location_name = "";
    String city_name = "";
    ArrayList<IntegrityModel> integrity;
    ReportUrlInfo report_urls;

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

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

    public ArrayList<IntegrityModel> getIntegrity() {
        return integrity;
    }

    public void setIntegrity(ArrayList<IntegrityModel> integrity) {
        this.integrity = integrity;
    }

    public ReportUrlInfo getReport_urls() {
        return report_urls;
    }

    public void setReport_urls(ReportUrlInfo report_urls) {
        this.report_urls = report_urls;
    }
}
