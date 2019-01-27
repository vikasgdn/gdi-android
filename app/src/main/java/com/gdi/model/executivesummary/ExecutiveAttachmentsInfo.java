package com.gdi.model.executivesummary;

import android.os.Parcel;
import android.os.Parcelable;

public class ExecutiveAttachmentsInfo implements Parcelable {

    String file_url = "";
    String thumb_url = "";
    String description = "";



    protected ExecutiveAttachmentsInfo(Parcel in) {
        file_url = in.readString();
        thumb_url = in.readString();
        description = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(file_url);
        dest.writeString(thumb_url);
        dest.writeString(description);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ExecutiveAttachmentsInfo> CREATOR = new Parcelable.Creator<ExecutiveAttachmentsInfo>() {
        @Override
        public ExecutiveAttachmentsInfo createFromParcel(Parcel in) {
            return new ExecutiveAttachmentsInfo(in);
        }

        @Override
        public ExecutiveAttachmentsInfo[] newArray(int size) {
            return new ExecutiveAttachmentsInfo[size];
        }
    };

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
