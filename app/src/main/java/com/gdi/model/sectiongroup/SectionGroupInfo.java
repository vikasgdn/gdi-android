package com.gdi.model.sectiongroup;

import android.os.Parcel;
import android.os.Parcelable;

import com.gdi.model.ReportUrlInfo;

import java.util.ArrayList;

public class SectionGroupInfo {

    ArrayList<SectionGroupModel> section_groups;
    ArrayList<SectionGroupLocation> locations;
    String avg_score = "";
    ReportUrlInfo report_urls;

    public ArrayList<SectionGroupModel> getSection_groups() {
        return section_groups;
    }

    public void setSection_groups(ArrayList<SectionGroupModel> section_groups) {
        this.section_groups = section_groups;
    }

    public ArrayList<SectionGroupLocation> getLocations() {
        return locations;
    }

    public void setLocations(ArrayList<SectionGroupLocation> locations) {
        this.locations = locations;
    }

    public String getAvg_score() {
        return avg_score;
    }

    public void setAvg_score(String avg_score) {
        this.avg_score = avg_score;
    }

    public ReportUrlInfo getReport_urls() {
        return report_urls;
    }

    public void setReport_urls(ReportUrlInfo report_urls) {
        this.report_urls = report_urls;
    }
}
