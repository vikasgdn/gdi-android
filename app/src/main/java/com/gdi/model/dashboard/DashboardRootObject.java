package com.gdi.model.dashboard;

import com.gdi.model.detailedsummary.DetailedSummaryInfo;

public class DashboardRootObject {

    boolean error;
    DashboardInfo data;
    String message = "";

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public DashboardInfo getData() {
        return data;
    }

    public void setData(DashboardInfo data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
