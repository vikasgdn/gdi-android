package com.gdi.model.filter;

import java.util.ArrayList;

public class LocationFilterRootObject {

    boolean error;
    ArrayList<FilterLocationInfo> data;
    String message = "";

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public ArrayList<FilterLocationInfo> getData() {
        return data;
    }

    public void setData(ArrayList<FilterLocationInfo> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
