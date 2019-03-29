package com.gdi.model.reportoverallbrand;

import com.gdi.model.ReportUrlInfo;

import java.util.ArrayList;

public class OverallInfo {

    ReportUrlInfo report_urls;
    ArrayList<LocationsInfo> locations;

    public ReportUrlInfo getReport_urls() {
        return report_urls;
    }

    public void setReport_urls(ReportUrlInfo report_urls) {
        this.report_urls = report_urls;
    }

    public ArrayList<LocationsInfo> getLocations() {
        return locations;
    }

    public void setLocations(ArrayList<LocationsInfo> locations) {
        this.locations = locations;
    }
}
