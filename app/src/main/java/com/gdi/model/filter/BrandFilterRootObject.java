package com.gdi.model.filter;

import java.util.ArrayList;

public class BrandFilterRootObject {

    boolean error;
    ArrayList<BrandsInfo> data;
    String message = "";

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public ArrayList<BrandsInfo> getData() {
        return data;
    }

    public void setData(ArrayList<BrandsInfo> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
