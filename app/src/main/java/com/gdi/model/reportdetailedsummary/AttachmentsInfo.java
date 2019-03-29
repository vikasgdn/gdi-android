package com.gdi.model.reportdetailedsummary;

import android.os.Parcel;
import android.os.Parcelable;

public class AttachmentsInfo implements Parcelable {

    String file_url = "";
    String thumb_url = "";
    String description = "";

    protected AttachmentsInfo(Parcel in) {
        file_url = in.readString();
        thumb_url = in.readString();
        description = in.readString();
    }

    public static final Creator<AttachmentsInfo> CREATOR = new Creator<AttachmentsInfo>() {
        @Override
        public AttachmentsInfo createFromParcel(Parcel in) {
            return new AttachmentsInfo(in);
        }

        @Override
        public AttachmentsInfo[] newArray(int size) {
            return new AttachmentsInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(file_url);
        parcel.writeString(thumb_url);
        parcel.writeString(description);
    }

    public String getFile_url() {
        return file_url;
    }

    public void setFile_url(String file_url) {
        this.file_url = file_url;
    }

    public String getThumb_url() {
        return thumb_url;
    }

    public void setThumb_url(String thumb_url) {
        this.thumb_url = thumb_url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
