package com.gdi.model.competetionbenchmarking;

import com.gdi.model.ReportUrlInfo;

import java.util.ArrayList;

public class CityCompset {

    String location_name;
    String score;
    ArrayList<Ranking> ranking;
    ArrayList<String> locations;
    int hotel_rank;
    String average_score;
    ReportUrlInfo report_urls;

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public ArrayList<Ranking> getRanking() {
        return ranking;
    }

    public void setRanking(ArrayList<Ranking> ranking) {
        this.ranking = ranking;
    }

    public ArrayList<String> getLocations() {
        return locations;
    }

    public void setLocations(ArrayList<String> locations) {
        this.locations = locations;
    }

    public int getHotel_rank() {
        return hotel_rank;
    }

    public void setHotel_rank(int hotel_rank) {
        this.hotel_rank = hotel_rank;
    }

    public String getAverage_score() {
        return average_score;
    }

    public void setAverage_score(String average_score) {
        this.average_score = average_score;
    }

    public ReportUrlInfo getReport_urls() {
        return report_urls;
    }

    public void setReport_urls(ReportUrlInfo report_urls) {
        this.report_urls = report_urls;
    }
}
