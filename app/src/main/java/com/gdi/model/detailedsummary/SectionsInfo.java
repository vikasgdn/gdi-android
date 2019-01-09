package com.gdi.model.detailedsummary;

import android.os.Parcel;
import android.os.Parcelable;

import com.gdi.model.ReportUrlInfo;

import java.util.ArrayList;

public class SectionsInfo implements Parcelable {

    String section_name = "";
    String score = "";
    String staff_name = "";
    String date = "";
    String time = "";
    String summary = "";
    String key_positives = "";
    String key_negatives = "";
    String recommendation = "";
    ArrayList<AttachmentsInfo> attachments;
    ReportUrlInfo report_urls;

    public SectionsInfo() {
    }

    protected SectionsInfo(Parcel in) {
        section_name = in.readString();
        score = in.readString();
        staff_name = in.readString();
        date = in.readString();
        time = in.readString();
        summary = in.readString();
        key_positives = in.readString();
        key_negatives = in.readString();
        recommendation = in.readString();
        //attachments = in.readArrayList(AttachmentsInfo);
        //report_urls = in.readTypedObject();
    }

    public static final Creator<SectionsInfo> CREATOR = new Creator<SectionsInfo>() {
        @Override
        public SectionsInfo createFromParcel(Parcel source) {
            return new SectionsInfo(source);
        }

        @Override
        public SectionsInfo[] newArray(int size) {
            return new SectionsInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(section_name);
        parcel.writeString(score);
        parcel.writeString(staff_name);
        parcel.writeString(date);
        parcel.writeString(time);
        parcel.writeString(summary);
        parcel.writeString(key_positives);
        parcel.writeString(key_negatives);
        parcel.writeString(recommendation);
    }

    public String getSection_name() {
        return section_name;
    }

    public void setSection_name(String section_name) {
        this.section_name = section_name;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getStaff_name() {
        return staff_name;
    }

    public void setStaff_name(String staff_name) {
        this.staff_name = staff_name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getKey_positives() {
        return key_positives;
    }

    public void setKey_positives(String key_positives) {
        this.key_positives = key_positives;
    }

    public String getKey_negatives() {
        return key_negatives;
    }

    public void setKey_negatives(String key_negatives) {
        this.key_negatives = key_negatives;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }

    public ArrayList<AttachmentsInfo> getAttachments() {
        return attachments;
    }

    public void setAttachments(ArrayList<AttachmentsInfo> attachments) {
        this.attachments = attachments;
    }

    public ReportUrlInfo getReport_urls() {
        return report_urls;
    }

    public void setReport_urls(ReportUrlInfo report_urls) {
        this.report_urls = report_urls;
    }
}
