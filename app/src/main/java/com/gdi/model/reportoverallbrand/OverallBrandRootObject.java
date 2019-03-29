package com.gdi.model.reportoverallbrand;

public class OverallBrandRootObject {

    boolean error;
    OverallBrandInfo data;
    String message = "";

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public OverallBrandInfo getData() {
        return data;
    }

    public void setData(OverallBrandInfo data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
