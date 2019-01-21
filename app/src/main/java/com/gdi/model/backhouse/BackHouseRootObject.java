package com.gdi.model.backhouse;

import com.gdi.model.audioimages.AudioImageInfo;

import java.util.ArrayList;

public class BackHouseRootObject {

    boolean error;
    ArrayList<BackHouseInfo> data;
    String message = "";

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public ArrayList<BackHouseInfo> getData() {
        return data;
    }

    public void setData(ArrayList<BackHouseInfo> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
