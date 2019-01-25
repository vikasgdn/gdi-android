package com.gdi.model.sectiongroup;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class SectionGroupLocation implements Parcelable {

    String location = "";
    String country = "";
    String city = "";
    String general_manager = "";
    String overall_score = "";
    ArrayList<SectionGroupModel> section_groups;




    protected SectionGroupLocation(Parcel in) {
        location = in.readString();
        country = in.readString();
        city = in.readString();
        general_manager = in.readString();
        overall_score = in.readString();
        if (in.readByte() == 0x01) {
            section_groups = new ArrayList<SectionGroupModel>();
            in.readList(section_groups, SectionGroupModel.class.getClassLoader());
        } else {
            section_groups = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(location);
        dest.writeString(country);
        dest.writeString(city);
        dest.writeString(general_manager);
        dest.writeString(overall_score);
        if (section_groups == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(section_groups);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<SectionGroupLocation> CREATOR = new Parcelable.Creator<SectionGroupLocation>() {
        @Override
        public SectionGroupLocation createFromParcel(Parcel in) {
            return new SectionGroupLocation(in);
        }

        @Override
        public SectionGroupLocation[] newArray(int size) {
            return new SectionGroupLocation[size];
        }
    };

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getGeneral_manager() {
        return general_manager;
    }

    public void setGeneral_manager(String general_manager) {
        this.general_manager = general_manager;
    }

    public String getOverall_score() {
        return overall_score;
    }

    public void setOverall_score(String overall_score) {
        this.overall_score = overall_score;
    }

    public ArrayList<SectionGroupModel> getSection_groups() {
        return section_groups;
    }

    public void setSection_groups(ArrayList<SectionGroupModel> section_groups) {
        this.section_groups = section_groups;
    }
}
