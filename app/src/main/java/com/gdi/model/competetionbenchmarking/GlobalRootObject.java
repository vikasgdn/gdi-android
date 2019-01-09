package com.gdi.model.competetionbenchmarking;

public class GlobalRootObject {

    boolean error;
    GlobalInfo data;
    String message = "";

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public GlobalInfo getData() {
        return data;
    }

    public void setData(GlobalInfo data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
