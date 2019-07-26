package com.gdi.model.dashboard;

import java.util.ArrayList;

public class AuditDashboardRootObject {

    boolean error;
    ArrayList<IAMainDashboardInfo> data;
    String message = "";

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public ArrayList<IAMainDashboardInfo> getData() {
        return data;
    }

    public void setData(ArrayList<IAMainDashboardInfo> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
