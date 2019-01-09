package com.gdi.model.competetionbenchmarking;

public class CityCompsetRootObject {

    boolean error;
    CityCompsetInfo data;
    String message = "";

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public CityCompsetInfo getData() {
        return data;
    }

    public void setData(CityCompsetInfo data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
