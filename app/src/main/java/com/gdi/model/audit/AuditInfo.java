package com.gdi.model.audit;


public class AuditInfo {

    private int audit_id = 0;
    private String audit_name = "";
    private String audit_due_date = "";
    private int brand_std_status = 0;
    private String audit_date = "";
    private String created_on = "";
    private int auditor_id = 0;
    private int reviewer_id = 0;
    private int detailed_sum_status = 0;
    private int exec_sum_status = 0;
    private String instructions = "";
    private int benchmark_per = 0;
    private String auditor_fname = "";
    private String auditor_lname = "";
    private String auditor_email = "";
    private String auditor_phone = "";
    private int location_id = 0;
    private String location_title = "";
    private int country_id = 0;
    private String country_name = "";
    private int city_id = 0;
    private String city_name = "";
    private int report_status = 0;
    private String reviewer_feedback = "";
    private int reviewer_rating = 0;
    private String brand_name = "";
    private String reviewer_fname = "";
    private String reviewer_lname = "";
    private String reviewer_email = "";
    private String reviewer_phone = "";
    private int total_obtained_mark = 0;
    private int total_max_mark = 0;
    private String category_title = "";
    private int audit_type_id = 0;
    private String audit_type = "";
    private String creator_fname = "";
    private String creator_lname = "";
    private int creator_role = 0;
    private int created_by = 0;
    private String brand_std_status_name = "";
    private String detailed_sum_status_name = "";
    private String exec_sum_status_name = "";

    public int getAudit_id() {
        return audit_id;
    }

    public void setAudit_id(int audit_id) {
        this.audit_id = audit_id;
    }

    public String getAudit_name() {
        return audit_name;
    }

    public void setAudit_name(String audit_name) {
        this.audit_name = audit_name;
    }

    public String getAudit_due_date() {
        return audit_due_date;
    }

    public void setAudit_due_date(String audit_due_date) {
        this.audit_due_date = audit_due_date;
    }

    public int getBrand_std_status() {
        return brand_std_status;
    }

    public void setBrand_std_status(int brand_std_status) {
        this.brand_std_status = brand_std_status;
    }

    public String getAudit_date() {
        return audit_date;
    }

    public void setAudit_date(String audit_date) {
        this.audit_date = audit_date;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    public int getAuditor_id() {
        return auditor_id;
    }

    public void setAuditor_id(int auditor_id) {
        this.auditor_id = auditor_id;
    }

    public int getReviewer_id() {
        return reviewer_id;
    }

    public void setReviewer_id(int reviewer_id) {
        this.reviewer_id = reviewer_id;
    }

    public int getDetailed_sum_status() {
        return detailed_sum_status;
    }

    public void setDetailed_sum_status(int detailed_sum_status) {
        this.detailed_sum_status = detailed_sum_status;
    }

    public int getExec_sum_status() {
        return exec_sum_status;
    }

    public void setExec_sum_status(int exec_sum_status) {
        this.exec_sum_status = exec_sum_status;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public int getBenchmark_per() {
        return benchmark_per;
    }

    public void setBenchmark_per(int benchmark_per) {
        this.benchmark_per = benchmark_per;
    }

    public String getAuditor_fname() {
        return auditor_fname;
    }

    public void setAuditor_fname(String auditor_fname) {
        this.auditor_fname = auditor_fname;
    }

    public String getAuditor_lname() {
        return auditor_lname;
    }

    public void setAuditor_lname(String auditor_lname) {
        this.auditor_lname = auditor_lname;
    }

    public String getAuditor_email() {
        return auditor_email;
    }

    public void setAuditor_email(String auditor_email) {
        this.auditor_email = auditor_email;
    }

    /*public int getAuditor_phone() {
        return auditor_phone;
    }

    public void setAuditor_phone(int auditor_phone) {
        this.auditor_phone = auditor_phone;
    }*/

    public String getAuditor_phone() {
        return auditor_phone;
    }

    public void setAuditor_phone(String auditor_phone) {
        this.auditor_phone = auditor_phone;
    }

    public int getLocation_id() {
        return location_id;
    }

    public void setLocation_id(int location_id) {
        this.location_id = location_id;
    }

    public String getLocation_title() {
        return location_title;
    }

    public void setLocation_title(String location_title) {
        this.location_title = location_title;
    }

    public int getCountry_id() {
        return country_id;
    }

    public void setCountry_id(int country_id) {
        this.country_id = country_id;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public int getCity_id() {
        return city_id;
    }

    public void setCity_id(int city_id) {
        this.city_id = city_id;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public int getReport_status() {
        return report_status;
    }

    public void setReport_status(int report_status) {
        this.report_status = report_status;
    }

    public String getReviewer_feedback() {
        return reviewer_feedback;
    }

    public void setReviewer_feedback(String reviewer_feedback) {
        this.reviewer_feedback = reviewer_feedback;
    }

    public int getReviewer_rating() {
        return reviewer_rating;
    }

    public void setReviewer_rating(int reviewer_rating) {
        this.reviewer_rating = reviewer_rating;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public String getReviewer_fname() {
        return reviewer_fname;
    }

    public void setReviewer_fname(String reviewer_fname) {
        this.reviewer_fname = reviewer_fname;
    }

    public String getReviewer_lname() {
        return reviewer_lname;
    }

    public void setReviewer_lname(String reviewer_lname) {
        this.reviewer_lname = reviewer_lname;
    }

    public String getReviewer_email() {
        return reviewer_email;
    }

    public void setReviewer_email(String reviewer_email) {
        this.reviewer_email = reviewer_email;
    }

    public String getReviewer_phone() {
        return reviewer_phone;
    }

    public void setReviewer_phone(String reviewer_phone) {
        this.reviewer_phone = reviewer_phone;
    }

    public int getTotal_obtained_mark() {
        return total_obtained_mark;
    }

    public void setTotal_obtained_mark(int total_obtained_mark) {
        this.total_obtained_mark = total_obtained_mark;
    }

    public int getTotal_max_mark() {
        return total_max_mark;
    }

    public void setTotal_max_mark(int total_max_mark) {
        this.total_max_mark = total_max_mark;
    }

    public String getCategory_title() {
        return category_title;
    }

    public void setCategory_title(String category_title) {
        this.category_title = category_title;
    }

    public int getAudit_type_id() {
        return audit_type_id;
    }

    public void setAudit_type_id(int audit_type_id) {
        this.audit_type_id = audit_type_id;
    }

    public String getAudit_type() {
        return audit_type;
    }

    public void setAudit_type(String audit_type) {
        this.audit_type = audit_type;
    }

    public String getCreator_fname() {
        return creator_fname;
    }

    public void setCreator_fname(String creator_fname) {
        this.creator_fname = creator_fname;
    }

    public String getCreator_lname() {
        return creator_lname;
    }

    public void setCreator_lname(String creator_lname) {
        this.creator_lname = creator_lname;
    }

    public int getCreator_role() {
        return creator_role;
    }

    public void setCreator_role(int creator_role) {
        this.creator_role = creator_role;
    }

    public int getCreated_by() {
        return created_by;
    }

    public void setCreated_by(int created_by) {
        this.created_by = created_by;
    }

    public String getBrand_std_status_name() {
        return brand_std_status_name;
    }

    public void setBrand_std_status_name(String brand_std_status_name) {
        this.brand_std_status_name = brand_std_status_name;
    }

    public String getDetailed_sum_status_name() {
        return detailed_sum_status_name;
    }

    public void setDetailed_sum_status_name(String detailed_sum_status_name) {
        this.detailed_sum_status_name = detailed_sum_status_name;
    }

    public String getExec_sum_status_name() {
        return exec_sum_status_name;
    }

    public void setExec_sum_status_name(String exec_sum_status_name) {
        this.exec_sum_status_name = exec_sum_status_name;
    }
}
