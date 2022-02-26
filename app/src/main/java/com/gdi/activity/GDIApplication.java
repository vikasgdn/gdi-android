package com.gdi.activity;

import android.app.Application;
import android.net.Uri;
import android.util.Log;

import com.gdi.R;
import com.gdi.model.audit.BrandStandard.BrandStandardSection;
import com.gdi.model.audit.BrandStandard.BrandStandardSectionNew;

import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public class GDIApplication extends Application {

    private List<BrandStandardSectionNew> mSubSectionList;
    private ArrayList<BrandStandardSection> mBrandStandardSectionList;
   // private List<String> mAttachImageListStr;

    private List<Uri> mAttachImageList;
    public List<Uri> getmAttachImageList()
    {
        return mAttachImageList;
    }

    public void setmAttachImageList(List<Uri> mAttachImageList) {
        this.mAttachImageList = mAttachImageList;
    }


    public ArrayList<BrandStandardSection> getmBrandStandardSectionList() {
        return mBrandStandardSectionList;
    }

    public void setmBrandStandardSectionList(ArrayList<BrandStandardSection> mBrandStandardSectionList) {
        this.mBrandStandardSectionList = mBrandStandardSectionList;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        handleSSLHandshake();
    }


    public static void handleSSLHandshake() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession session) {
                    Log.e("host", arg0+":"+session.getCipherSuite());
                    return arg0.compareTo("api.gdiworldwide.com")==0;

                }
            });
        } catch (Exception ignored) {
        }
    }


    public List<BrandStandardSectionNew> getmSubSectionList() {
        return mSubSectionList;
    }

    public void setmSubSectionList(List<BrandStandardSectionNew> mSubSectionList) {
        this.mSubSectionList = mSubSectionList;
    }

   /* public List<String> getmAttachImageListStr() {
        return mAttachImageListStr;
    }

    public void setmAttachImageListStr(List<String> mAttachImageListStr) {
        this.mAttachImageListStr = mAttachImageListStr;
    }*/
}
