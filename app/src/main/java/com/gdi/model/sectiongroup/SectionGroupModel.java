package com.gdi.model.sectiongroup;

import android.os.Parcel;
import android.os.Parcelable;

public class SectionGroupModel implements Parcelable {

    String section_group_name = "";
    String score = "";

    protected SectionGroupModel(Parcel in) {
        section_group_name = in.readString();
        score = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(section_group_name);
        dest.writeString(score);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<SectionGroupModel> CREATOR = new Parcelable.Creator<SectionGroupModel>() {
        @Override
        public SectionGroupModel createFromParcel(Parcel in) {
            return new SectionGroupModel(in);
        }

        @Override
        public SectionGroupModel[] newArray(int size) {
            return new SectionGroupModel[size];
        }
    };

    public String getSection_group_name() {
        return section_group_name;
    }

    public void setSection_group_name(String section_group_name) {
        this.section_group_name = section_group_name;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
