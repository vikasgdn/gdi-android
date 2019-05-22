package com.gdi.model.dashboard;

public class IACurrentVsLastInfo {

    Integer section_group_id = 0;
    String section_group_title = "";
    Integer max_mark = 0;
    Integer option_mark = 0;
    IAScoreInfo score;

    public Integer getSection_group_id() {
        return section_group_id;
    }

    public void setSection_group_id(Integer section_group_id) {
        this.section_group_id = section_group_id;
    }

    public String getSection_group_title() {
        return section_group_title;
    }

    public void setSection_group_title(String section_group_title) {
        this.section_group_title = section_group_title;
    }

    public Integer getMax_mark() {
        return max_mark;
    }

    public void setMax_mark(Integer max_mark) {
        this.max_mark = max_mark;
    }

    public Integer getOption_mark() {
        return option_mark;
    }

    public void setOption_mark(Integer option_mark) {
        this.option_mark = option_mark;
    }

    public IAScoreInfo getScore() {
        return score;
    }

    public void setScore(IAScoreInfo score) {
        this.score = score;
    }
}
