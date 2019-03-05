package com.gdi.model.faq;


import com.gdi.model.ReportUrlInfo;

import java.util.ArrayList;

public class FAQInfo {

    String location_title = "";
    int faq_total_obtained_mark = 0;
    int faq_total_max_mark = 0;
    String score_text = "";
    String city_name = "";
    ArrayList<FAQSectionInfo> sections;
    ReportUrlInfo report_urls;
    boolean isExpand = false;

    public String getLocation_title() {
        return location_title;
    }

    public void setLocation_title(String location_title) {
        this.location_title = location_title;
    }

    public int getFaq_total_obtained_mark() {
        return faq_total_obtained_mark;
    }

    public void setFaq_total_obtained_mark(int faq_total_obtained_mark) {
        this.faq_total_obtained_mark = faq_total_obtained_mark;
    }

    public int getFaq_total_max_mark() {
        return faq_total_max_mark;
    }

    public void setFaq_total_max_mark(int faq_total_max_mark) {
        this.faq_total_max_mark = faq_total_max_mark;
    }

    public String getScore_text() {
        return score_text;
    }

    public void setScore_text(String score_text) {
        this.score_text = score_text;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public ArrayList<FAQSectionInfo> getSections() {
        return sections;
    }

    public void setSections(ArrayList<FAQSectionInfo> sections) {
        this.sections = sections;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
    }

    public ReportUrlInfo getReport_urls() {
        return report_urls;
    }

    public void setReport_urls(ReportUrlInfo report_urls) {
        this.report_urls = report_urls;
    }
}
