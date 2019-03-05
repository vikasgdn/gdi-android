package com.gdi.model.faq;

import android.os.Parcel;
import android.os.Parcelable;

public class FAQAttachment implements Parcelable {

    String file_url = "";
    String thumb_url = "";
    String client_file_name = "";
    String file_name = "";
    String description = "";
    String file_type = "";


    protected FAQAttachment(Parcel in) {
        file_url = in.readString();
        thumb_url = in.readString();
        client_file_name = in.readString();
        file_name = in.readString();
        description = in.readString();
        file_type = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(file_url);
        dest.writeString(thumb_url);
        dest.writeString(client_file_name);
        dest.writeString(file_name);
        dest.writeString(description);
        dest.writeString(file_type);
    }

    @SuppressWarnings("unused")
    public static final Creator<FAQAttachment> CREATOR = new Creator<FAQAttachment>() {
        @Override
        public FAQAttachment createFromParcel(Parcel in) {
            return new FAQAttachment(in);
        }

        @Override
        public FAQAttachment[] newArray(int size) {
            return new FAQAttachment[size];
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

    public String getClient_file_name() {
        return client_file_name;
    }

    public void setClient_file_name(String client_file_name) {
        this.client_file_name = client_file_name;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFile_type() {
        return file_type;
    }

    public void setFile_type(String file_type) {
        this.file_type = file_type;
    }
}
