package com.gdi.model.dashboard;

public class IADashboardRootObject {

    boolean error;
    IADashboardInfo data;
    String message = "";

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public IADashboardInfo getData() {
        return data;
    }

    public void setData(IADashboardInfo data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
