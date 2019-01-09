package com.gdi.model.competetionbenchmarking;

import com.gdi.model.ReportUrlInfo;

import java.util.ArrayList;

public class GlobalModel {

    String city_name;
    String audit_date;
    int total_rank;
    int hotel_rank;
    String average_score;
    String score;
    ArrayList<Ranking> ranking;
    ArrayList<String> hotels;
    ReportUrlInfo report_urls;

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getAudit_date() {
        return audit_date;
    }

    public void setAudit_date(String audit_date) {
        this.audit_date = audit_date;
    }

    public int getTotal_rank() {
        return total_rank;
    }

    public void setTotal_rank(int total_rank) {
        this.total_rank = total_rank;
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

    public ArrayList<String> getHotels() {
        return hotels;
    }

    public void setHotels(ArrayList<String> hotels) {
        this.hotels = hotels;
    }

    public ReportUrlInfo getReport_urls() {
        return report_urls;
    }

    public void setReport_urls(ReportUrlInfo report_urls) {
        this.report_urls = report_urls;
    }
}
