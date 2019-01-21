package com.gdi.model.audioimages;

import java.util.ArrayList;

public class IAAudioImageRootObject {

    boolean error;
    AudioImageLocation data;
    String message = "";

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public AudioImageLocation getData() {
        return data;
    }

    public void setData(AudioImageLocation data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
