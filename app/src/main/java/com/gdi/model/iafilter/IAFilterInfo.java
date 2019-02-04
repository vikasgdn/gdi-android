package com.gdi.model.iafilter;

import com.gdi.model.filter.BrandsInfo;
import com.gdi.model.filter.FilterLocationInfo;

import java.util.ArrayList;

public class IAFilterInfo {

    ArrayList<BrandsInfo> brands;
    ArrayList<AuditTypes> audit_types;
    ArrayList<Audit> audits;
    ArrayList<FilterLocationInfo> locations;

    public ArrayList<BrandsInfo> getBrands() {
        return brands;
    }

    public void setBrands(ArrayList<BrandsInfo> brands) {
        this.brands = brands;
    }

    public ArrayList<AuditTypes> getAudit_types() {
        return audit_types;
    }

    public void setAudit_types(ArrayList<AuditTypes> audit_types) {
        this.audit_types = audit_types;
    }

    public ArrayList<Audit> getAudits() {
        return audits;
    }

    public void setAudits(ArrayList<Audit> audits) {
        this.audits = audits;
    }

    public ArrayList<FilterLocationInfo> getLocations() {
        return locations;
    }

    public void setLocations(ArrayList<FilterLocationInfo> locations) {
        this.locations = locations;
    }
}
