package com.gdi.api;

/**
 * Created by Nitish on 3/31/2018.
 */

public class ApiEndPoints {

    //API URL'S
    //public static final String API_PRODUCTION_URL = "http://api.test.gdiworldwide.com/m/";
    public static final String API_PRODUCTION_URL = "https://api.gdiworldwide.com/m/";
    public static final String API_USER_IMAGES_URL = "https://s3-ap-southeast-1.amazonaws.com/lyli-new/users/";

    public static final String API_BASE_URL = API_PRODUCTION_URL;



    public static final String SIGNIN = API_BASE_URL + "login";
    public static final String LOGOUT = API_BASE_URL + "logout";
    public static final String RESETPASSWORD = API_BASE_URL + "password/change";
    public static final String CHANGEPASSWORD = API_BASE_URL + "password";
    public static final String SENDOTP = API_BASE_URL + "send_otp";
    public static final String FILTER = API_BASE_URL + "filter";
    public static final String FILTERBRAND = API_BASE_URL + "filter/brand";
    public static final String FILTERCAMPAIGN = API_BASE_URL + "filter/campaign";
    public static final String FILTERLOCATION = API_BASE_URL + "filter/location";
    public static final String AUDIT = API_BASE_URL + "report/audit";
    public static final String DASHBOARD = API_BASE_URL + "report/dashboard";
    public static final String OVERALLBRAND = API_BASE_URL + "report/overall/brand";
    public static final String DETAILEDSUMMARY = API_BASE_URL + "report/detailed/summary";
    public static final String EXECUTIVESUMMARY = API_BASE_URL + "report/executive/summary";
    public static final String HIGHLIGHTS = API_BASE_URL + "report/highlights";
    public static final String AUDIOIMAGE = API_BASE_URL + "report/audio_image";
    public static final String BACKHOUSE = API_BASE_URL + "report/back_house";
    public static final String INTEGRITY = API_BASE_URL + "report/integrity";
    public static final String FAQ = "https://api.gdiworldwide.com/report/faq";
    public static final String GETPROFILE = API_BASE_URL + "profile";
    public static final String ACTIONPLAN = API_BASE_URL + "action_plan";
    public static final String CITYCOMPSET = API_BASE_URL + "report/competition/city_compset";
    public static final String GLOBAL = API_BASE_URL + "report/competition/global";
    public static final String SECTIONGROUPSCORE = API_BASE_URL + "report/section_group_score";
    public static final String TRENDLOCATION = API_BASE_URL + "report/trend_location";
    public static final String LOCATIONCAMPAIGNSCORE = API_BASE_URL + "report/location_campaign_score";

    public static final String IAFILTER = API_BASE_URL + "ia/filter";
    public static final String IAAUDIT = API_BASE_URL + "ia/report/audit";
    public static final String IADASHBOARD = API_BASE_URL + "ia/report/dashboard";
    public static final String IADETAILEDSUMMARY = API_BASE_URL + "ia/report/detailed";
    public static final String IAEXECUTIVESUMMARY = API_BASE_URL + "ia/report/executive";
    public static final String IAAUDIOIMAGE = API_BASE_URL + "ia/report/audio_images";

    public static final String UPDATEPROFILE = API_BASE_URL + "profile";


}
