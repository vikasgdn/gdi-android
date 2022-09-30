package com.gdi.activity;

import android.app.Application;
import android.net.Uri;

import com.gdi.activity.internalaudit.model.audit.BrandStandard.BrandStandardQuestion;
import com.gdi.activity.internalaudit.model.audit.BrandStandard.BrandStandardSection;

import java.util.ArrayList;
import java.util.List;


public class GDIApplication extends Application {

    private List<Uri> mAttachImageList;
    private ArrayList<BrandStandardQuestion> mSubQuestionForOptions;
    private ArrayList<BrandStandardSection> mBrandStandardSectionList;



    @Override
    public void onCreate() {
        super.onCreate();
       // handleSSLHandshake();
    }


    public List<Uri> getmAttachImageList()
    {
        return mAttachImageList;
    }
    public void setmAttachImageList(List<Uri> mAttachImageList) {
        this.mAttachImageList = mAttachImageList;
    }


    public ArrayList<BrandStandardQuestion> getmSubQuestionForOptions() {
        return mSubQuestionForOptions;
    }

    public void setmSubQuestionForOptions(ArrayList<BrandStandardQuestion> mSubQuestionForOptions) {
        this.mSubQuestionForOptions = mSubQuestionForOptions;
    }

    public ArrayList<BrandStandardSection> getmBrandStandardSectionList() {
        return mBrandStandardSectionList;
    }

    public void setmBrandStandardSectionList(ArrayList<BrandStandardSection> mBrandStandardSectionList) {
        this.mBrandStandardSectionList = mBrandStandardSectionList;
    }


}
