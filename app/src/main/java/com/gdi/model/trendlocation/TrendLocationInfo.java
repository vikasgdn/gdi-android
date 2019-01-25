package com.gdi.model.trendlocation;

import com.gdi.model.ReportUrlInfo;

import java.util.ArrayList;

public class TrendLocationInfo {

    ArrayList<TrendLocationModel> locations;
    ArrayList<TrendLocationRound2> rounds;
    ReportUrlInfo report_urls;

    public ArrayList<TrendLocationModel> getLocations() {
        return locations;
    }

    public void setLocations(ArrayList<TrendLocationModel> locations) {
        this.locations = locations;
    }

    public ArrayList<TrendLocationRound2> getRounds() {
        return rounds;
    }

    public void setRounds(ArrayList<TrendLocationRound2> rounds) {
        this.rounds = rounds;
    }

    public ReportUrlInfo getReport_urls() {
        return report_urls;
    }

    public void setReport_urls(ReportUrlInfo report_urls) {
        this.report_urls = report_urls;
    }
}
