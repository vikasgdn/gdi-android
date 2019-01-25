package com.gdi.model.trendlocation;

import android.os.Parcel;
import android.os.Parcelable;

public class TrendLocationRound2 implements Parcelable {

    String round_name = "";
    String score = "";
    String rank = "";


    protected TrendLocationRound2(Parcel in) {
        round_name = in.readString();
        score = in.readString();
        rank = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(round_name);
        dest.writeString(score);
        dest.writeString(rank);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<TrendLocationRound2> CREATOR = new Parcelable.Creator<TrendLocationRound2>() {
        @Override
        public TrendLocationRound2 createFromParcel(Parcel in) {
            return new TrendLocationRound2(in);
        }

        @Override
        public TrendLocationRound2[] newArray(int size) {
            return new TrendLocationRound2[size];
        }
    };

    public String getRound_name() {
        return round_name;
    }

    public void setRound_name(String round_name) {
        this.round_name = round_name;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }
}
