package com.gdi.model.reportfaq;


public class FAQQuestionsOption {

    int option_id = 0;
    String option_text = "";
    int option_mark = 0;
    int answer_option_id = 0;

    public int getOption_id() {
        return option_id;
    }

    public void setOption_id(int option_id) {
        this.option_id = option_id;
    }

    public String getOption_text() {
        return option_text;
    }

    public void setOption_text(String option_text) {
        this.option_text = option_text;
    }

    public int getOption_mark() {
        return option_mark;
    }

    public void setOption_mark(int option_mark) {
        this.option_mark = option_mark;
    }

    public int getAnswer_option_id() {
        return answer_option_id;
    }

    public void setAnswer_option_id(int answer_option_id) {
        this.answer_option_id = answer_option_id;
    }
}
