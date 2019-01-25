package com.gdi.model.integrity;

import android.os.Parcel;
import android.os.Parcelable;

public class IntegrityAttachment implements Parcelable {

    String file_url = "";
    String thumb_url = "";
    int location_id = 0;
    int integrity_id = 0;
    String client_file_name = "";
    String file_name = "";
    String description = "";
    String created_on = "";
    String file_type = "";

    protected IntegrityAttachment(Parcel in) {
        file_url = in.readString();
        thumb_url = in.readString();
        location_id = in.readInt();
        integrity_id = in.readInt();
        client_file_name = in.readString();
        file_name = in.readString();
        description = in.readString();
        created_on = in.readString();
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
        dest.writeInt(location_id);
        dest.writeInt(integrity_id);
        dest.writeString(client_file_name);
        dest.writeString(file_name);
        dest.writeString(description);
        dest.writeString(created_on);
        dest.writeString(file_type);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<IntegrityAttachment> CREATOR = new Parcelable.Creator<IntegrityAttachment>() {
        @Override
        public IntegrityAttachment createFromParcel(Parcel in) {
            return new IntegrityAttachment(in);
        }

        @Override
        public IntegrityAttachment[] newArray(int size) {
            return new IntegrityAttachment[size];
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

    public int getLocation_id() {
        return location_id;
    }

    public void setLocation_id(int location_id) {
        this.location_id = location_id;
    }

    public int getIntegrity_id() {
        return integrity_id;
    }

    public void setIntegrity_id(int integrity_id) {
        this.integrity_id = integrity_id;
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

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    public String getFile_type() {
        return file_type;
    }

    public void setFile_type(String file_type) {
        this.file_type = file_type;
    }

    public static Creator<IntegrityAttachment> getCREATOR() {
        return CREATOR;
    }
}
