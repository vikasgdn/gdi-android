package com.gdi.model.reportfaq;


import java.util.ArrayList;

public class FAQQuestionsInfo {

    int question_id = 0;
    String question_title = "";
    int max_mark = 0;
    int obtained_mark = 0;
    int question_type_id = 0;
    String question_type = "";
    String answer_comment = "";
    int answer_na = 0;
    ArrayList<FAQQuestionsOption> options;
    //ArrayList<FAQOptionAnswerId> answer_option_id;

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

    public int getMax_mark() {
        return max_mark;
    }

    public void setMax_mark(int max_mark) {
        this.max_mark = max_mark;
    }

    public int getObtained_mark() {
        return obtained_mark;
    }

    public void setObtained_mark(int obtained_mark) {
        this.obtained_mark = obtained_mark;
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

    public int getAnswer_na() {
        return answer_na;
    }

    public void setAnswer_na(int answer_na) {
        this.answer_na = answer_na;
    }

    public ArrayList<FAQQuestionsOption> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<FAQQuestionsOption> options) {
        this.options = options;
    }

    public String getAnswer_comment() {
        return answer_comment;
    }

    public void setAnswer_comment(String answer_comment) {
        this.answer_comment = answer_comment;
    }

    /*public ArrayList<FAQOptionAnswerId> getAnswer_option_id() {
        return answer_option_id;
    }

    public void setAnswer_option_id(ArrayList<FAQOptionAnswerId> answer_option_id) {
        this.answer_option_id = answer_option_id;
    }*/
}
