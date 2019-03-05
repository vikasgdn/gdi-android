package com.gdi.model.faq;


import java.util.ArrayList;

public class FAQRootObject {

    boolean error;
    ArrayList<FAQInfo> data;
    String message = "";

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public ArrayList<FAQInfo> getData() {
        return data;
    }

    public void setData(ArrayList<FAQInfo> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
