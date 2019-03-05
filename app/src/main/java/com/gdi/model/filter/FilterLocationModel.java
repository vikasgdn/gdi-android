package com.gdi.model.filter;

import java.util.ArrayList;

public class FilterLocationModel {

    ArrayList<FilterLocationInfo> locations ;
    ArrayList<FilterCountryInfo> countries ;
    ArrayList<FilterCityInfo> cities ;

    public ArrayList<FilterLocationInfo> getLocations() {
        return locations;
    }

    public void setLocations(ArrayList<FilterLocationInfo> locations) {
        this.locations = locations;
    }

    public ArrayList<FilterCountryInfo> getCountries() {
        return countries;
    }

    public void setCountries(ArrayList<FilterCountryInfo> countries) {
        this.countries = countries;
    }

    public ArrayList<FilterCityInfo> getCities() {
        return cities;
    }

    public void setCities(ArrayList<FilterCityInfo> cities) {
        this.cities = cities;
    }
}
