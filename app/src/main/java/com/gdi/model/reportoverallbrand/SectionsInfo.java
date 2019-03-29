package com.gdi.model.reportoverallbrand;

import java.util.ArrayList;

public class SectionsInfo {

    String section_name = "";
    String score = "";
    ArrayList<LocationsInfo> locations;

    public String getSection_name() {
        return section_name;
    }

    public void setSection_name(String section_name) {
        this.section_name = section_name;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public ArrayList<LocationsInfo> getLocations() {
        return locations;
    }

    public void setLocations(ArrayList<LocationsInfo> locations) {
        this.locations = locations;
    }
}
