package com.gdi.model.reportintegrity;

import java.util.ArrayList;

public class IntegrityRootObject {

    boolean error;
    ArrayList<IntegrityInfo> data;
    String message = "";

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public ArrayList<IntegrityInfo> getData() {
        return data;
    }

    public void setData(ArrayList<IntegrityInfo> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
