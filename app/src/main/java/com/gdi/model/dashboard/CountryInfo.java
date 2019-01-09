package com.gdi.model.dashboard;

public class CountryInfo {

    Integer current_rank = 0;
    Integer total_rank = 0;
    String score = "";

    public Integer getCurrent_rank() {
        return current_rank;
    }

    public void setCurrent_rank(Integer current_rank) {
        this.current_rank = current_rank;
    }

    public Integer getTotal_rank() {
        return total_rank;
    }

    public void setTotal_rank(Integer total_rank) {
        this.total_rank = total_rank;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
