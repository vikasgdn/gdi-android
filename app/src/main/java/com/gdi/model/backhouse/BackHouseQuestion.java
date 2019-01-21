package com.gdi.model.backhouse;

import java.util.ArrayList;

public class BackHouseQuestion {

    String question_name;
    String comment;
    ArrayList<BackHouseOption> options;
    ArrayList<BackHouseAttachment> attachments;

    public String getQuestion_name() {
        return question_name;
    }

    public void setQuestion_name(String question_name) {
        this.question_name = question_name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ArrayList<BackHouseOption> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<BackHouseOption> options) {
        this.options = options;
    }

    public ArrayList<BackHouseAttachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(ArrayList<BackHouseAttachment> attachments) {
        this.attachments = attachments;
    }
}
