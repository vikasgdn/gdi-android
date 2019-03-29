package com.gdi.model.audit.BrandStandard;


import java.util.ArrayList;

public class BrandStandardSection {

    private int section_id = 0;
    private String section_title = "";
    private int section_group_id = 0;
    private String section_group_title = "";
    private int audit_section_file_cnt = 0;
    ArrayList<BrandStandardQuestion> questions;
    ArrayList<BrandStandardSubSection> sub_sections;

    public int getSection_id() {
        return section_id;
    }

    public void setSection_id(int section_id) {
        this.section_id = section_id;
    }

    public String getSection_title() {
        return section_title;
    }

    public void setSection_title(String section_title) {
        this.section_title = section_title;
    }

    public int getSection_group_id() {
        return section_group_id;
    }

    public void setSection_group_id(int section_group_id) {
        this.section_group_id = section_group_id;
    }

    public String getSection_group_title() {
        return section_group_title;
    }

    public void setSection_group_title(String section_group_title) {
        this.section_group_title = section_group_title;
    }

    public int getAudit_section_file_cnt() {
        return audit_section_file_cnt;
    }

    public void setAudit_section_file_cnt(int audit_section_file_cnt) {
        this.audit_section_file_cnt = audit_section_file_cnt;
    }

    public ArrayList<BrandStandardQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<BrandStandardQuestion> questions) {
        this.questions = questions;
    }

    public ArrayList<BrandStandardSubSection> getSub_sections() {
        return sub_sections;
    }

    public void setSub_sections(ArrayList<BrandStandardSubSection> sub_sections) {
        this.sub_sections = sub_sections;
    }
}
