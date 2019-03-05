package com.gdi.model.detailedsummary;

import com.gdi.model.ReportUrlInfo;

import java.util.ArrayList;

public class SectionGroupInfo {

    String section_group_name = "";
    String score = "";
    Integer sec_grp_obt_mark = 0;
    Integer sec_grp_tot_mark = 0;
    ReportUrlInfo report_urls;
    ArrayList<SectionsInfo> sections;
    boolean isExpand = false;

    public String getSection_group_name() {
        return section_group_name;
    }

    public void setSection_group_name(String section_group_name) {
        this.section_group_name = section_group_name;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public Integer getSec_grp_obt_mark() {
        return sec_grp_obt_mark;
    }

    public void setSec_grp_obt_mark(Integer sec_grp_obt_mark) {
        this.sec_grp_obt_mark = sec_grp_obt_mark;
    }

    public Integer getSec_grp_tot_mark() {
        return sec_grp_tot_mark;
    }

    public void setSec_grp_tot_mark(Integer sec_grp_tot_mark) {
        this.sec_grp_tot_mark = sec_grp_tot_mark;
    }

    public ReportUrlInfo getReport_urls() {
        return report_urls;
    }

    public void setReport_urls(ReportUrlInfo report_urls) {
        this.report_urls = report_urls;
    }

    public ArrayList<SectionsInfo> getSections() {
        return sections;
    }

    public void setSections(ArrayList<SectionsInfo> sections) {
        this.sections = sections;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
    }
}
