package com.gdi.activity.internalaudit.model.audit.createaudit;


import java.util.ArrayList;

public class ActionFilterData {

    private ArrayList<PriorityBean> priorities;
    private ArrayList<AditorReviewBean> users;
    private ArrayList<SectionBean> sections;

    public ArrayList<PriorityBean> getPriorities() {
        return priorities;
    }

    public void setPriorities(ArrayList<PriorityBean> priorities) {
        this.priorities = priorities;
    }

    public ArrayList<AditorReviewBean> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<AditorReviewBean> users) {
        this.users = users;
    }

    public ArrayList<SectionBean> getSections() {
        return sections;
    }

    public void setSections(ArrayList<SectionBean> sections) {
        this.sections = sections;
    }
}
