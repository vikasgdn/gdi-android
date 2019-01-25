package com.gdi.model.locationcampaign;

import com.gdi.model.ReportUrlInfo;

import java.util.ArrayList;

public class LocationCampaignInfo {

    ArrayList<LocationCampaignModel> locations;
    ArrayList<LocationCampaignRound2> rounds;
    ReportUrlInfo report_urls;

    public ArrayList<LocationCampaignModel> getLocations() {
        return locations;
    }

    public void setLocations(ArrayList<LocationCampaignModel> locations) {
        this.locations = locations;
    }

    public ArrayList<LocationCampaignRound2> getRounds() {
        return rounds;
    }

    public void setRounds(ArrayList<LocationCampaignRound2> rounds) {
        this.rounds = rounds;
    }

    public ReportUrlInfo getReport_urls() {
        return report_urls;
    }

    public void setReport_urls(ReportUrlInfo report_urls) {
        this.report_urls = report_urls;
    }
}
