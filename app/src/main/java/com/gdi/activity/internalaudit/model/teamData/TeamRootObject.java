package com.gdi.activity.internalaudit.model.teamData;


public class TeamRootObject {

    boolean error;
    TeamData data;
    String message = "";

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public TeamData getData() {
        return data;
    }

    public void setData(TeamData data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
