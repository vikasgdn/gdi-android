package com.gdi.model.filter;

import java.util.ArrayList;

public class FilterInfo {

    ArrayList<BrandsInfo> brands;
    ArrayList<CampaignsInfo> campaigns;
    ArrayList<FilterCountryInfo> country;
    ArrayList<FilterCityInfo> city;
    ArrayList<FilterLocationInfo> locations;

    public ArrayList<BrandsInfo> getBrands() {
        return brands;
    }

    public void setBrands(ArrayList<BrandsInfo> brands) {
        this.brands = brands;
    }

    public ArrayList<CampaignsInfo> getCampaigns() {
        return campaigns;
    }

    public void setCampaigns(ArrayList<CampaignsInfo> campaigns) {
        this.campaigns = campaigns;
    }

    public ArrayList<FilterCountryInfo> getCountry() {
        return country;
    }

    public void setCountry(ArrayList<FilterCountryInfo> country) {
        this.country = country;
    }

    public ArrayList<FilterCityInfo> getCity() {
        return city;
    }

    public void setCity(ArrayList<FilterCityInfo> city) {
        this.city = city;
    }

    public ArrayList<FilterLocationInfo> getLocations() {
        return locations;
    }

    public void setLocations(ArrayList<FilterLocationInfo> locations) {
        this.locations = locations;
    }
}
