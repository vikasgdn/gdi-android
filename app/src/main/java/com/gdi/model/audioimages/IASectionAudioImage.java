package com.gdi.model.audioimages;

import com.gdi.model.ReportUrlInfo;

import java.util.ArrayList;

public class IASectionAudioImage {

    String section_name;
    ArrayList<IAAudioImages> images_audio;
    ReportUrlInfo report_urls;

    public String getSection_name() {
        return section_name;
    }

    public void setSection_name(String section_name) {
        this.section_name = section_name;
    }

    public ArrayList<IAAudioImages> getImages_audio() {
        return images_audio;
    }

    public void setImages_audio(ArrayList<IAAudioImages> images_audio) {
        this.images_audio = images_audio;
    }

    public ReportUrlInfo getReport_urls() {
        return report_urls;
    }

    public void setReport_urls(ReportUrlInfo report_urls) {
        this.report_urls = report_urls;
    }
}
