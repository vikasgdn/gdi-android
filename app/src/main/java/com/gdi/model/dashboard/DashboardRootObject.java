package com.gdi.model.dashboard;

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
