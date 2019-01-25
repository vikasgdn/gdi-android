package com.gdi.model.trendlocation;

import android.os.Parcel;
import android.os.Parcelable;

public class TrendLocationRound {

    String round_name = "";
    String overall_score = "";
    String rank = "";

    public String getRound_name() {
        return round_name;
    }

    public void setRound_name(String round_name) {
        this.round_name = round_name;
    }

    public String getOverall_score() {
        return overall_score;
    }

    public void setOverall_score(String overall_score) {
        this.overall_score = overall_score;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }
}
