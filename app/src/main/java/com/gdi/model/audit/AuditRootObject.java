package com.gdi.model.audit;

public class AuditRootObject {

    boolean error;
    AuditInfo data;
    String message = "";

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public AuditInfo getData() {
        return data;
    }

    public void setData(AuditInfo data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
