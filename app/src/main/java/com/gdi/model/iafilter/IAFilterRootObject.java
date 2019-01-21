package com.gdi.model.iafilter;


public class IAFilterRootObject {

    boolean error;
    IAFilterInfo data;
    String message = "";

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public IAFilterInfo getData() {
        return data;
    }

    public void setData(IAFilterInfo data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
