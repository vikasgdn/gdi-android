package com.gdi.model.locationcampaign;

import android.os.Parcel;
import android.os.Parcelable;

public class LocationCampaignRound2 implements Parcelable {

    String round_name = "";
    String score = "";



    protected LocationCampaignRound2(Parcel in) {
        round_name = in.readString();
        score = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(round_name);
        dest.writeString(score);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<LocationCampaignRound2> CREATOR = new Parcelable.Creator<LocationCampaignRound2>() {
        @Override
        public LocationCampaignRound2 createFromParcel(Parcel in) {
            return new LocationCampaignRound2(in);
        }

        @Override
        public LocationCampaignRound2[] newArray(int size) {
            return new LocationCampaignRound2[size];
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
}
