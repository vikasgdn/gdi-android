package com.gdi.model.actionplan;

import java.util.ArrayList;

public class ActionPlanRootObject {

    boolean error;
    ArrayList<ActionPlanModel> data;
    String message = "";

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public ArrayList<ActionPlanModel> getData() {
        return data;
    }

    public void setData(ArrayList<ActionPlanModel> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
