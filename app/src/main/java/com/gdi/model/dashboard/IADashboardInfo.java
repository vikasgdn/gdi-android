package com.gdi.model.dashboard;

import java.util.ArrayList;

public class IADashboardInfo {

    OverallInfo overall;
    ArrayList<LastFiveInfo> last_five;
    ArrayList<IACurrentVsLastInfo> curr_vs_last;
    ArrayList<IADeparmentInfo> highest_dept;
    ArrayList<IADeparmentInfo> lowest_dept;
    String last_audit_date = "";

    public OverallInfo getOverall() {
        return overall;
    }

    public void setOverall(OverallInfo overall) {
        this.overall = overall;
    }

    public ArrayList<LastFiveInfo> getLast_five() {
        return last_five;
    }

    public void setLast_five(ArrayList<LastFiveInfo> last_five) {
        this.last_five = last_five;
    }

    public ArrayList<IACurrentVsLastInfo> getCurr_vs_last() {
        return curr_vs_last;
    }

    public void setCurr_vs_last(ArrayList<IACurrentVsLastInfo> curr_vs_last) {
        this.curr_vs_last = curr_vs_last;
    }

    public ArrayList<IADeparmentInfo> getHighest_dept() {
        return highest_dept;
    }

    public void setHighest_dept(ArrayList<IADeparmentInfo> highest_dept) {
        this.highest_dept = highest_dept;
    }

    public ArrayList<IADeparmentInfo> getLowest_dept() {
        return lowest_dept;
    }

    public void setLowest_dept(ArrayList<IADeparmentInfo> lowest_dept) {
        this.lowest_dept = lowest_dept;
    }

    public String getLast_audit_date() {
        return last_audit_date;
    }

    public void setLast_audit_date(String last_audit_date) {
        this.last_audit_date = last_audit_date;
    }
}
