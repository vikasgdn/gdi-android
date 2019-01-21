package com.gdi.model.trendlocation;

import com.gdi.model.ReportUrlInfo;
import com.gdi.model.sectiongroup.SectionGroupInfo;

import java.util.ArrayList;

public class TrendLocationRootObject {

    boolean error;
    ArrayList<TrendLocationInfo> data;
    String message = "";
    ReportUrlInfo report_urls;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public ArrayList<TrendLocationInfo> getData() {
        return data;
    }

    public void setData(ArrayList<TrendLocationInfo> data) {
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
