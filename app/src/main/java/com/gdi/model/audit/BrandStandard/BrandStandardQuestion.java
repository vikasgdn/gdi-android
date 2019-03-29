package com.gdi.model.audit.BrandStandard;


import java.util.ArrayList;

public class BrandStandardQuestion {

    private int question_id = 0;
    private String question_title = "";
    private int sub_section_id = 0;
    private String sub_section_title = "";
    private int question_type_id = 0;
    private String question_type = "";
    private String hint = "";
    private int is_required = 0;
    private int is_visible = 0;
    private int has_na = 0;
    private int has_comment = 0;
    private int is_numbered = 0;
    private int max_mark = 0;
    private String audit_answer = "";
    private int audit_answer_na = 0;
    private String audit_comment = "";
    private int answer_status = 0;
    private String reviewer_answer_comment = "";
    private int obtained_mark = 0;
    private int audit_question_file_cnt = 0;
    ArrayList<BrandStandardQuestionsOption> options;

    public int getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }

    public String getQuestion_title() {
        return question_title;
    }

    public void setQuestion_title(String question_title) {
        this.question_title = question_title;
    }

    public int getSub_section_id() {
        return sub_section_id;
    }

    public void setSub_section_id(int sub_section_id) {
        this.sub_section_id = sub_section_id;
    }

    public String getSub_section_title() {
        return sub_section_title;
    }

    public void setSub_section_title(String sub_section_title) {
        this.sub_section_title = sub_section_title;
    }

    public int getQuestion_type_id() {
        return question_type_id;
    }

    public void setQuestion_type_id(int question_type_id) {
        this.question_type_id = question_type_id;
    }

    public String getQuestion_type() {
        return question_type;
    }

    public void setQuestion_type(String question_type) {
        this.question_type = question_type;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public int getIs_required() {
        return is_required;
    }

    public void setIs_required(int is_required) {
        this.is_required = is_required;
    }

    public int getIs_visible() {
        return is_visible;
    }

    public void setIs_visible(int is_visible) {
        this.is_visible = is_visible;
    }

    public int getHas_na() {
        return has_na;
    }

    public void setHas_na(int has_na) {
        this.has_na = has_na;
    }

    public int getHas_comment() {
        return has_comment;
    }

    public void setHas_comment(int has_comment) {
        this.has_comment = has_comment;
    }

    public int getIs_numbered() {
        return is_numbered;
    }

    public void setIs_numbered(int is_numbered) {
        this.is_numbered = is_numbered;
    }

    public int getMax_mark() {
        return max_mark;
    }

    public void setMax_mark(int max_mark) {
        this.max_mark = max_mark;
    }

    public String getAudit_answer() {
        return audit_answer;
    }

    public void setAudit_answer(String audit_answer) {
        this.audit_answer = audit_answer;
    }

    public int getAudit_answer_na() {
        return audit_answer_na;
    }

    public void setAudit_answer_na(int audit_answer_na) {
        this.audit_answer_na = audit_answer_na;
    }

    public String getAudit_comment() {
        return audit_comment;
    }

    public void setAudit_comment(String audit_comment) {
        this.audit_comment = audit_comment;
    }

    public int getAnswer_status() {
        return answer_status;
    }

    public void setAnswer_status(int answer_status) {
        this.answer_status = answer_status;
    }

    public String getReviewer_answer_comment() {
        return reviewer_answer_comment;
    }

    public void setReviewer_answer_comment(String reviewer_answer_comment) {
        this.reviewer_answer_comment = reviewer_answer_comment;
    }

    public int getObtained_mark() {
        return obtained_mark;
    }

    public void setObtained_mark(int obtained_mark) {
        this.obtained_mark = obtained_mark;
    }

    public int getAudit_question_file_cnt() {
        return audit_question_file_cnt;
    }

    public void setAudit_question_file_cnt(int audit_question_file_cnt) {
        this.audit_question_file_cnt = audit_question_file_cnt;
    }

    public ArrayList<BrandStandardQuestionsOption> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<BrandStandardQuestionsOption> options) {
        this.options = options;
    }
}
