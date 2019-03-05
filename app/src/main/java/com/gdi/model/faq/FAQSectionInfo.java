package com.gdi.model.faq;


import java.util.ArrayList;

public class FAQSectionInfo {

    String section_title = "";
    int total_obtained_mark = 0;
    int total_max_mark = 0;
    String score_text = "";
    ArrayList<FAQQuestionsInfo> questions;
    ArrayList<FAQAttachment> attachments;

    public String getSection_title() {
        return section_title;
    }

    public void setSection_title(String section_title) {
        this.section_title = section_title;
    }

    public int getTotal_obtained_mark() {
        return total_obtained_mark;
    }

    public void setTotal_obtained_mark(int total_obtained_mark) {
        this.total_obtained_mark = total_obtained_mark;
    }

    public int getTotal_max_mark() {
        return total_max_mark;
    }

    public void setTotal_max_mark(int total_max_mark) {
        this.total_max_mark = total_max_mark;
    }

    public String getScore_text() {
        return score_text;
    }

    public void setScore_text(String score_text) {
        this.score_text = score_text;
    }

    public ArrayList<FAQQuestionsInfo> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<FAQQuestionsInfo> questions) {
        this.questions = questions;
    }

    public ArrayList<FAQAttachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(ArrayList<FAQAttachment> attachments) {
        this.attachments = attachments;
    }
}
