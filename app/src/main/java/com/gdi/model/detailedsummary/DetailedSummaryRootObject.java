package com.gdi.model.detailedsummary;

public class DetailedSummaryRootObject {

    boolean error;
    DetailedSummaryInfo data;
    String message = "";

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public DetailedSummaryInfo getData() {
        return data;
    }

    public void setData(DetailedSummaryInfo data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
