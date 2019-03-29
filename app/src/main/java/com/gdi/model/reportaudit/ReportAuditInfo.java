package com.gdi.model.reportaudit;

import java.util.ArrayList;

public class ReportAuditInfo {

    DashBoardInfo dashboard;
    HotelOverallInfo hotel_overall;
    ArrayList<DepatmentOverallInfo> department_overall;

    public DashBoardInfo getDashboard() {
        return dashboard;
    }

    public void setDashboard(DashBoardInfo dashboard) {
        this.dashboard = dashboard;
    }

    public HotelOverallInfo getHotel_overall() {
        return hotel_overall;
    }

    public void setHotel_overall(HotelOverallInfo hotel_overall) {
        this.hotel_overall = hotel_overall;
    }

    public ArrayList<DepatmentOverallInfo> getDepartment_overall() {
        return department_overall;
    }

    public void setDepartment_overall(ArrayList<DepatmentOverallInfo> department_overall) {
        this.department_overall = department_overall;
    }
}
