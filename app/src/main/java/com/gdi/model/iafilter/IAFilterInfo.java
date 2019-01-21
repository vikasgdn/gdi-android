package com.gdi.model.iafilter;

import com.gdi.model.filter.BrandsInfo;
import com.gdi.model.filter.LocationsInfo;

import java.util.ArrayList;

public class IAFilterInfo {

    ArrayList<BrandsInfo> brands;
    ArrayList<AuditTypes> audit_types;
    ArrayList<Audit> audits;
    ArrayList<LocationsInfo> locations;

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

    public ArrayList<LocationsInfo> getLocations() {
        return locations;
    }

    public void setLocations(ArrayList<LocationsInfo> locations) {
        this.locations = locations;
    }
}
