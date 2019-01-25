package com.gdi.model.sectiongroup;

import com.gdi.model.ReportUrlInfo;

import java.util.ArrayList;

public class SectionGroupRootObject {

    boolean error;
    SectionGroupInfo data;
    String message = "";


    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public SectionGroupInfo getData() {
        return data;
    }

    public void setData(SectionGroupInfo data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
