package com.gdi.model.dashboard;

import java.util.ArrayList;

public class IADashboardInfo {

    OverallInfo overall;
    ArrayList<LastFiveInfo> last_five;
    ArrayList<IACurrentVsLastInfo> curr_vs_last;

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
}
