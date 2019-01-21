package com.gdi.model.locationcampaign;

import com.gdi.model.ReportUrlInfo;
import com.gdi.model.trendlocation.TrendLocationInfo;

import java.util.ArrayList;

public class LocationCampaignRootObject {

    boolean error;
    ArrayList<LocationCampaignInfo> data;
    String message = "";
    ReportUrlInfo report_urls;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public ArrayList<LocationCampaignInfo> getData() {
        return data;
    }

    public void setData(ArrayList<LocationCampaignInfo> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ReportUrlInfo getReport_urls() {
        return report_urls;
    }

    public void setReport_urls(ReportUrlInfo report_urls) {
        this.report_urls = report_urls;
    }
}
