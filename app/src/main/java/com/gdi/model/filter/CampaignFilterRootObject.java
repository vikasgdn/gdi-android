package com.gdi.model.filter;

import java.util.ArrayList;

public class CampaignFilterRootObject {

    boolean error;
    ArrayList<CampaignsInfo> data;
    String message = "";

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public ArrayList<CampaignsInfo> getData() {
        return data;
    }

    public void setData(ArrayList<CampaignsInfo> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
