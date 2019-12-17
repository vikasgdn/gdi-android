package com.gdi.activity.MysteryAuditReport;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.gdi.R;
import com.gdi.activity.BaseActivity;
import com.gdi.activity.SignInActivity;
import com.gdi.adapter.AuditAdapter;
import com.gdi.api.ApiEndPoints;
import com.gdi.api.GetReportRequest;
import com.gdi.api.FilterRequest;
import com.gdi.api.SendToEmailRequest;
import com.gdi.api.VolleyNetworkRequest;
import com.gdi.model.reportaudit.ReportAuditInfo;
import com.gdi.model.reportaudit.ReportAuditRootObject;
import com.gdi.model.reportaudit.DashBoardInfo;
import com.gdi.model.reportaudit.DepatmentOverallInfo;
import com.gdi.model.reportaudit.HotelOverallInfo;
import com.gdi.model.filter.BrandFilterRootObject;
import com.gdi.model.filter.BrandsInfo;
import com.gdi.model.filter.CampaignFilterRootObject;
import com.gdi.model.filter.CampaignsInfo;
import com.gdi.model.filter.FilterCityInfo;
import com.gdi.model.filter.FilterCountryInfo;
import com.gdi.model.filter.FilterInfo;
import com.gdi.model.filter.FilterLocationInfo;
import com.gdi.model.filter.FilterLocationModel;
import com.gdi.model.filter.LocationFilterRootObject;
import com.gdi.utils.ApiResponseKeys;
import com.gdi.utils.AppLogger;
import com.gdi.utils.AppPrefs;
import com.gdi.utils.AppUtils;
import com.gdi.utils.DownloadExcelTask;
import com.gdi.utils.DownloadPdfTask;
import com.gdi.utils.Validation;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReportAuditActivity extends BaseActivity implements
        DownloadPdfTask.PDFDownloadFinishedListner,
        DownloadExcelTask.DownloadExcelFinishedListner, View.OnClickListener {

    @BindView(R.id.audit_recycler)
    RecyclerView list1;
    @BindView(R.id.btn_search)
    Button search;
    @BindView(R.id.spinner_brand)
    Spinner brandSearch;
    @BindView(R.id.spinner_audit_round)
    Spinner auditRoundSearch;
    @BindView(R.id.spinner_country)
    Spinner countrySearch;
    @BindView(R.id.spinner_city)
    Spinner citySearch;
    @BindView(R.id.spinner_location)
    Spinner locationSearch;
    @BindView(R.id.dashboard_score_txt)
    TextView dashboardScore;
    @BindView(R.id.dashboard_city_txt)
    TextView dashboardCity;
    @BindView(R.id.dashboard_country_txt)
    TextView dashboardCountry;
    @BindView(R.id.dashboard_pdf_icon)
    ImageView dashboardPdfIcon;
    @BindView(R.id.dashboard_mail_icon)
    ImageView dashboardMailIcon;
    @BindView(R.id.hotel_score_txt)
    TextView hotelScore;
    @BindView(R.id.hotel_city_txt)
    TextView hotelCity;
    @BindView(R.id.hotel_country_txt)
    TextView hotelCountry;
    @BindView(R.id.hotel_pdf_icon)
    ImageView hotelPdfIcon;
    @BindView(R.id.hotel_excel_icon)
    ImageView hotelExcelIcon;
    @BindView(R.id.hotel_mail_icon)
    ImageView hotelMailIcon;
    @BindView(R.id.expandLayout2)
    RelativeLayout expandLayout;
    @BindView(R.id.dashboard_card)
    CardView dashboardLayout;
    @BindView(R.id.hotel_overall_card)
    CardView hotelOverallLayout;
    @BindView(R.id.audit_department_card)
    CardView departmentLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.expand_icon)
    ImageView expandIcon;
    @BindView(R.id.brand_layout)
    CardView brandLayout;
    @BindView(R.id.audit_round_layout)
    CardView auditRoundLayout;
    @BindView(R.id.city_layout)
    RelativeLayout cityLayout;
    @BindView(R.id.country_layout)
    RelativeLayout countryLayout;
    @BindView(R.id.location_layout)
    RelativeLayout locationLayout;
    @BindView(R.id.tv_filter_brand)
    TextView tvFilterBrand;
    @BindView(R.id.tv_filter_audit_round)
    TextView tvFilterAuditRound;
    @BindView(R.id.tv_filter_city)
    TextView tvFilterCity;
    @BindView(R.id.tv_filter_country)
    TextView tvFilterCountry;
    @BindView(R.id.tv_filter_location)
    TextView tvFilterLocation;
    @BindView(R.id.brand_icon)
    ImageView brandIcon;
    @BindView(R.id.audit_icon)
    ImageView auditIcon;

    Context context;
    private ArrayList<DepatmentOverallInfo> auditDepartmentList;
    private ReportAuditInfo reportAuditInfos;
    private FilterInfo filterInfo;
    private ArrayList<BrandsInfo> brandList = new ArrayList<>();
    private ArrayList<CampaignsInfo> campaignList = new ArrayList<>();
    private ArrayList<FilterCountryInfo> countryList;
    private ArrayList<FilterCityInfo> cityList;
    private ArrayList<FilterLocationInfo> locationList;
    private DashBoardInfo dashBoardInfo;
    private HotelOverallInfo hotelOverallInfo;
    private AuditAdapter auditAdapter;
    private String brandId = "";
    private String campaignId = "";
    private String countryId = "";
    private String cityId = "";
    private String locationId = "";
    private String auditUrl = "";
    private boolean expand = false;
    private int REQUEST_FOR_READ = 1;
    private static final int REQUEST_FOR_WRITE_PDF = 1;
    private static final int REQUEST_FOR_WRITE_EXCEL = 10;
    private static final String TAG = ReportAuditActivity.class.getSimpleName();
    private boolean isFirstTime = true;
    private boolean isFirstCompaignLoad = true;
    private boolean isFirstCountryLoad = true;
    private boolean isFirstCityLoad = true;

    @Override
    protected void onResume() {
        super.onResume();
        AppUtils.hideKeyboard(ReportAuditActivity.this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_audit);
        context = this;
        ButterKnife.bind(ReportAuditActivity.this);
        initView();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_search:
                AppPrefs.setFilterBrand(context, brandSearch.getSelectedItemPosition());
                AppPrefs.setFilterCampaign(context, auditRoundSearch.getSelectedItemPosition());
                AppPrefs.setFilterCity(context, citySearch.getSelectedItemPosition());
                AppPrefs.setFilterCountry(context, countrySearch.getSelectedItemPosition());
                AppPrefs.setFilterLocation(context, locationSearch.getSelectedItemPosition());
                view.playSoundEffect(android.view.SoundEffectConstants.CLICK);
                auditList();
                break;
            case R.id.expandLayout2:
                if (!expand) {
                    expand = true;
                    list1.setVisibility(View.VISIBLE);
                    expandIcon.setImageResource(R.drawable.compress_icon);
                    setAuditDeparment();
                } else if (expand) {
                    expand = false;
                    list1.setVisibility(View.GONE);
                    expandIcon.setImageResource(R.drawable.expand_icon);
                }
                break;
            case R.id.dashboard_pdf_icon:
                downloadPdf(dashBoardInfo.getReport_urls().getPdf());
                break;
            case R.id.dashboard_mail_icon:
                sentEmail(dashBoardInfo.getReport_urls().getEmail());
                break;
            case R.id.hotel_pdf_icon:
                downloadPdf(hotelOverallInfo.getReport_urls().getPdf());
                break;
            case R.id.hotel_excel_icon:
                downloadExcel(hotelOverallInfo.getReport_urls().getExcel());
                break;
            case R.id.hotel_mail_icon:
                sentEmail(hotelOverallInfo.getReport_urls().getEmail());
                break;
        }
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        setActionBar();
        search = findViewById(R.id.btn_search);
        list1 = findViewById(R.id.audit_recycler);
        dashboardScore = findViewById(R.id.dashboard_score_txt);
        dashboardCity = findViewById(R.id.dashboard_city_txt);
        dashboardCountry = findViewById(R.id.dashboard_country_txt);
        dashboardPdfIcon = findViewById(R.id.dashboard_pdf_icon);
        dashboardMailIcon = findViewById(R.id.dashboard_mail_icon);
        hotelScore = findViewById(R.id.hotel_score_txt);
        hotelCity = findViewById(R.id.hotel_city_txt);
        hotelCountry = findViewById(R.id.hotel_country_txt);
        hotelPdfIcon = findViewById(R.id.hotel_pdf_icon);
        hotelExcelIcon = findViewById(R.id.hotel_excel_icon);
        hotelMailIcon = findViewById(R.id.hotel_mail_icon);
        brandSearch = findViewById(R.id.spinner_brand);
        auditRoundSearch = findViewById(R.id.spinner_audit_round);
        countrySearch = findViewById(R.id.spinner_country);
        citySearch = findViewById(R.id.spinner_city);
        locationSearch = findViewById(R.id.spinner_location);
        expandLayout = findViewById(R.id.expandLayout2);
        dashboardLayout = findViewById(R.id.dashboard_card);
        hotelOverallLayout = findViewById(R.id.hotel_overall_card);
        departmentLayout = findViewById(R.id.audit_department_card);
        expandIcon = findViewById(R.id.expand_icon);
        brandLayout = findViewById(R.id.brand_layout);
        auditRoundLayout = findViewById(R.id.audit_round_layout);
        cityLayout = findViewById(R.id.city_layout);
        countryLayout = findViewById(R.id.country_layout);
        locationLayout = findViewById(R.id.location_layout);
        tvFilterBrand = findViewById(R.id.tv_filter_brand);
        tvFilterAuditRound = findViewById(R.id.tv_filter_audit_round);
        tvFilterCountry = findViewById(R.id.tv_filter_country);
        tvFilterCity = findViewById(R.id.tv_filter_city);
        tvFilterLocation = findViewById(R.id.tv_filter_location);
        brandIcon = findViewById(R.id.brand_icon);
        auditIcon = findViewById(R.id.audit_icon);
        String android_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        AppLogger.e("Android Id: ", android_id);
        getBrandFilter();//set filter by call filet api
        expandLayout.setOnClickListener(this);
        search.setOnClickListener(this);
        dashboardPdfIcon.setOnClickListener(this);
        dashboardMailIcon.setOnClickListener(this);
        hotelPdfIcon.setOnClickListener(this);
        hotelExcelIcon.setOnClickListener(this);
        hotelMailIcon.setOnClickListener(this);
    }

    public void auditList() {
        showProgressDialog();
        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AppLogger.e(TAG, "Audit Response: " + response);
                try {
                    JSONObject object = new JSONObject(response);

                    if (!object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        ReportAuditRootObject reportAuditRootObject = new GsonBuilder().create()
                                .fromJson(object.toString(), ReportAuditRootObject.class);
                        if (reportAuditRootObject.getData() != null &&
                                reportAuditRootObject.getData().toString().length() > 0) {
                            reportAuditInfos = reportAuditRootObject.getData();
                            setAuditDashboard();
                            setAuditHotelOverall();
                            dashboardLayout.setVisibility(View.VISIBLE);
                            hotelOverallLayout.setVisibility(View.VISIBLE);
                            departmentLayout.setVisibility(View.VISIBLE);
                        }
                    } else if (object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        AppUtils.toast((BaseActivity) context,
                                object.getString(ApiResponseKeys.RES_KEY_MESSAGE));
                        dashboardLayout.setVisibility(View.GONE);
                        hotelOverallLayout.setVisibility(View.GONE);
                        departmentLayout.setVisibility(View.GONE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                hideProgressDialog();
            }

        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
                AppLogger.e(TAG, "Audit Error: " + error.getMessage());
                AppUtils.toast((BaseActivity) context, "Server temporary unavailable, Please try again");

            }
        };
        AppLogger.e(TAG, "Brand Id: " + brandId);
        AppLogger.e(TAG, "Campaign Id: " + campaignId);
        AppLogger.e(TAG, "Country Id: " + countryId);
        AppLogger.e(TAG, "City Id: " + cityId);
        AppLogger.e(TAG, "Location Id: " + locationId);
        String auditUrl = ApiEndPoints.AUDIT + "?"
                + "brand_id=" + brandId + "&"
                + "campaign_id=" + campaignId + "&"
                + "location_id=" + locationId + "&"
                + "country_id=" + countryId + "&"
                + "city_id=" + cityId;
        GetReportRequest getReportRequest = new GetReportRequest(AppPrefs.getAccessToken(context),
                auditUrl, stringListener, errorListener);
        VolleyNetworkRequest.getInstance(context).addToRequestQueue(getReportRequest);
    }

    private void setAuditDashboard() {
        dashBoardInfo = reportAuditInfos.getDashboard();
        AppUtils.setScoreColor(dashBoardInfo.getScore(), dashboardScore, context);
        dashboardScore.setText(dashBoardInfo.getScore());
        dashboardCity.setText(dashBoardInfo.getCity());
        dashboardCountry.setText(dashBoardInfo.getCountry());
    }

    private void setAuditHotelOverall() {
        hotelOverallInfo = reportAuditInfos.getHotel_overall();
        AppUtils.setScoreColor(hotelOverallInfo.getScore(), hotelScore, context);
        hotelScore.setText(hotelOverallInfo.getScore());
        hotelCity.setText(hotelOverallInfo.getCity());
        hotelCountry.setText(hotelOverallInfo.getCountry());
    }

    private void setAuditDeparment() {
        auditDepartmentList = new ArrayList<>();
        auditDepartmentList.clear();
        auditDepartmentList.addAll(reportAuditInfos.getDepartment_overall());
        AppLogger.e(TAG, "List Size: " + auditDepartmentList.size());
        auditAdapter = new AuditAdapter(context, auditDepartmentList);
        list1.setLayoutManager(new LinearLayoutManager(context));
        list1.setAdapter(auditAdapter);
    }

    private void getBrandFilter() {
        showProgressDialog();
        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AppLogger.e(TAG, "Filter Response: " + response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (!object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        BrandFilterRootObject brandFilterRootObject = new GsonBuilder().create()
                                .fromJson(object.toString(), BrandFilterRootObject.class);
                        if (brandFilterRootObject.getData() != null &&
                                brandFilterRootObject.getData().toString().length() > 0) {
                            setBrandFilter(brandFilterRootObject.getData());
                        }

                    } else if (object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        AppUtils.toast((BaseActivity) context,
                                object.getString(ApiResponseKeys.RES_KEY_MESSAGE));
                        finish();
                        startActivity(new Intent(context, SignInActivity.class));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                hideProgressDialog();
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
                AppLogger.e(TAG, "Filter Error: " + error.getMessage());
                AppUtils.toast((BaseActivity) context, "Server temporary unavailable, Please try again");

            }
        };
        String brandUrl = ApiEndPoints.FILTERBRAND;
        FilterRequest filterRequest = new FilterRequest(brandUrl,
                AppPrefs.getAccessToken(context), stringListener, errorListener);
        VolleyNetworkRequest.getInstance(context).addToRequestQueue(filterRequest);
    }

    private void getCampaignFilter(String brandId) {
        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AppLogger.e(TAG, "Filter Response: " + response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (!object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        CampaignFilterRootObject campaignFilterRootObject = new GsonBuilder().create()
                                .fromJson(object.toString(), CampaignFilterRootObject.class);
                        if (campaignFilterRootObject.getData() != null &&
                                campaignFilterRootObject.getData().toString().length() > 0) {
                            setCampaignFilter(campaignFilterRootObject.getData());
                        }

                    } else if (object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        AppUtils.toast((BaseActivity) context,
                                object.getString(ApiResponseKeys.RES_KEY_MESSAGE));
                        finish();
                        startActivity(new Intent(context, SignInActivity.class));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
                AppLogger.e(TAG, "Filter Error: " + error.getMessage());

            }
        };
        String campaignUrl = ApiEndPoints.FILTERCAMPAIGN + "?"
                + "brand_id=" + brandId;
        FilterRequest filterRequest = new FilterRequest(campaignUrl,
                AppPrefs.getAccessToken(context), stringListener, errorListener);
        VolleyNetworkRequest.getInstance(context).addToRequestQueue(filterRequest);
    }

    private void getLocationFilter() {
        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AppLogger.e(TAG, "Filter Response: " + response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (!object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        LocationFilterRootObject locationCampaignRootObject = new GsonBuilder().create()
                                .fromJson(object.toString(), LocationFilterRootObject.class);
                        if (locationCampaignRootObject.getData() != null &&
                                locationCampaignRootObject.getData().toString().length() > 0) {
                            FilterLocationModel locationModel = new FilterLocationModel();
                            locationModel = locationCampaignRootObject.getData();
                            setCountryFilter(locationModel);
                        }

                    } else if (object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        AppUtils.toast((BaseActivity) context,
                                object.getString(ApiResponseKeys.RES_KEY_MESSAGE));
                        finish();
                        startActivity(new Intent(context, SignInActivity.class));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
                AppLogger.e(TAG, "Filter Error: " + error.getMessage());

            }
        };
        String locationUrl = ApiEndPoints.FILTERLOCATION + "?"
                + "brand_id=" + brandId + "&"
                + "campaign_id=" + campaignId;
        FilterRequest filterRequest = new FilterRequest(locationUrl,
                AppPrefs.getAccessToken(context), stringListener, errorListener);
        VolleyNetworkRequest.getInstance(context).addToRequestQueue(filterRequest);
    }

    private void setBrandFilter(ArrayList<BrandsInfo> brandsInfos) {
        final ArrayList<BrandsInfo> brandList = new ArrayList<>();
        BrandsInfo brandsInfo = new BrandsInfo();
        brandsInfo.setBrand_id(0);
        brandsInfo.setBrand_name("Select Brand");
        brandList.add(brandsInfo);
        brandList.addAll(brandsInfos);
        ArrayAdapter<String> brandAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_dropdown_item);
        for (int i = 0; i < brandList.size(); i++) {
            brandAdapter.add(brandList.get(i).getBrand_name());
        }
        brandSearch.setAdapter(brandAdapter);
        brandSearch.setSelection(AppPrefs.getFilterBrand(context));
        brandSearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (isFirstTime) {
                    isFirstTime = false;
                    if (AppPrefs.getFilterBrand(context) > 0) {
                        brandId = "" + brandList.get(position).getBrand_id();
                        getCampaignFilter(brandId);
                    } else {
                        auditRoundSearch.setSelection(0);
                        citySearch.setSelection(0);
                        countrySearch.setSelection(0);
                        locationSearch.setSelection(0);
                    }
                } else {
                    if (position > 0) {
                        auditRoundSearch.setSelection(0);
                        citySearch.setSelection(0);
                        countrySearch.setSelection(0);
                        locationSearch.setSelection(0);
                        AppPrefs.setFilterBrand(context,position);
                        AppPrefs.setFilterCampaign(context,0);
                        AppPrefs.setFilterCountry(context,0);
                        AppPrefs.setFilterCity(context,0);
                        AppPrefs.setFilterLocation(context,0);
                        brandId = "" + brandList.get(position).getBrand_id();
                        getCampaignFilter(brandId);
                        AppLogger.e(TAG, "Brand Id: " + brandId);
                        AppLogger.e(TAG, "Brand Position: " + AppPrefs.getFilterBrand(context));
                    } else {
                        auditRoundSearch.setSelection(0);
                        citySearch.setSelection(0);
                        countrySearch.setSelection(0);
                        locationSearch.setSelection(0);
                        brandId = "";
                        campaignId = "";
                        countryId = "";
                        cityId = "";
                        locationId = "";

                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void setCampaignFilter(ArrayList<CampaignsInfo> campaignsInfos) {
        final ArrayList<CampaignsInfo> campaignList = new ArrayList<>();
        CampaignsInfo campaignsInfo = new CampaignsInfo();
        campaignsInfo.setCampaign_id(0);
        campaignsInfo.setCampaign_title("Select Round");
        campaignList.add(campaignsInfo);
        campaignList.addAll(campaignsInfos);
        ArrayAdapter<String> campaignAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_dropdown_item);
        for (int i = 0; i < campaignList.size(); i++) {
            campaignAdapter.add(campaignList.get(i).getCampaign_title());
        }
        auditRoundSearch.setAdapter(campaignAdapter);
        auditRoundSearch.setSelection(AppPrefs.getFilterCampaign(context));
        auditRoundSearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isFirstCompaignLoad) {
                    isFirstCompaignLoad = false;
                    if (AppPrefs.getFilterCampaign(context) > 0) {
                        campaignId = "" + campaignList.get(position).getCampaign_id();
                        getLocationFilter();
                    } else {
                        citySearch.setSelection(0);
                        countrySearch.setSelection(0);
                        locationSearch.setSelection(0);
                    }

                } else {
                    if (position > 0) {
                        AppPrefs.setFilterCampaign(context, position);
                        AppPrefs.setFilterCity(context, 0);
                        AppPrefs.setFilterCountry(context, 0);
                        AppPrefs.setFilterLocation(context, 0);
                        campaignId = "" + campaignList.get(position).getCampaign_id();
                        getLocationFilter();
                        AppLogger.e(TAG, "Campaign Id: " + campaignId);
                        AppLogger.e(TAG, "Campaign position: " + AppPrefs.getFilterCampaign(context));
                    } else {
                        citySearch.setSelection(0);
                        countrySearch.setSelection(0);
                        locationSearch.setSelection(0);
                        campaignId = "";
                        countryId = "";
                        cityId = "";
                        locationId = "";
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //auditRoundSearch.setSelection(AppPrefs.getFilterCampaign(context));
    }

    private void setCountryFilter(final FilterLocationModel locationModel) {
        final ArrayList<FilterCountryInfo> countryList = new ArrayList<>();
        FilterCountryInfo countryInfo = new FilterCountryInfo();
        countryInfo.setCountry_id(0);
        countryInfo.setCountry_name("All");
        countryList.add(countryInfo);
        countryList.addAll(locationModel.getCountries());
        ArrayAdapter<String> brandAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_dropdown_item);
        for (int i = 0; i < countryList.size(); i++) {
            brandAdapter.add(countryList.get(i).getCountry_name());
        }
        countrySearch.setAdapter(brandAdapter);
        countrySearch.setSelection(AppPrefs.getFilterCountry(context));
        //countryId = "" + countryList.get(AppPrefs.getFilterCountry(context)).getCountry_id();
        countryId = "" + AppPrefs.getFilterCountry(context);
        countrySearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isFirstCountryLoad) {
                    isFirstCountryLoad = false;
                    if (AppPrefs.getFilterCountry(context) > 0) {
                        countryId = "" + countryList.get(position).getCountry_id();
                        setCityFilter(locationModel);

                    } else {
                        setCityFilter(locationModel);
                        cityId = String.valueOf(AppPrefs.getFilterCity(context));
                    }
                } else {
                    countryId = "" + countryList.get(position).getCountry_id();
                    AppPrefs.setFilterCountry(context, position);
                    setCityFilter(locationModel);
                    citySearch.setSelection(0);
                    locationSearch.setSelection(0);
                    cityId = "";
                    locationId = "";
                    AppPrefs.setFilterCity(context, 0);
                    AppPrefs.setFilterLocation(context, 0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void setCityFilter(final FilterLocationModel locationModel) {
        final ArrayList<FilterCityInfo> cityList = new ArrayList<>();
        FilterCityInfo cityInfo = new FilterCityInfo();
        cityInfo.setCity_id(0);
        cityInfo.setCity_name("All");
        cityList.add(cityInfo);
        if (countryId.equals("0")) {
            cityList.addAll(locationModel.getCities());
        }else {
            for(int i = 0 ; i < locationModel.getCities().size() ; i++){
                if (countryId.equals(String.valueOf(locationModel.getCities().get(i).getCountry_id()))){
                    cityList.add(locationModel.getCities().get(i));
                }
            }
        }
        ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_dropdown_item);
        for (int i = 0; i < cityList.size(); i++) {
            cityAdapter.add(cityList.get(i).getCity_name());
        }
        citySearch.setAdapter(cityAdapter);
        citySearch.setSelection(AppPrefs.getFilterCity(context));
        citySearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isFirstCityLoad) {
                    isFirstCityLoad = false;
                    if (AppPrefs.getFilterCountry(context) > 0) {
                        cityId = "" + cityList.get(position).getCity_id();
                        setLocationFilter(locationModel);
                    } else {
                        setLocationFilter(locationModel);
                    }
                } else {
                    cityId = "" + cityList.get(position).getCity_id();
                    AppPrefs.setFilterCity(context, position);
                    setLocationFilter(locationModel);
                    locationSearch.setSelection(0);
                    locationId = "";
                    AppPrefs.setFilterLocation(context, 0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setLocationFilter(FilterLocationModel locationModel) {
        final ArrayList<FilterLocationInfo> locationList = new ArrayList<>();
        FilterLocationInfo filterLocationInfo = new FilterLocationInfo();
        filterLocationInfo.setLocation_id(0);
        filterLocationInfo.setLocation_name("All");
        locationList.add(filterLocationInfo);
        if (countryId.equals("0")) {
            if (cityId.equals("0")) {
                locationList.addAll(locationModel.getLocations());
            }else {
                for(int i = 0 ; i < locationModel.getLocations().size() ; i++){
                    if (cityId.equals(String.valueOf(locationModel.getLocations().get(i).getCity_id()))){
                        locationList.add(locationModel.getLocations().get(i));
                    }
                }
            }
        }else {
            if (cityId.equals("0")) {
                for(int i = 0 ; i < locationModel.getLocations().size() ; i++){
                    if (countryId.equals(String.valueOf(locationModel.getLocations().get(i).getCountry_id()))){
                        locationList.add(locationModel.getLocations().get(i));
                    }
                }
            }else {
                for(int i = 0 ; i < locationModel.getLocations().size() ; i++){
                    if (cityId.equals(String.valueOf(locationModel.getLocations().get(i).getCity_id()))){
                        locationList.add(locationModel.getLocations().get(i));
                    }
                }
            }
        }
        ArrayAdapter<String> locationAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_dropdown_item);
        for (int i = 0; i < locationList.size(); i++) {
            locationAdapter.add(locationList.get(i).getLocation_name());
        }
        locationSearch.setAdapter(locationAdapter);
        locationSearch.setSelection(AppPrefs.getFilterLocation(context));
        locationSearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                locationId = "" + locationList.get(position).getLocation_id();
                AppPrefs.setFilterLocation(context, position);
                AppLogger.e(TAG, "Location Id: " + locationId);
                AppLogger.e(TAG, "Location position: " + AppPrefs.getFilterLocation(context));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void setActionBar() {
        initToolbar(toolbar);
        setTitle("Audit");
        enableBack(true);
        enableBackPressed();
    }

    private boolean validate(EditText edit_email) {
        boolean validate = true;
        if (edit_email.getText().toString().length() <= 0) {
            validate = false;
            edit_email.setError(getString(R.string.enter_email));
        } else if (!Validation.isValidEmail(edit_email.getText().toString())) {
            validate = false;
            edit_email.setError("Enter a valid mail id");
        }
        return validate;
    }

    public void sentEmail(String apiUrl) {
        emailAttachment(apiUrl);
    }

    private void emailAttachment(final String apiUrl) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        final View view = layoutInflater.inflate(R.layout.send_email_layout, null);
        dialog.setView(view);

        final EditText emailId = view.findViewById(R.id.send_email_edt_txt);

        dialog.setTitle("Enter Email");

        dialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AppUtils.hideKeyboard(context, view);
                dialog.dismiss();
            }
        });
        dialog.setPositiveButton(R.string.send, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AppUtils.hideKeyboard(context, view);
                if (validate(emailId)) {
                    AppLogger.e(TAG, "Send to email Email: " + emailId.getText().toString());
                    String mainUrl = apiUrl + emailId.getText().toString();
                    AppLogger.e(TAG, "Send to email mail url: " + mainUrl);
                    sendEmailApi(mainUrl);
                }
            }
        });
        dialog.create().show();
    }

    public void sendEmailApi(String url) {
        showProgressDialog();
        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AppLogger.e(TAG, "Send to email Response: " + response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (!object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        AppUtils.toast((BaseActivity) context,
                                object.getString(ApiResponseKeys.RES_KEY_MESSAGE));
                    } else if (object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        AppUtils.toast((BaseActivity) context,
                                object.getString(ApiResponseKeys.RES_KEY_MESSAGE));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                hideProgressDialog();
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
                AppLogger.e(TAG, "Send to email Error: " + error.getMessage());
                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        // Now you can use any deserializer to make sense of data
                        JSONObject obj = new JSONObject(res);
                        String message = obj.getString("message");
                        Log.e("Error: ", "" + obj);
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        //AppUtils.toast((BaseActivity) context, message);
                    } catch (UnsupportedEncodingException e1) {
                        // Couldn't properly decode data to string
                        if (context != null) {
                            Toast.makeText(context, "Invalid Responce", Toast.LENGTH_SHORT).show();
                        }
                        e1.printStackTrace();
                    } catch (JSONException e2) {
                        if (context != null) {
                            Toast.makeText(context, "Invalid Responce", Toast.LENGTH_SHORT).show();
                        }
                        // returned data is not JSONObject?
                        e2.printStackTrace();
                    }
                }

            }
        };
        SendToEmailRequest sendToEmailRequest = new SendToEmailRequest(url,
                AppPrefs.getAccessToken(context), stringListener, errorListener);
        VolleyNetworkRequest.getInstance(context).addToRequestQueue(sendToEmailRequest);
    }

    public void downloadPdf(final String url) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(ReportAuditActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_FOR_WRITE_PDF);
        } else {
            DownloadPdfTask downloadTask = new DownloadPdfTask(context, url, ReportAuditActivity.this);
        }
    }

    public void downloadExcel(final String url) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(ReportAuditActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_FOR_WRITE_EXCEL);
        } else {
            DownloadExcelTask downloadTask = new DownloadExcelTask(context, url, ReportAuditActivity.this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_FOR_WRITE_PDF) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                // permission denied, boo! Disable the
                // functionality that depends on this permission.
            }
        }

        if (requestCode == REQUEST_FOR_WRITE_EXCEL) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                // permission denied, boo! Disable the
                // functionality that depends on this permission.
            }
        }

    }

    @Override
    public void onPDFDownloadFinished(String path) {

        File file = new File(path);
        Intent target = new Intent(Intent.ACTION_VIEW);
        target.setDataAndType(Uri.fromFile(file), "application/pdf");
        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        Intent intent = Intent.createChooser(target, "Open File");
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            // Instruct the user to install a PDF reader here, or something
        }
    }

    @Override
    public void onExcelDownloadFinished(String path) {
        File file = new File(path);
        Intent target = new Intent(Intent.ACTION_VIEW);
        target.setDataAndType(Uri.fromFile(file), "application/vnd.ms-excel");
        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        Intent intent = Intent.createChooser(target, "Open File");
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            // Instruct the user to install a PDF reader here, or something
        }
    }
}
