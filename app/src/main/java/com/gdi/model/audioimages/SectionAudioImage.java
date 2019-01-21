package com.gdi.model.audioimages;

import com.gdi.model.ReportUrlInfo;

import java.util.ArrayList;

public class SectionAudioImage {

    String section_name;
    String score;
    ArrayList<AttachmentAudioImages> attachments;
    ReportUrlInfo report_urls;

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

    public ArrayList<AttachmentAudioImages> getAttachments() {
        return attachments;
    }

    public void setAttachments(ArrayList<AttachmentAudioImages> attachments) {
        this.attachments = attachments;
    }

    public ReportUrlInfo getReport_urls() {
        return report_urls;
    }

    public void setReport_urls(ReportUrlInfo report_urls) {
        this.report_urls = report_urls;
    }
}
