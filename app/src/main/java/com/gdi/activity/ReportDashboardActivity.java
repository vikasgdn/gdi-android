package com.gdi.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gdi.R;
import com.gdi.adapter.DashBoardHighestDeptAdapter;
import com.gdi.adapter.DashBoardLowestDeptAdapter;
import com.gdi.api.ApiEndPoints;
import com.gdi.api.DashboardRequest;
import com.gdi.api.FilterRequest;
import com.gdi.api.VolleyNetworkRequest;
import com.gdi.model.dashboard.CurrentVsLastInfo;
import com.gdi.model.dashboard.DashboardInfo;
import com.gdi.model.dashboard.DashboardRootObject;
import com.gdi.model.dashboard.GlobalInfo;
import com.gdi.model.dashboard.HighestDeparmentInfo;
import com.gdi.model.dashboard.LastFiveInfo;
import com.gdi.model.dashboard.LowestDepartmentInfo;
import com.gdi.model.dashboard.OverallInfo;
import com.gdi.model.dashboard.RanksInfo;
import com.gdi.model.filter.BrandFilterRootObject;
import com.gdi.model.filter.BrandsInfo;
import com.gdi.model.filter.CampaignFilterRootObject;
import com.gdi.model.filter.CampaignsInfo;
import com.gdi.model.filter.CountryInfo;
import com.gdi.model.filter.FilterInfo;
import com.gdi.model.filter.FilterRootObject;
import com.gdi.model.filter.FilterLocationInfo;
import com.gdi.model.filter.LocationFilterRootObject;
import com.gdi.utils.ApiResponseKeys;
import com.gdi.utils.AppConstant;
import com.gdi.utils.AppLogger;
import com.gdi.utils.AppPrefs;
import com.gdi.utils.AppUtils;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Highlight;
import com.github.mikephil.charting.utils.LargeValueFormatter;
import com.github.mikephil.charting.utils.PercentFormatter;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ReportDashboardActivity extends BaseActivity implements OnChartValueSelectedListener {

    @BindView(R.id.city_rank_txt)
    TextView cityRank;
    @BindView(R.id.country_rank_txt)
    TextView countryRank;
    @BindView(R.id.global_rank_txt)
    TextView globalRank;
    @BindView(R.id.city_rank_score)
    TextView cityRankScore;
    @BindView(R.id.country_rank_score)
    TextView countryRankScore;
    @BindView(R.id.global_rank_score)
    TextView globalRankScore;
    @BindView(R.id.dashboard_highest_dept_list)
    RecyclerView list1;
    @BindView(R.id.dashboard_lowest_dept_list)
    RecyclerView list2;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.spinner_brand)
    Spinner brandSearch;
    @BindView(R.id.spinner_audit_round)
    Spinner auditRoundSearch;
    @BindView(R.id.spinner_country)
    Spinner countrySearch;
    @BindView(R.id.spinner_location)
    Spinner locationSearch;
    @BindView(R.id.btn_search)
    Button search;
    @BindView(R.id.bar_chart)
    HorizontalBarChart gpBarChart;
    @BindView(R.id.pie_chart)
    PieChart pieChart;
    @BindView(R.id.bar_chart)
    BarChart barChart;
    @BindView(R.id.overall_layout)
    CardView overallLayout;
    @BindView(R.id.department_layout)
    CardView departmentLayout;
    @BindView(R.id.last5_layout)
    CardView last5Layout;
    @BindView(R.id.rank_layout)
    CardView rankLayout;
    @BindView(R.id.highest_dept_layout)
    CardView highestDepartmentalLayout;
    @BindView(R.id.lowest_dept_layout)
    CardView lowestDepartmentalLayout;
    @BindView(R.id.tv_audit_date)
    TextView tvAuditDate;
    @BindView(R.id.tv_hotel)
    TextView tvHotel;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.overall_text)
    TextView overall_text;
    @BindView(R.id.tv_gm)
    TextView tvGM;
    @BindView(R.id.tv_report_score)
    TextView tvReportScore;
    @BindView(R.id.tv_average_score)
    TextView tvAverageScore;
    @BindView(R.id.tv_top_score)
    TextView tvTopScore;
    @BindView(R.id.dashboard_text_layout)
    CardView dashBoardTextLayout;
    private FilterInfo filterInfo;
    private ArrayList<BrandsInfo> brandList;
    private ArrayList<CampaignsInfo> campaignList;
    private ArrayList<CountryInfo> countryList;
    private ArrayList<FilterLocationInfo> locationList;
    private String brandId = "";
    private String campaignId = "";
    private String countryId = "";
    private String locationId = "";
    private DashboardInfo dashboardInfo;
    private RanksInfo ranksInfo;
    private OverallInfo overallInfo;
    private com.gdi.model.dashboard.CityInfo cityInfo;
    private com.gdi.model.dashboard.CountryInfo countryInfo;
    private GlobalInfo globalInfo;
    private ArrayList<HighestDeparmentInfo> highestDeparmentList;
    private ArrayList<LowestDepartmentInfo> lowestDepartmentList;
    private DashBoardHighestDeptAdapter dashBoardHighestDeptAdapter;
    private DashBoardLowestDeptAdapter dashBoardLowestDeptAdapter;
    ArrayList<CurrentVsLastInfo> currentVsLastList;
    ArrayList<LastFiveInfo> lastFiveList;
    private boolean isFirstTime = true;
    private boolean isFirstCompaignLoad = true;
    protected String[] mMonths = new String[]{
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dec"
    };
    protected Typeface mTfLight;
    /*ArrayList<String> xAxis1;
    ArrayList<String> xAxis2;
    ArrayList<BarDataSet> yAxis1;
    ArrayList<BarDataSet> yAxis2;
    ArrayList<BarEntry> yValues1;
    ArrayList<BarEntry> yValues2;
    private BarEntry values1 ;
    private BarEntry values2 ;
    private BarData data;*/
    //ArrayList<String> xAxis3;
    Context context;
    private final int itemcount = 12;
    private static final String TAG = ReportDashboardActivity.class.getSimpleName();
    private LinearLayout dateLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_dashboard);
        context = this;
        ButterKnife.bind(ReportDashboardActivity.this);
        initView();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setActionBar();
        search = (Button) findViewById(R.id.btn_search);
        overall_text = (TextView) findViewById(R.id.overall_text);
        cityRank = (TextView) findViewById(R.id.city_rank_txt);
        countryRank = (TextView) findViewById(R.id.country_rank_txt);
        globalRank = (TextView) findViewById(R.id.global_rank_txt);
        cityRankScore = (TextView) findViewById(R.id.city_rank_score);
        countryRankScore = (TextView) findViewById(R.id.country_rank_score);
        globalRankScore = (TextView) findViewById(R.id.global_rank_score);
        list1 = (RecyclerView) findViewById(R.id.dashboard_highest_dept_list);
        list2 = (RecyclerView) findViewById(R.id.dashboard_lowest_dept_list);
        brandSearch = (Spinner) findViewById(R.id.spinner_brand);
        auditRoundSearch = (Spinner) findViewById(R.id.spinner_audit_round);
        countrySearch = (Spinner) findViewById(R.id.spinner_country);
        locationSearch = (Spinner) findViewById(R.id.spinner_location);
        gpBarChart = (HorizontalBarChart) findViewById(R.id.gp_bar_chart);
        pieChart = (PieChart) findViewById(R.id.pie_chart);
        //pieChart.setUsePercentValues(true);
        barChart = (BarChart) findViewById(R.id.bar_chart);
        overallLayout = (CardView) findViewById(R.id.overall_layout);
        departmentLayout = (CardView) findViewById(R.id.department_layout);
        last5Layout = (CardView) findViewById(R.id.last5_layout);
        rankLayout = (CardView) findViewById(R.id.rank_layout);
        highestDepartmentalLayout = (CardView) findViewById(R.id.highest_dept_layout);
        lowestDepartmentalLayout = (CardView) findViewById(R.id.lowest_dept_layout);
        tvAuditDate = (TextView) findViewById(R.id.tv_audit_date);
        tvHotel = (TextView) findViewById(R.id.tv_hotel);
        tvAddress = (TextView) findViewById(R.id.tv_address);
        tvGM = (TextView) findViewById(R.id.tv_gm);
        tvReportScore = (TextView) findViewById(R.id.tv_report_score);
        tvAverageScore = (TextView) findViewById(R.id.tv_average_score);
        tvTopScore = (TextView) findViewById(R.id.tv_top_score);
        dashBoardTextLayout = (CardView) findViewById(R.id.dashboard_text_layout);
        dateLayout = (LinearLayout) findViewById(R.id.date_layout);
        getBrandFilter();
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.playSoundEffect(android.view.SoundEffectConstants.CLICK);
                getDashboard();
            }
        });
    }

    public void getDashboard() {
        showProgressDialog();
        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AppLogger.e(TAG, "Dashboard Response: " + response);
                try {
                    JSONObject object = new JSONObject(response);

                    if (!object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        DashboardRootObject dashboardRootObject = new GsonBuilder().create()
                                .fromJson(object.toString(), DashboardRootObject.class);
                        if (dashboardRootObject.getData() != null &&
                                dashboardRootObject.getData().toString().length() > 0) {
                            dashboardInfo = dashboardRootObject.getData();
                            setCurrentVsLastGraph();
                            overAllGraph();
                            setLastFiveOverallGraph();
                            setRank();
                            setHighestDept();
                            setLowestDept();
                            overallLayout.setVisibility(View.VISIBLE);
                            departmentLayout.setVisibility(View.VISIBLE);
                            last5Layout.setVisibility(View.VISIBLE);
                            rankLayout.setVisibility(View.VISIBLE);
                            highestDepartmentalLayout.setVisibility(View.VISIBLE);
                            lowestDepartmentalLayout.setVisibility(View.VISIBLE);
                            dashBoardTextLayout.setVisibility(View.VISIBLE);
                        }
                    } else if (object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        /*if (object.getInt(ApiResponseKeys.RES_KEY_CODE) == AppConstant.ERROR){
                            AppUtils.toast((BaseActivity) context,
                                    object.getString(ApiResponseKeys.RES_KEY_MESSAGE));
                            finish();
                            startActivity(new Intent(context, SignInActivity.class));
                        }else {

                        }*/
                        AppUtils.toast((BaseActivity) context,
                                object.getString(ApiResponseKeys.RES_KEY_MESSAGE));
                        overallLayout.setVisibility(View.GONE);
                        departmentLayout.setVisibility(View.GONE);
                        last5Layout.setVisibility(View.GONE);
                        rankLayout.setVisibility(View.GONE);
                        highestDepartmentalLayout.setVisibility(View.GONE);
                        lowestDepartmentalLayout.setVisibility(View.GONE);
                        dashBoardTextLayout.setVisibility(View.GONE);
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
                AppLogger.e(TAG, "Dashboard Error: " + error.getMessage());
                AppUtils.toast((BaseActivity) context, "Server temporary unavailable, Please try again");
            }
        };
        AppLogger.e(TAG, "Brand Id: " + brandId);
        AppLogger.e(TAG, "Campaign Id: " + campaignId);
        AppLogger.e(TAG, "Country Id: " + countryId);
        AppLogger.e(TAG, "Location Id: " + locationId);
        String dashboardUrl = ApiEndPoints.DASHBOARD + "?"
                + "brand_id=" + brandId + "&"
                + "campaign_id=" + campaignId + "&"
                + "location_id=" + locationId + "&"
                + "country_id=" + countryId;
        DashboardRequest dashboardRequest = new DashboardRequest(AppPrefs.getAccessToken(context),
                dashboardUrl, stringListener, errorListener);
        VolleyNetworkRequest.getInstance(context).addToRequestQueue(dashboardRequest);
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
                            setLocationFilter(locationCampaignRootObject.getData());
                            setCountryFilter(locationCampaignRootObject.getData());
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
                        countrySearch.setSelection(0);
                        locationSearch.setSelection(0);
                    }
                } else {
                    if (position > 0) {
                        auditRoundSearch.setSelection(0);
                        countrySearch.setSelection(0);
                        locationSearch.setSelection(0);
                        AppPrefs.setFilterBrand(context, position);
                        AppPrefs.setFilterCampaign(context, 0);
                        AppPrefs.setFilterCountry(context, 0);
                        AppPrefs.setFilterCity(context, 0);
                        AppPrefs.setFilterLocation(context, 0);
                        brandId = "" + brandList.get(position).getBrand_id();
                        getCampaignFilter(brandId);
                        AppLogger.e(TAG, "Brand Id: " + brandId);
                        AppLogger.e(TAG, "Brand Position: " + AppPrefs.getFilterBrand(context));
                    } else {
                        auditRoundSearch.setSelection(0);
                        countrySearch.setSelection(0);
                        locationSearch.setSelection(0);

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
                        countrySearch.setSelection(0);
                        locationSearch.setSelection(0);
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //auditRoundSearch.setSelection(AppPrefs.getFilterCampaign(context));
    }

    private void setCountryFilter(ArrayList<FilterLocationInfo> filterLocationInfos) {
        final ArrayList<FilterLocationInfo> countryList = new ArrayList<>();
        FilterLocationInfo countryInfo = new FilterLocationInfo();
        countryInfo.setCountry_id(0);
        ;
        countryInfo.setCountry_name("All");
        countryList.add(countryInfo);
        countryList.addAll(filterLocationInfos);
        ArrayAdapter<String> brandAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_dropdown_item);
        for (int i = 0; i < countryList.size(); i++) {
            brandAdapter.add(countryList.get(i).getCountry_name());
        }
        countrySearch.setAdapter(brandAdapter);
        countrySearch.setSelection(AppPrefs.getFilterCountry(context));
        countryId = "" + countryList.get(AppPrefs.getFilterCountry(context)).getCountry_id();
        countrySearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                countryId = "" + countryList.get(position).getCountry_id();
                AppPrefs.setFilterCountry(context, position);
                AppLogger.e(TAG, "Country Id: " + countryId);
                AppLogger.e(TAG, "Country Name: " + AppPrefs.getFilterCountry(context));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void setLocationFilter(ArrayList<FilterLocationInfo> filterLocationInfos) {
        final ArrayList<FilterLocationInfo> locationList = new ArrayList<>();
        FilterLocationInfo filterLocationInfo = new FilterLocationInfo();
        filterLocationInfo.setLocation_id(0);
        filterLocationInfo.setLocation_name("Select Location");
        locationList.add(filterLocationInfo);
        locationList.addAll(filterLocationInfos);
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

    private void overAllGraph() {
        pieChart.setUsePercentValues(true);

        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.
        overallInfo = dashboardInfo.getOverall();
        String s1 = overallInfo.getScore();
        String s = "88.8%";
        AppLogger.e(TAG, "overAllGraph string1: " + s1);
        String s2 = s1.replace("%", "f");
        AppLogger.e(TAG, "overAllGraph string2: " + s2);
        float score = Float.parseFloat(s2);
        AppLogger.e(TAG, "overAllGraph score: " + score);
        float score2 = 100 - score;
        AppLogger.e(TAG, "overAllGraph score2: " + score2);
        ArrayList<Entry> yvalues = new ArrayList<Entry>();
        yvalues.add(new Entry(score, 0));
        yvalues.add(new Entry(score2, 1));
        /*yvalues.add(new Entry(1f, 2));
        yvalues.add(new Entry(2f, 3));
        yvalues.add(new Entry(3f, 4));
        yvalues.add(new Entry(4f, 5));*/

        PieDataSet dataSet = new PieDataSet(yvalues, "");

        ArrayList<String> xVals = new ArrayList<>();

        xVals.add("Overall Score");
        xVals.add("Missed");
        float value = context.getResources().getDimensionPixelSize(R.dimen.gp_bar_graph_text_size);
        float value2 = context.getResources().getDimensionPixelSize(R.dimen.gp_bar_graph_text_size_1);
        Legend l = pieChart.getLegend();
        l.setTextSize(value2);
        l.setTextColor(getResources().getColor(R.color.colorBlack));
        l.setForm(Legend.LegendForm.CIRCLE); // set what type of form/shape should be used
        l.setPosition(Legend.LegendPosition.BELOW_CHART_RIGHT);
        /*l.setXEntrySpace(100f);
        l.setYEntrySpace(100f);*/
        l.setFormSize(value2);
        l.setFormToTextSpace(5f);
        l.setEnabled(false);

        PieData data = new PieData(xVals, dataSet);
        // In Percentage
        data.setValueFormatter(new PercentFormatter());
        // Default value
        //data.setValueFormatter(new DefaultValueFormatter(0));
        pieChart.setData(data);
        pieChart.setDescription("");
        pieChart.setDrawHoleEnabled(true);
        pieChart.setTransparentCircleRadius(58f);
        //pieChart.setHoleRadius(58f);
        pieChart.invalidate(); //refresh
        pieChart.setRotationEnabled(false);
        pieChart.setHoleColorTransparent(true);
        //pieChart.setCenterTextTypeface(Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf"));

        int[] COLORS_1 = {
                Color.rgb(2, 255, 2),
                Color.rgb(175, 154, 154),
        };
        int[] COLORS_2 = {
                Color.rgb(255, 255, 0),
                Color.rgb(175, 154, 154),
        };
        int[] COLORS_3 = {
                Color.rgb(255, 0, 0),
                Color.rgb(175, 154, 154),
        };
        dataSet.setColors(COLORS_3);

        /*if (score >= 80.0) {
            dataSet.setColors(COLORS_1);
        } else if (score < 80.0 && score >= 65.0) {
            dataSet.setColors(COLORS_2);
        } else {
            dataSet.setColors(COLORS_3);
        }*/

        data.setValueTextSize(value);
        data.setValueTextColor(Color.WHITE);

        pieChart.setOnChartValueSelectedListener(this);

        String auditDate = AppUtils.getAuditDate(overallInfo.getAudit_date());
        tvAuditDate.setText(auditDate);
        tvHotel.setText(overallInfo.getLocation_name());
        tvAddress.setText(overallInfo.getAddress());
        tvGM.setText(overallInfo.getGm());
        tvReportScore.setText("Report Score:\n" + dashboardInfo.getOverall().getScore());
        tvAverageScore.setText("Average Score:\n" + dashboardInfo.getAverage_score());
        tvTopScore.setText("Top Score:\n" + dashboardInfo.getTop_score());
    }

    private void setCurrentVsLastGraph() {
        ArrayList<String> xAxis = new ArrayList<>();
        ArrayList<BarEntry> yValues = new ArrayList<>();
        ArrayList<BarEntry> yValues2 = new ArrayList<>();

        currentVsLastList = new ArrayList<>();
        currentVsLastList.clear();
        currentVsLastList.addAll(dashboardInfo.getCurr_vs_last());

        for (int i = 0; i < currentVsLastList.size(); i++) {
            String current = currentVsLastList.get(i).getScore().getCurrent();
            String currentNew = current.replace("%", "");

            String last = currentVsLastList.get(i).getScore().getLast();
            String lastNew = last.replace("%", "");

            BarEntry values1 = new BarEntry(Float.valueOf(currentNew), i);
            yValues.add(values1);
            if (!lastNew.equals("0") && !lastNew.equals("NA")) {
                BarEntry values2 = new BarEntry(Float.valueOf(lastNew), i);
                yValues2.add(values2);
            }

            //String xAxisValue = currentVsLastList.get(i).getSection_group_name();
            xAxis.add(currentVsLastList.get(i).getSection_group_name());


        }

        BarDataSet barDataSet1 = new BarDataSet(yValues, "Current");
        barDataSet1.setColor(Color.rgb(253, 206, 2));

        String auditDate = AppUtils.getAuditDate(dashboardInfo.getLast_audit_date());

        BarDataSet barDataSet2 = new BarDataSet(yValues2, "Last (" + auditDate + ")");
        barDataSet2.setColor(Color.rgb(70, 130, 162));

        /*List<BarDataSet> barDataSetList = new ArrayList<>();
        barDataSetList.add(barDataSet1);
        barDataSetList.add(barDataSet2);*/
        //BarData data = new BarData(barDataSet1, barDataSet2);

        ArrayList<BarDataSet> yAxis = new ArrayList<>();
        //yAxis2 = new ArrayList<>();
        yAxis.add(barDataSet1);
        yAxis.add(barDataSet2);

        String names[] = xAxis.toArray(new String[xAxis.size()]);
        float value = context.getResources().getDimensionPixelSize(R.dimen.gp_bar_graph_text_size);
        float value2 = context.getResources().getDimensionPixelSize(R.dimen.gp_bar_graph_text_size_1);
        BarData data;
        data = new BarData(xAxis, yAxis);
        data.setValueTextSize(value);
        data.setValueFormatter(new PercentFormatter());
        gpBarChart.setData(data);
        //barChart.getHighlightByTouchPoint(2,2);
        //data.setGroupSpace(200);
        gpBarChart.setDescription("");
        gpBarChart.animateXY(2000, 2000);
        gpBarChart.invalidate();
        gpBarChart.setTouchEnabled(false);
        gpBarChart.setScaleEnabled(false);
        Legend l = gpBarChart.getLegend();
        l.setTextSize(value2);
        l.setTextColor(getResources().getColor(R.color.colorBlack));
        l.setForm(Legend.LegendForm.CIRCLE); // set what type of form/shape should be used
        l.setPosition(Legend.LegendPosition.BELOW_CHART_RIGHT);
        l.setFormSize(value2);

        //X-axis
        XAxis x_axis = gpBarChart.getXAxis();
        x_axis.setDrawGridLines(true);
        x_axis.setGridColor(getResources().getColor(R.color.lightGrey));
        x_axis.setTextColor(getResources().getColor(R.color.colorBlack));
        AppLogger.e("Dimen value", String.valueOf(value));
        x_axis.setTextSize(value);
        //x_axis.setAvoidFirstLastClipping(false);
        x_axis.setPosition(XAxis.XAxisPosition.BOTTOM);


        //Y-axis
        gpBarChart.getAxisRight().setEnabled(false);
        YAxis leftAxis = gpBarChart.getAxisLeft();
        leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setDrawGridLines(true);
        leftAxis.setGridColor(getResources().getColor(R.color.lightGrey));
        leftAxis.setTextColor(getResources().getColor(R.color.colorBlack));
        leftAxis.setTextSize(value);
        leftAxis.setSpaceTop(50f);
        leftAxis.setLabelCount(5); // force 6 labels

    }

    private void setLastFiveOverallGraph() {
        ArrayList<String> xAxis = new ArrayList<>();
        ArrayList<BarEntry> yValues = new ArrayList<>();

        lastFiveList = new ArrayList<>();
        lastFiveList.clear();
        lastFiveList.addAll(dashboardInfo.getLast_five());

        for (int i = 0; i < lastFiveList.size(); i++) {
            String current = lastFiveList.get(i).getScore();
            String currentNew = current.replace("%", "");

            BarEntry values1 = new BarEntry(Float.valueOf(currentNew), i);

            String date = AppUtils.getShowDate(lastFiveList.get(i).getAudit_date());
            AppLogger.e("Date", date);
            TextView textView = new TextView(context);
            textView.setText(date);
            int screenWidth = getDisplayDimensions();
            textView.setWidth(screenWidth / (lastFiveList.size()+1));
            textView.setTextSize(9);
            textView.setTextColor(getResources().getColor(R.color.colorBlack));
            textView.setGravity(Gravity.CENTER);
            dateLayout.addView(textView);
            xAxis.add("");
            yValues.add(values1);
        }

        BarDataSet barDataSet1 = new BarDataSet(yValues, "Overall Score");
        barDataSet1.setColor(Color.rgb(140, 198, 227));

        ArrayList<BarDataSet> yAxis = new ArrayList<>();
        //yAxis2 = new ArrayList<>();
        yAxis.add(barDataSet1);
        float value = context.getResources().getDimensionPixelSize(R.dimen.gp_bar_graph_text_size);
        float value2 = context.getResources().getDimensionPixelSize(R.dimen.gp_bar_graph_text_size_1);
        BarData data;
        data = new BarData(xAxis, yAxis);
        data.setValueTextSize(value);
        data.setValueFormatter(new PercentFormatter());
        barChart.setData(data);
        //barChart.getHighlightByTouchPoint(2,2);
        //data.setGroupSpace(10);
        barChart.setDescription("");
        barChart.animateXY(2000, 2000);
        barChart.invalidate();
        barChart.setScaleEnabled(false);
        barChart.setTouchEnabled(false);
        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        //barChart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        barChart.setPinchZoom(false);
        barChart.getAxisRight().setEnabled(false);
        barChart.setDrawGridBackground(false);
        // mChart.setDrawYLabels(false);

        Legend l = barChart.getLegend();
        l.setTextSize(value2);
        l.setTextColor(getResources().getColor(R.color.colorBlack));
        l.setForm(Legend.LegendForm.CIRCLE); // set what type of form/shape should be used
        l.setPosition(Legend.LegendPosition.BELOW_CHART_RIGHT);
        l.setXEntrySpace(10f);
        l.setYEntrySpace(10f);
        l.setFormSize(value2);
        l.setEnabled(false);

        //X-axis
        XAxis x_axis = barChart.getXAxis();
        x_axis.setDrawGridLines(true);
        x_axis.setAdjustXLabels(true);
        x_axis.setGridColor(getResources().getColor(R.color.lightGrey));
        x_axis.setTextColor(getResources().getColor(R.color.colorBlack));
        x_axis.setTextSize(6f);
        x_axis.setSpaceBetweenLabels(0);
        x_axis.setPosition(XAxis.XAxisPosition.BOTTOM);

        //Y-axis
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setDrawGridLines(true);
        leftAxis.setGridColor(getResources().getColor(R.color.lightGrey));
        leftAxis.setTextColor(getResources().getColor(R.color.colorBlack));
        leftAxis.setTextSize(value);
        leftAxis.setSpaceTop(35f);
        leftAxis.setLabelCount(5);

    }

    private int getDisplayDimensions() {
        Display display = getWindowManager().getDefaultDisplay();

        Point size = new Point();
        display.getSize(size);
        int width = size.x;

        return width;
    }

    private void setRank() {
        cityInfo = dashboardInfo.getRanks().getCity();
        countryInfo = dashboardInfo.getRanks().getCountry();
        globalInfo = dashboardInfo.getRanks().getGlobal();
        cityRank.setText(cityInfo.getCurrent_rank() + " Out of " + cityInfo.getTotal_rank());
        countryRank.setText(countryInfo.getCurrent_rank() + " Out of " + countryInfo.getTotal_rank());
        globalRank.setText(globalInfo.getCurrent_rank() + " Out of " + globalInfo.getTotal_rank());

        AppUtils.setScoreColor(cityInfo.getScore(), cityRankScore, context);
        AppUtils.setScoreColor(countryInfo.getScore(), countryRankScore, context);
        AppUtils.setScoreColor(globalInfo.getScore(), globalRankScore, context);

        cityRankScore.setText("Top Score: " + cityInfo.getScore());
        countryRankScore.setText("Top Score: " + countryInfo.getScore());
        globalRankScore.setText("Top Score: " + globalInfo.getScore());
    }

    private void setHighestDept() {
        highestDeparmentList = new ArrayList<>();
        highestDeparmentList.clear();
        highestDeparmentList.addAll(dashboardInfo.getHighest_dept());
        AppLogger.e(TAG, "List Size: " + highestDeparmentList.size());
        dashBoardHighestDeptAdapter = new DashBoardHighestDeptAdapter(context, highestDeparmentList);
        list1.setLayoutManager(new LinearLayoutManager(context));
        list1.setAdapter(dashBoardHighestDeptAdapter);
    }

    private void setLowestDept() {
        lowestDepartmentList = new ArrayList<>();
        lowestDepartmentList.clear();
        lowestDepartmentList.addAll(dashboardInfo.getLowest_dept());
        AppLogger.e(TAG, "List Size: " + lowestDepartmentList.size());
        dashBoardLowestDeptAdapter = new DashBoardLowestDeptAdapter(context, lowestDepartmentList);
        list2.setLayoutManager(new LinearLayoutManager(context));
        list2.setAdapter(dashBoardLowestDeptAdapter);
    }

    private void setActionBar() {
        initToolbar(toolbar);
        setTitle("Dashboard");
        enableBack(true);
        enableBackPressed();
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

        if (e == null)
            return;
        AppLogger.e("VAL SELECTED",
                "Value: " + e.getVal() + ", xIndex: " + e.getXIndex()
                        + ", DataSet index: " + dataSetIndex);
    }

    @Override
    public void onNothingSelected() {
        AppLogger.e("PieChart", "nothing selected");
    }
}
