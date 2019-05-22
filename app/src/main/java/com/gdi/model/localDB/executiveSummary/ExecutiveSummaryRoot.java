package com.gdi.model.localDB.executiveSummary;

import com.gdi.model.audit.BrandStandard.BrandStandardSection;
import com.gdi.model.audit.ExecutiveSummary.ExecutiveSummaryInfo;

import java.util.ArrayList;

public class ExecutiveSummaryRoot {

    String auditId;
    ExecutiveSummaryInfo sections;

    public String getAuditId() {
        return auditId;
    }

    public void setAuditId(String auditId) {
        this.auditId = auditId;
    }

    public ExecutiveSummaryInfo getSections() {
        return sections;
    }

    public void setSections(ExecutiveSummaryInfo sections) {
        this.sections = sections;
    }
}
