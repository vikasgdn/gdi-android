package com.gdi.model.trendlocation;

import com.gdi.model.ReportUrlInfo;
import com.gdi.model.sectiongroup.SectionGroupInfo;

import java.util.ArrayList;

public class TrendLocationRootObject {

    boolean error;
    TrendLocationInfo data;
    String message = "";

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public TrendLocationInfo getData() {
        return data;
    }

    public void setData(TrendLocationInfo data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
