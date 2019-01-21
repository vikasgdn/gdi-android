package com.gdi.model.locationcampaign;

import com.gdi.model.trendlocation.TrendLocationRound;

import java.util.ArrayList;

public class LocationCampaignInfo {

    String location = "";
    String country = "";
    String city = "";
    String general_manager = "";
    String brand = "";
    String avg_score = "";
    ArrayList<LocationCampaignRound> rounds;
    int rank = 0;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getGeneral_manager() {
        return general_manager;
    }

    public void setGeneral_manager(String general_manager) {
        this.general_manager = general_manager;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getAvg_score() {
        return avg_score;
    }

    public void setAvg_score(String avg_score) {
        this.avg_score = avg_score;
    }

    public ArrayList<LocationCampaignRound> getRounds() {
        return rounds;
    }

    public void setRounds(ArrayList<LocationCampaignRound> rounds) {
        this.rounds = rounds;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
