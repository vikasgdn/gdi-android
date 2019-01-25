package com.gdi.model.trendlocation;

import java.util.ArrayList;

public class TrendLocationModel {

    String location = "";
    String country = "";
    String city = "";
    String general_manager = "";
    ArrayList<TrendLocationRound> rounds;
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

    public ArrayList<TrendLocationRound> getRounds() {
        return rounds;
    }

    public void setRounds(ArrayList<TrendLocationRound> rounds) {
        this.rounds = rounds;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
