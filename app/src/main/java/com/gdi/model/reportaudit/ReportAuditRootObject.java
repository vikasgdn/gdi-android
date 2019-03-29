package com.gdi.model.reportaudit;

public class ReportAuditRootObject {

    boolean error;
    ReportAuditInfo data;
    String message = "";

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public ReportAuditInfo getData() {
        return data;
    }

    public void setData(ReportAuditInfo data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
