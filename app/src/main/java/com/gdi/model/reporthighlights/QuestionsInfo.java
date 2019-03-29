package com.gdi.model.reporthighlights;

import java.util.ArrayList;

public class QuestionsInfo {

    String question = "";
    String answer = "";
    ArrayList<AttachmentsInfo> attachments;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public ArrayList<AttachmentsInfo> getAttachments() {
        return attachments;
    }

    public void setAttachments(ArrayList<AttachmentsInfo> attachments) {
        this.attachments = attachments;
    }
}
