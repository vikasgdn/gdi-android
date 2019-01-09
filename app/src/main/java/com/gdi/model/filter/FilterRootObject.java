package com.gdi.model.filter;

public class FilterRootObject {

    boolean error;
    FilterInfo data;
    String message = "";

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public FilterInfo getData() {
        return data;
    }

    public void setData(FilterInfo data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
