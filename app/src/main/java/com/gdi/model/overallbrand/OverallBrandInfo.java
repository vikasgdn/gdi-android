package com.gdi.model.overallbrand;

import java.util.ArrayList;

public class OverallBrandInfo {

    OverallInfo overall;
    ArrayList<DepartmentOverallInfo> department_overall;

    public OverallInfo getOverall() {
        return overall;
    }

    public void setOverall(OverallInfo overall) {
        this.overall = overall;
    }

    public ArrayList<DepartmentOverallInfo> getDepartment_overall() {
        return department_overall;
    }

    public void setDepartment_overall(ArrayList<DepartmentOverallInfo> department_overall) {
        this.department_overall = department_overall;
    }
}
