package com.gdi.model.reporthighlights;


public class HighlightsRootObject {

    boolean error;
    HighlightsInfo data;
    String message = "";

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public HighlightsInfo getData() {
        return data;
    }

    public void setData(HighlightsInfo data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
