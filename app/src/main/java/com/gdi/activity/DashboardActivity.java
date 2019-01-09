package com.gdi.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.gdi.model.filter.BrandsInfo;
import com.gdi.model.filter.CampaignsInfo;
import com.gdi.model.filter.CountryInfo;
import com.gdi.model.filter.FilterInfo;
import com.gdi.model.filter.FilterRootObject;
import com.gdi.model.filter.LocationsInfo;
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
import com.github.mikephil.charting.utils.DefaultValueFormatter;
import com.github.mikephil.charting.utils.Highlight;
import com.github.mikephil.charting.utils.LargeValueFormatter;
import com.github.mikephil.charting.utils.PercentFormatter;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DashboardActivity extends BaseActivity implements OnChartValueSelectedListener {

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
    private FilterInfo filterInfo;
    private ArrayList<BrandsInfo> brandList;
    private ArrayList<CampaignsInfo> campaignList;
    private ArrayList<CountryInfo> countryList;
    private ArrayList<LocationsInfo> locationList;
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
    protected String[] mMonths = new String[] {
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
    private static final String TAG = DashboardActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        context = this;
        ButterKnife.bind(DashboardActivity.this);
        initView();
    }

    private void initView(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setActionBar();
        search = (Button) findViewById(R.id.btn_search);
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
        filterList();
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.playSoundEffect(android.view.SoundEffectConstants.CLICK);
                getDashboard();
            }
        });
    }

    public void filterList() {
        showProgressDialog();
        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AppLogger.e(TAG, "Filter Response: " + response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (!object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        FilterRootObject filterRootObject = new GsonBuilder().create()
                                .fromJson(object.toString(), FilterRootObject.class);
                        if (filterRootObject.getData() != null &&
                                filterRootObject.getData().toString().length() > 0) {
                            filterInfo = filterRootObject.getData();
                            setFilter(filterInfo);
                        }

                    } else if (object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        /*AppUtils.toast((BaseActivity) context,
                                object.getString(ApiResponseKeys.RES_KEY_MESSAGE));*/
                        if (object.getInt(ApiResponseKeys.RES_KEY_CODE) == AppConstant.ERROR){
                            AppUtils.toast((BaseActivity) context,
                                    object.getString(ApiResponseKeys.RES_KEY_MESSAGE));
                            finish();
                            startActivity(new Intent(context, SignInActivity.class));
                        }
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

            }
        };
        FilterRequest auditRequest = new FilterRequest(AppPrefs.getAccessToken(context),
                stringListener, errorListener);
        VolleyNetworkRequest.getInstance(context).addToRequestQueue(auditRequest);
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
                        }
                    } else if (object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        if (object.getInt(ApiResponseKeys.RES_KEY_CODE) == AppConstant.ERROR){
                            AppUtils.toast((BaseActivity) context,
                                    object.getString(ApiResponseKeys.RES_KEY_MESSAGE));
                            finish();
                            startActivity(new Intent(context, SignInActivity.class));
                        }else {
                            AppUtils.toast((BaseActivity) context,
                                    object.getString(ApiResponseKeys.RES_KEY_MESSAGE));
                        }
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
                + "country_id=" + countryId ;
        DashboardRequest dashboardRequest = new DashboardRequest(AppPrefs.getAccessToken(context),
                dashboardUrl, stringListener, errorListener);
        VolleyNetworkRequest.getInstance(context).addToRequestQueue(dashboardRequest);
    }

    private void setBrandFilter(FilterInfo filterInfo){
        brandList = new ArrayList<>();
        BrandsInfo brandsInfo = new BrandsInfo();
        brandsInfo.setBrand_id(0);
        brandsInfo.setBrand_name("--select--");
        brandList.add(brandsInfo);
        brandList.addAll(filterInfo.getBrands());
        ArrayAdapter<String> brandAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_dropdown_item);
        for (int i = 0; i < brandList.size(); i++) {
            brandAdapter.add(brandList.get(i).getBrand_name());
        }
        brandSearch.setAdapter(brandAdapter);

        brandSearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AppConstant.FILTER_BRAND = position;
                brandId = ""+brandList.get(position).getBrand_id();
                AppLogger.e(TAG, "Brand Id: " + brandId);
                AppLogger.e(TAG, "Brand Position: " + AppConstant.FILTER_BRAND);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        brandSearch.setSelection(AppConstant.FILTER_BRAND);


    }

    private void setCampaignFilter(FilterInfo filterInfo){
        campaignList = new ArrayList<>();
        CampaignsInfo campaignsInfo = new CampaignsInfo();
        campaignsInfo.setCampaign_id(0);
        campaignsInfo.setCampaign_name("--select--");
        campaignList.add(campaignsInfo);
        campaignList.addAll(filterInfo.getCampaigns());
        ArrayAdapter<String> campaignAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_dropdown_item);
        for (int i = 0; i < campaignList.size(); i++) {
            campaignAdapter.add(campaignList.get(i).getCampaign_name());
        }
        auditRoundSearch.setAdapter(campaignAdapter);
        auditRoundSearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                campaignId = ""+campaignList.get(position).getCampaign_id();
                AppConstant.FILTER_CAMPAIGN = position;
                AppLogger.e(TAG, "Campaign Id: " + campaignId);
                AppLogger.e(TAG, "Campaign position: " + AppConstant.FILTER_CAMPAIGN);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        auditRoundSearch.setSelection(AppConstant.FILTER_CAMPAIGN);
    }

    private void setCountryFilter(FilterInfo filterInfo){
        countryList = new ArrayList<>();
        CountryInfo countryInfo = new CountryInfo();
        countryInfo.setCountry_id(0);;
        countryInfo.setCountry_name("--select--");
        countryList.add(countryInfo);
        countryList.addAll(filterInfo.getCountry());
        ArrayAdapter<String> brandAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_dropdown_item);
        for (int i = 0; i < countryList.size(); i++) {
            brandAdapter.add(countryList.get(i).getCountry_name());
        }
        countrySearch.setAdapter(brandAdapter);
        countrySearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                countryId = ""+countryList.get(position).getCountry_id();
                AppConstant.FILTER_COUNTRY = position;
                AppLogger.e(TAG, "Country Id: " + countryId);
                AppLogger.e(TAG, "Country Name: " + AppConstant.FILTER_COUNTRY);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        countrySearch.setSelection(AppConstant.FILTER_COUNTRY);

    }

    private void setLocationFilter(FilterInfo filterInfo){
        locationList = new ArrayList<>();
        LocationsInfo locationsInfo = new LocationsInfo();
        locationsInfo.setLocation_id(0);
        locationsInfo.setLocation_name("--select--");
        locationList.add(locationsInfo);
        locationList.addAll(filterInfo.getLocations());
        ArrayAdapter<String> locationAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_dropdown_item);
        for (int i = 0; i < locationList.size(); i++) {
            locationAdapter.add(locationList.get(i).getLocation_name());
        }
        locationSearch.setAdapter(locationAdapter);
        locationSearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                locationId = ""+locationList.get(position).getLocation_id();
                AppConstant.FILTER_LOCATION = position;
                AppLogger.e(TAG, "Location Id: " + locationId);
                AppLogger.e(TAG, "Location position: " + AppConstant.FILTER_LOCATION);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        locationSearch.setSelection(AppConstant.FILTER_LOCATION);
    }

    private void setFilter(FilterInfo filterInfo) {

        setBrandFilter(filterInfo);
        setCampaignFilter(filterInfo);
        setCountryFilter(filterInfo);
        setLocationFilter(filterInfo);
        /*brandList = filterInfo.getBrands();
        ArrayAdapter<String> brandAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_dropdown_item);
        for (int i = 0; i < brandList.size(); i++) {
            brandArrayList.add("--select--");
            brandArrayList.add(brandList.get(i).getBrand_name());
            for (int j = 0; j < brandArrayList.size(); j++) {

                brandAdapter.add(brandList.get(i).getBrand_name());
            }
        }
        brandSearch.setAdapter(brandAdapter);
        brandSearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                brandId = String.valueOf(brandList.get(position).getBrand_id());
                AppConstant.FILTER_BRAND = brandList.get(position).getBrand_name();
                AppLogger.e(TAG, "Brand Id: " + brandId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        campaignList = filterInfo.getCampaigns();
        ArrayAdapter<String> campaignAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_dropdown_item);
        for (int i = 0; i < campaignList.size(); i++) {
            //campaignAdapter.add("--select--");
            campaignAdapter.add(campaignList.get(i).getCampaign_name());
        }
        auditRoundSearch.setAdapter(campaignAdapter);
        auditRoundSearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                campaignId = String.valueOf(campaignList.get(position).getCampaign_id());
                AppConstant.FILTER_CAMPAIGN = campaignList.get(position).getCampaign_name();
                AppLogger.e(TAG, "Campaign Id: " + campaignId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        countryList = filterInfo.getCountry();
        ArrayAdapter<String> countryAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_dropdown_item);
        for (int i = 0; i < countryList.size(); i++) {
            //countryAdapter.add("--select--");
            countryAdapter.add(countryList.get(i).getCountry_name());
        }
        countrySearch.setAdapter(countryAdapter);
        countrySearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                countryId = String.valueOf(countryList.get(position).getCountry_id());
                AppConstant.FILTER_COUNTRY = countryList.get(position).getCountry_name();
                AppLogger.e(TAG, "Country Id: " + countryId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        cityList = filterInfo.getCity();
        ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_dropdown_item);
        for (int i = 0; i < cityList.size(); i++) {
            //cityAdapter.add("--select--");
            cityAdapter.add(cityList.get(i).getCity_name());
        }
        citySearch.setAdapter(cityAdapter);
        citySearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cityId = String.valueOf(cityList.get(position).getCity_id());
                AppConstant.FILTER_CITY = cityList.get(position).getCity_name();
                AppLogger.e(TAG, "City Id: " + cityId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        locationList = filterInfo.getLocations();
        ArrayAdapter<String> locationAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_dropdown_item);
        for (int i = 0; i < locationList.size(); i++) {
            //locationAdapter.add("--select--");
            locationAdapter.add(locationList.get(i).getLocation_name());
        }
        locationSearch.setAdapter(locationAdapter);
        locationSearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                locationId = String.valueOf(locationList.get(position).getLocation_id());
                AppConstant.FILTER_LOCATION = locationList.get(position).getLocation_name();
                AppLogger.e(TAG, "Location Id: " + locationId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
    }

    /*private void setFilter(FilterInfo filterInfo) {
        brandList = filterInfo.getBrands();
        ArrayAdapter<String> brandAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_dropdown_item);
        for (int i = 0; i < brandList.size(); i++) {
            //brandAdapter.add("--select--");
            brandAdapter.add(brandList.get(i).getBrand_name());
        }
        brandSearch.setAdapter(brandAdapter);
        brandSearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                brandId = String.valueOf(brandList.get(position).getBrand_id());
                AppConstant.FILTER_BRAND = brandList.get(position).getBrand_name();
                AppLogger.e(TAG, "Brand Id: " + brandId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        campaignList = filterInfo.getCampaigns();
        ArrayAdapter<String> campaignAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_dropdown_item);
        for (int i = 0; i < campaignList.size(); i++) {
            //campaignAdapter.add("--select--");
            campaignAdapter.add(campaignList.get(i).getCampaign_name());
        }
        auditRoundSearch.setAdapter(campaignAdapter);
        auditRoundSearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                campaignId = String.valueOf(campaignList.get(position).getCampaign_id());
                AppConstant.FILTER_CAMPAIGN = campaignList.get(position).getCampaign_name();
                AppLogger.e(TAG, "Campaign Id: " + campaignId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        countryList = filterInfo.getCountry();
        ArrayAdapter<String> countryAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_dropdown_item);
        for (int i = 0; i < countryList.size(); i++) {
            //countryAdapter.add("--select--");
            countryAdapter.add(countryList.get(i).getCountry_name());
        }
        countrySearch.setAdapter(countryAdapter);
        countrySearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                countryId = String.valueOf(countryList.get(position).getCountry_id());
                AppConstant.FILTER_COUNTRY = countryList.get(position).getCountry_name();
                AppLogger.e(TAG, "Country Id: " + countryId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        locationList = filterInfo.getLocations();
        ArrayAdapter<String> locationAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_dropdown_item);
        for (int i = 0; i < locationList.size(); i++) {
            //locationAdapter.add("--select--");
            locationAdapter.add(locationList.get(i).getLocation_name());
        }
        locationSearch.setAdapter(locationAdapter);
        locationSearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                locationId = String.valueOf(locationList.get(position).getLocation_id());
                AppConstant.FILTER_LOCATION = locationList.get(position).getLocation_name();
                AppLogger.e(TAG, "Location Id: " + locationId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }*/

    private void overAllGraph(){
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
        float score2  = 100 - score;
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

        xVals.add("Average Score");
        xVals.add("Rest");
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

        int[] COLORS = {
                Color.rgb(255, 0, 0),
                Color.rgb(175, 154, 154),
        };
        dataSet.setColors(COLORS);

        data.setValueTextSize(value);
        data.setValueTextColor(Color.WHITE);

        pieChart.setOnChartValueSelectedListener(this);
    }

    private void setCurrentVsLastGraph(){
        ArrayList<String> xAxis = new ArrayList<>();
        ArrayList<BarEntry> yValues = new ArrayList<>();
        ArrayList<BarEntry> yValues2 = new ArrayList<>();

        currentVsLastList = new ArrayList<>();
        currentVsLastList.clear();
        currentVsLastList.addAll(dashboardInfo.getCurr_vs_last());

        for (int i = 0; i < currentVsLastList.size(); i++){
            String current = currentVsLastList.get(i).getScore().getCurrent();
            String currentNew = current.replace("%", "");

            String last = currentVsLastList.get(i).getScore().getLast();
            String lastNew = last.replace("%", "");

            BarEntry values1 = new BarEntry(Float.valueOf(currentNew),i);
            yValues.add(values1);
            if (!lastNew.equals("0")){
                BarEntry values2 = new BarEntry(Float.valueOf(lastNew),i);
                yValues2.add(values2);
            }

            //String xAxisValue = currentVsLastList.get(i).getSection_group_name();
            xAxis.add(currentVsLastList.get(i).getSection_group_name());




        }

        BarDataSet barDataSet1 = new BarDataSet(yValues, "Current");
        barDataSet1.setColor(Color.rgb(253, 206, 2));

        BarDataSet barDataSet2 = new BarDataSet(yValues2, "Last");
        barDataSet2.setColor(Color.rgb(70, 130, 162));

        /*List<BarDataSet> barDataSetList = new ArrayList<>();
        barDataSetList.add(barDataSet1);
        barDataSetList.add(barDataSet2);*/
        //BarData data = new BarData(barDataSet1, barDataSet2);

        ArrayList<BarDataSet> yAxis = new ArrayList<>();
        //yAxis2 = new ArrayList<>();
        yAxis.add(barDataSet1);
        yAxis.add(barDataSet2);

        String names[]= xAxis.toArray(new String[xAxis.size()]);
        float value = context.getResources().getDimensionPixelSize(R.dimen.gp_bar_graph_text_size);
        float value2 = context.getResources().getDimensionPixelSize(R.dimen.gp_bar_graph_text_size_1);
        BarData data;
        data = new BarData(xAxis,yAxis);
        data.setValueTextSize(value);
        data.setValueFormatter(new PercentFormatter());
        gpBarChart.setData(data);
        //barChart.getHighlightByTouchPoint(2,2);
        //data.setGroupSpace(200);
        gpBarChart.setDescription("");
        gpBarChart.animateXY(2000, 2000);
        gpBarChart.invalidate();
        gpBarChart.setScaleEnabled(false);
        Legend l = gpBarChart.getLegend();
        l.setTextSize(value2);
        l.setTextColor(getResources().getColor(R.color.colorBlack));
        l.setForm(Legend.LegendForm.CIRCLE); // set what type of form/shape should be used
        l.setPosition(Legend.LegendPosition.BELOW_CHART_RIGHT);
        l.setFormSize(value2);

        //X-axis
        XAxis x_axis = gpBarChart.getXAxis();
        x_axis.setDrawGridLines(false);
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
        leftAxis.setTextColor(getResources().getColor(R.color.colorBlack));
        leftAxis.setTextSize(value);
        leftAxis.setStartAtZero(false);
        leftAxis.setSpaceTop(100f);
        leftAxis.setLabelCount(6); // force 6 labels
        
    }

    private void setLastFiveOverallGraph() {
        ArrayList<String> xAxis = new ArrayList<>();
        ArrayList<BarEntry> yValues = new ArrayList<>();

        lastFiveList = new ArrayList<>();
        lastFiveList.clear();
        lastFiveList.addAll(dashboardInfo.getLast_five());

        for (int i = 0; i < lastFiveList.size(); i++){
            String current = lastFiveList.get(i).getScore();
            String currentNew = current.replace("%", "");

            BarEntry values1 = new BarEntry(Float.valueOf(currentNew),i);

            xAxis.add(lastFiveList.get(i).getAudit_date());
            yValues.add(values1);
        }

        BarDataSet barDataSet1 = new BarDataSet(yValues, "Current");
        barDataSet1.setColor(Color.rgb(140, 198, 227));

        ArrayList<BarDataSet> yAxis = new ArrayList<>();
        //yAxis2 = new ArrayList<>();
        yAxis.add(barDataSet1);
        float value = context.getResources().getDimensionPixelSize(R.dimen.gp_bar_graph_text_size);
        float value2 = context.getResources().getDimensionPixelSize(R.dimen.gp_bar_graph_text_size_1);
        BarData data;
        data = new BarData(xAxis,yAxis);
        data.setValueTextSize(value);
        data.setValueFormatter(new PercentFormatter());
        barChart.setData(data);
        //barChart.getHighlightByTouchPoint(2,2);
        //data.setGroupSpace(10);
        barChart.setDescription("");
        barChart.animateXY(2000, 2000);
        barChart.invalidate();
        barChart.setScaleEnabled(false);

        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        //barChart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        barChart.setPinchZoom(false);

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

        //X-axis
        XAxis x_axis = barChart.getXAxis();
        x_axis.setDrawGridLines(false);
        x_axis.setTextColor(getResources().getColor(R.color.colorBlack));
        AppLogger.e("Dimen value", String.valueOf(value));
        x_axis.setTextSize(value);
        x_axis.setPosition(XAxis.XAxisPosition.BOTTOM);

        //Y-axis
        barChart.getAxisRight().setEnabled(false);
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setDrawGridLines(true);
        leftAxis.setTextColor(getResources().getColor(R.color.colorBlack));
        leftAxis.setTextSize(value);
        leftAxis.setStartAtZero(false);
        leftAxis.setSpaceTop(35f);

    }

    private void setRank(){
        cityInfo = dashboardInfo.getRanks().getCity();
        countryInfo = dashboardInfo.getRanks().getCountry();
        globalInfo = dashboardInfo.getRanks().getGlobal();
        cityRank.setText(cityInfo.getCurrent_rank() + " Out of " + cityInfo.getTotal_rank());
        countryRank.setText(countryInfo.getCurrent_rank() + " Out of " + countryInfo.getTotal_rank());
        globalRank.setText(globalInfo.getCurrent_rank() + " Out of " + globalInfo.getTotal_rank());
        cityRankScore.setText("Top Score: " +cityInfo.getScore());
        countryRankScore.setText("Top Score: " +countryInfo.getScore());
        globalRankScore.setText("Top Score: " +globalInfo.getScore());
    }

    private void setHighestDept(){
        highestDeparmentList = new ArrayList<>();
        highestDeparmentList.clear();
        highestDeparmentList.addAll(dashboardInfo.getHighest_dept());
        AppLogger.e(TAG, "List Size: " + highestDeparmentList.size());
        dashBoardHighestDeptAdapter = new DashBoardHighestDeptAdapter(context, highestDeparmentList);
        list1.setLayoutManager(new LinearLayoutManager(context));
        list1.setAdapter(dashBoardHighestDeptAdapter);
    }

    private void setLowestDept(){
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
