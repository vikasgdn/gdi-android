package com.gdi.model.dashboard;

import java.util.ArrayList;

public class DashboardInfo {

    OverallInfo overall;
    ArrayList<LastFiveInfo> last_five;
    ArrayList<CurrentVsLastInfo> curr_vs_last;
    RanksInfo ranks;
    ArrayList<HighestDeparmentInfo> highest_dept;
    ArrayList<LowestDepartmentInfo> lowest_dept;

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

    public ArrayList<CurrentVsLastInfo> getCurr_vs_last() {
        return curr_vs_last;
    }

    public void setCurr_vs_last(ArrayList<CurrentVsLastInfo> curr_vs_last) {
        this.curr_vs_last = curr_vs_last;
    }

    public RanksInfo getRanks() {
        return ranks;
    }

    public void setRanks(RanksInfo ranks) {
        this.ranks = ranks;
    }

    public ArrayList<HighestDeparmentInfo> getHighest_dept() {
        return highest_dept;
    }

    public void setHighest_dept(ArrayList<HighestDeparmentInfo> highest_dept) {
        this.highest_dept = highest_dept;
    }

    public ArrayList<LowestDepartmentInfo> getLowest_dept() {
        return lowest_dept;
    }

    public void setLowest_dept(ArrayList<LowestDepartmentInfo> lowest_dept) {
        this.lowest_dept = lowest_dept;
    }
}
