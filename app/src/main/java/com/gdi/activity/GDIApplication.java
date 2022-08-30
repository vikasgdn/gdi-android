package com.gdi.activity;

import android.app.Application;
import android.net.Uri;

import java.util.List;


public class GDIApplication extends Application {

    private List<Uri> mAttachImageList;
    public List<Uri> getmAttachImageList()
    {
        return mAttachImageList;
    }

    public void setmAttachImageList(List<Uri> mAttachImageList) {
        this.mAttachImageList = mAttachImageList;
    }


    @Override
    public void onCreate() {
        super.onCreate();
       // handleSSLHandshake();
    }



}
