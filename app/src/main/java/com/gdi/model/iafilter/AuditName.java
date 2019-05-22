package com.gdi.model.iafilter;

public class AuditName {

    int audit_id = 0;
    String audit_date = "";
    String audit_name = "";

    public int getAudit_id() {
        return audit_id;
    }

    public void setAudit_id(int audit_id) {
        this.audit_id = audit_id;
    }

    public String getAudit_date() {
        return audit_date;
    }

    public void setAudit_date(String audit_date) {
        this.audit_date = audit_date;
    }

    public String getAudit_name() {
        return audit_name;
    }

    public void setAudit_name(String audit_name) {
        this.audit_name = audit_name;
    }
}
