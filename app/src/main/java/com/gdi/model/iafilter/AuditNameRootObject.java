package com.gdi.model.iafilter;


import java.util.ArrayList;

public class AuditNameRootObject {

    boolean error;
    ArrayList<AuditName> data;
    String message = "";

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public ArrayList<AuditName> getData() {
        return data;
    }

    public void setData(ArrayList<AuditName> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
