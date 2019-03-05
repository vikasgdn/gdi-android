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
    int is_na = 0;
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
        is_na = in.readInt();
        if (in.readByte() == 0x01) {
            attachments = new ArrayList<AttachmentsInfo>();
            in.readList(attachments, AttachmentsInfo.class.getClassLoader());
        } else {
            attachments = null;
        }
        report_urls = (ReportUrlInfo) in.readValue(ReportUrlInfo.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(section_name);
        dest.writeString(score);
        dest.writeString(staff_name);
        dest.writeString(date);
        dest.writeString(time);
        dest.writeString(summary);
        dest.writeString(key_positives);
        dest.writeString(key_negatives);
        dest.writeString(recommendation);
        dest.writeInt(is_na);
        if (attachments == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(attachments);
        }
        //dest.writeValue(report_urls);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<SectionsInfo> CREATOR = new Parcelable.Creator<SectionsInfo>() {
        @Override
        public SectionsInfo createFromParcel(Parcel in) {
            return new SectionsInfo(in);
        }

        @Override
        public SectionsInfo[] newArray(int size) {
            return new SectionsInfo[size];
        }
    };

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

    public int getIs_na() {
        return is_na;
    }

    public void setIs_na(int is_na) {
        this.is_na = is_na;
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
