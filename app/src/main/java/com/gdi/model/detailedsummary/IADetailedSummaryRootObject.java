package com.gdi.model.detailedsummary;

import java.util.ArrayList;

public class IADetailedSummaryRootObject {

    boolean error;
    ArrayList<LocationInfo> data;
    String message = "";

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public ArrayList<LocationInfo> getData() {
        return data;
    }

    public void setData(ArrayList<LocationInfo> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
