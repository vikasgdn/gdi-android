package com.gdi.model.dashboard;

public class LastFiveInfo {

    Integer audit_id = 0;
    Integer max_mark = 0;
    Integer option_mark = 0;
    String audit_date = "";
    String score = "";

    public Integer getAudit_id() {
        return audit_id;
    }

    public void setAudit_id(Integer audit_id) {
        this.audit_id = audit_id;
    }

    public Integer getMax_mark() {
        return max_mark;
    }

    public void setMax_mark(Integer max_mark) {
        this.max_mark = max_mark;
    }

    public Integer getOption_mark() {
        return option_mark;
    }

    public void setOption_mark(Integer option_mark) {
        this.option_mark = option_mark;
    }

    public String getAudit_date() {
        return audit_date;
    }

    public void setAudit_date(String audit_date) {
        this.audit_date = audit_date;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
