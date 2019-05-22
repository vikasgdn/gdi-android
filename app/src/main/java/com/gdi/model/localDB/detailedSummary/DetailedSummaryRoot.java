package com.gdi.model.localDB.detailedSummary;

import com.gdi.model.audit.BrandStandard.BrandStandardSection;
import com.gdi.model.audit.DetailedSummary.DetailedSummaryInfo;

import java.util.ArrayList;

public class DetailedSummaryRoot {

    String auditId;
    ArrayList<DetailedSummaryInfo> sections;

    public String getAuditId() {
        return auditId;
    }

    public void setAuditId(String auditId) {
        this.auditId = auditId;
    }

    public ArrayList<DetailedSummaryInfo> getSections() {
        return sections;
    }

    public void setSections(ArrayList<DetailedSummaryInfo> sections) {
        this.sections = sections;
    }
}
