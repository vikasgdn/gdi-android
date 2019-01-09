package com.gdi.model.actionplan;

public class ActionPlanModel {

    String audit_round;
    String hotel_name;
    String audit_date;
    String city;
    String start_date;
    String end_date;
    ActionPlanStatus status;

    public String getAudit_round() {
        return audit_round;
    }

    public void setAudit_round(String audit_round) {
        this.audit_round = audit_round;
    }

    public String getHotel_name() {
        return hotel_name;
    }

    public void setHotel_name(String hotel_name) {
        this.hotel_name = hotel_name;
    }

    public String getAudit_date() {
        return audit_date;
    }

    public void setAudit_date(String audit_date) {
        this.audit_date = audit_date;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public ActionPlanStatus getStatus() {
        return status;
    }

    public void setStatus(ActionPlanStatus status) {
        this.status = status;
    }
}
