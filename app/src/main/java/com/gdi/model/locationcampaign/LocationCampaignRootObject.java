package com.gdi.model.locationcampaign;

import com.gdi.model.ReportUrlInfo;
import com.gdi.model.trendlocation.TrendLocationInfo;

import java.util.ArrayList;

public class LocationCampaignRootObject {

    boolean error;
    LocationCampaignInfo data;
    String message = "";

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public LocationCampaignInfo getData() {
        return data;
    }

    public void setData(LocationCampaignInfo data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
