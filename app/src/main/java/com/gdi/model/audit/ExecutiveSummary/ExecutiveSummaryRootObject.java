package com.gdi.model.audit.ExecutiveSummary;


import com.gdi.model.audit.DetailedSummary.DetailedSummaryInfo;

import java.util.ArrayList;

public class ExecutiveSummaryRootObject {

    boolean error;
    ExecutiveSummaryInfo data;
    String message = "";

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public ExecutiveSummaryInfo getData() {
        return data;
    }

    public void setData(ExecutiveSummaryInfo data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
