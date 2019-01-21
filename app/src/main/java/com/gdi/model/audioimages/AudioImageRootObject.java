package com.gdi.model.audioimages;

import java.util.ArrayList;

public class AudioImageRootObject {

    boolean error;
    ArrayList<AudioImageInfo> data;
    String message = "";

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public ArrayList<AudioImageInfo> getData() {
        return data;
    }

    public void setData(ArrayList<AudioImageInfo> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
