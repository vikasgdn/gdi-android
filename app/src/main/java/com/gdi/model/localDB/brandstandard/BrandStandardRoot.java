package com.gdi.model.localDB.brandstandard;

import com.gdi.model.audit.BrandStandard.BrandStandardSection;

import java.util.ArrayList;

public class BrandStandardRoot {

    String auditId;
    ArrayList<BrandStandardSection> sections;

    public String getAuditId() {
        return auditId;
    }

    public void setAuditId(String auditId) {
        this.auditId = auditId;
    }

    public ArrayList<BrandStandardSection> getSections() {
        return sections;
    }

    public void setSections(ArrayList<BrandStandardSection> sections) {
        this.sections = sections;
    }
}
