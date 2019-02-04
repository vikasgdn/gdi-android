package com.gdi.model.filter;

import java.util.ArrayList;

public class FilterInfo {

    ArrayList<BrandsInfo> brands;
    ArrayList<CampaignsInfo> campaigns;
    ArrayList<CountryInfo> country;
    ArrayList<CityInfo> city;
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

    public ArrayList<CountryInfo> getCountry() {
        return country;
    }

    public void setCountry(ArrayList<CountryInfo> country) {
        this.country = country;
    }

    public ArrayList<CityInfo> getCity() {
        return city;
    }

    public void setCity(ArrayList<CityInfo> city) {
        this.city = city;
    }

    public ArrayList<FilterLocationInfo> getLocations() {
        return locations;
    }

    public void setLocations(ArrayList<FilterLocationInfo> locations) {
        this.locations = locations;
    }
}
