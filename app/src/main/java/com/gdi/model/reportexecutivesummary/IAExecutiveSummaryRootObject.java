package com.gdi.model.reportexecutivesummary;


import java.util.ArrayList;

public class IAExecutiveSummaryRootObject {

    boolean error;
    ArrayList<ExecutiveLocationsInfo> data;
    String message = "";

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public ArrayList<ExecutiveLocationsInfo> getData() {
        return data;
    }

    public void setData(ArrayList<ExecutiveLocationsInfo> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
