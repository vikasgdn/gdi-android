package com.gdi.activity.InternalAuditReport;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gdi.R;
import com.gdi.activity.BaseActivity;
import com.gdi.adapter.IADashBoardHighestDeptAdapter;
import com.gdi.api.ApiEndPoints;
import com.gdi.api.FilterRequest;
import com.gdi.api.GetReportRequest;
import com.gdi.api.VolleyNetworkRequest;
import com.gdi.model.dashboard.GlobalInfo;
import com.gdi.model.dashboard.IACurrentVsLastInfo;
import com.gdi.model.dashboard.IADashboardInfo;
import com.gdi.model.dashboard.IADashboardRootObject;
import com.gdi.model.dashboard.IADeparmentInfo;
import com.gdi.model.dashboard.LastFiveInfo;
import com.gdi.model.dashboard.OverallInfo;
import com.gdi.model.dashboard.RanksInfo;
import com.gdi.model.filter.BrandFilterRootObject;
import com.gdi.model.filter.BrandsInfo;
import com.gdi.model.filter.FilterLocationInfo;
import com.gdi.model.filter.FilterLocationModel;
import com.gdi.model.filter.LocationFilterRootObject;
import com.gdi.model.iafilter.Audit;
import com.gdi.model.iafilter.AuditName;
import com.gdi.model.iafilter.AuditNameRootObject;
import com.gdi.model.iafilter.IAFilterInfo;
import com.gdi.utils.ApiResponseKeys;
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
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;


public class IAReportDashboardActivity extends BaseActivity implements
        OnChartValueSelectedListener, View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.btn_search)
    Button search;
    @BindView(R.id.spinner_brand)
    Spinner brandSearch;
    @BindView(R.id.spinner_audit_type)
    Spinner auditTypeSearch;
    @BindView(R.id.tv_audit_month)
    TextView auditMonthSearch;
    @BindView(R.id.spinner_audit_name)
    Spinner auditNameSearch;
    @BindView(R.id.spinner_location)
    Spinner locationSearch;
    @BindView(R.id.dashboard_highest_dept_list)
    RecyclerView list1;
    @BindView(R.id.dashboard_lowest_dept_list)
    RecyclerView list2;
    @BindView(R.id.gp_bar_chart)
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
    @BindView(R.id.highest_dept_layout)
    CardView highestDepartmentalLayout;
    @BindView(R.id.lowest_dept_layout)
    CardView lowestDepartmentalLayout;
    @BindView(R.id.tv_audit_date)
    TextView tvAuditDate;
    @BindView(R.id.tv_audit_name)
    TextView tvAuditName;
    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.tv_auditor)
    TextView tvAuditor;
    @BindView(R.id.tv_reviewer)
    TextView tvReviewer;
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
    private LinearLayout dateLayout;
    private IAFilterInfo iaFilterInfo;
    private ArrayList<BrandsInfo> brandList;
    private ArrayList<Audit> audits;
    private ArrayList<AuditName> auditTypes;
    private ArrayList<FilterLocationInfo> locationList;
    private String brandId = "";
    private String auditTypeId = "";
    private String auditNameId = "";
    private String auditMonth = "";
    private String locationId = "";
    private IADashboardInfo dashboardInfo;
    private RanksInfo ranksInfo;
    private OverallInfo overallInfo;
    private com.gdi.model.dashboard.CityInfo cityInfo;
    private com.gdi.model.dashboard.CountryInfo countryInfo;
    private GlobalInfo globalInfo;
    private ArrayList<IADeparmentInfo> highestDeparmentList;
    private ArrayList<IADeparmentInfo> lowestDepartmentList;
    private IADashBoardHighestDeptAdapter dashBoardHighestDeptAdapter;
    private IADashBoardHighestDeptAdapter dashBoardLowestDeptAdapter;
    ArrayList<IACurrentVsLastInfo> currentVsLastList;
    ArrayList<LastFiveInfo> lastFiveList;
    protected String[] mMonths = new String[] {
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dec"
    };
    protected Typeface mTfLight;
    Context context;
    private final int itemcount = 12;
    private boolean isFirstTime = true;
    private boolean isFirstTimeLocation = true;
    private boolean isFirstTimeAuditType = true;
    private boolean isFirstTimeBrand = true;
    ArrayList<String> arrayList = new ArrayList<>();
    private static final String TAG = IAReportDashboardActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ia_report_dashboard);
        context = this;
        ButterKnife.bind(IAReportDashboardActivity.this);
        initView();
    }

    private void initView(){
        toolbar = findViewById(R.id.toolbar);
        setActionBar();
        search = findViewById(R.id.btn_search);
        brandSearch = findViewById(R.id.spinner_brand);
        auditTypeSearch = findViewById(R.id.spinner_audit_type);
        auditMonthSearch = findViewById(R.id.tv_audit_month);
        auditNameSearch = findViewById(R.id.spinner_audit_name);
        locationSearch = findViewById(R.id.spinner_location);
        gpBarChart = findViewById(R.id.gp_bar_chart);
        pieChart = findViewById(R.id.pie_chart);
        //pieChart.setUsePercentValues(true);
        list1 = findViewById(R.id.dashboard_highest_dept_list);
        list2 = findViewById(R.id.dashboard_lowest_dept_list);
        barChart = findViewById(R.id.bar_chart);
        overallLayout = findViewById(R.id.overall_layout);
        departmentLayout = findViewById(R.id.department_layout);
        last5Layout = findViewById(R.id.last5_layout);
        highestDepartmentalLayout = findViewById(R.id.highest_dept_layout);
        lowestDepartmentalLayout = findViewById(R.id.lowest_dept_layout);
        tvAuditDate = findViewById(R.id.tv_audit_date);
        tvReviewer = findViewById(R.id.tv_reviewer);
        tvAuditor = findViewById(R.id.tv_auditor);
        tvAuditName = findViewById(R.id.tv_audit_name);
        tvLocation = findViewById(R.id.tv_location);
        tvGM = findViewById(R.id.tv_gm);
        tvReportScore = findViewById(R.id.tv_report_score);
        tvAverageScore = findViewById(R.id.tv_average_score);
        tvTopScore = findViewById(R.id.tv_top_score);
        dashBoardTextLayout = findViewById(R.id.dashboard_text_layout);
        dateLayout = findViewById(R.id.date_layout);

        arrayList.add("Select");

        brandId = "" + AppPrefs.getIaFilterBrand(context);
        auditTypeId = "" + AppPrefs.getIaFilterAuditType(context);
        auditNameId = "" + AppPrefs.getIaFilterAuditName(context);
        auditMonth = "" + AppPrefs.getIaFilterMonth(context);
        locationId = "" + AppPrefs.getIaFilterLocation(context);

        search.setOnClickListener(this);
        auditMonthSearch.setOnClickListener(this);


        /*if (!AppUtils.isStringEmpty(AppPrefs.getIaFilterMonth(context))){
            auditMonthSearch.setText(AppPrefs.getIaFilterMonth(context));
        }
        setAuditTypeFilter();
        setBrandFilter(new ArrayList<BrandsInfo>());
        setLocationFilter(new FilterLocationModel());
        setAuditNameFilter(new ArrayList<AuditName>());*/

        setAuditType();

        if (!AppUtils.isStringEmpty(AppPrefs.getIaFilterMonth(context))){
            auditMonthSearch.setText(AppPrefs.getIaFilterMonth(context));
            auditMonth = AppPrefs.getIaFilterMonth(context);
            getBrandFilter(auditMonth);
        }else {
            brandSearch.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item,arrayList));
            locationSearch.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item,arrayList));
            auditNameSearch.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item,arrayList));
        }

    }



    private void getBrandFilter(String auditMonth) {
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
                            setBrand(brandFilterRootObject.getData());
                        }

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
                AppLogger.e(TAG, "Filter Error: " + error.getMessage());
                AppUtils.toast((BaseActivity) context, "Server temporary unavailable, Please try again");

            }
        };
        String brandUrl = ApiEndPoints.FILTERBRAND + "?"
                + "audit_type_id=" + auditTypeId + "&"
                + "audit_month=" + auditMonth;

        AppLogger.e("BrandUrL",brandUrl);

        FilterRequest filterRequest = new FilterRequest(brandUrl,
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

                            setLocation(locationCampaignRootObject.getData());
                        }

                    } else if (object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        AppUtils.toast((BaseActivity) context,
                                object.getString(ApiResponseKeys.RES_KEY_MESSAGE));
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
                + "campaign_id=" + "";
        FilterRequest filterRequest = new FilterRequest(locationUrl,
                AppPrefs.getAccessToken(context), stringListener, errorListener);
        VolleyNetworkRequest.getInstance(context).addToRequestQueue(filterRequest);
    }

    private void getAuditNameFilter() {
        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AppLogger.e(TAG, "AuditNameFilterResponse: " + response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (!object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        AuditNameRootObject auditNameRootObject = new GsonBuilder().create()
                                .fromJson(object.toString(), AuditNameRootObject.class);
                        if (auditNameRootObject.getData() != null &&
                                auditNameRootObject.getData().toString().length() > 0) {
                            setAuditName(auditNameRootObject.getData());
                        }

                    } else if (object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        AppUtils.toast((BaseActivity) context,
                                object.getString(ApiResponseKeys.RES_KEY_MESSAGE));
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
        String campaignUrl = ApiEndPoints.FILTERIAAUDIT + "?"
                + "audit_type_id=" + auditTypeId + "&"
                + "audit_month=" + auditMonth + "&"
                + "location_id=" + locationId;
        FilterRequest filterRequest = new FilterRequest(campaignUrl,
                AppPrefs.getAccessToken(context), stringListener, errorListener);
        VolleyNetworkRequest.getInstance(context).addToRequestQueue(filterRequest);
    }

    private void setAuditType(){
        ArrayList<String> auditTypes = new ArrayList<>();
        auditTypes.add("Select");
        auditTypes.add("Self Assessment");
        auditTypes.add("Heart of the House");
        auditTypes.add("Inspection");
        auditTypes.add("Pre Opening");

        ArrayAdapter<String> auditTypeAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_dropdown_item,auditTypes);
        auditTypeSearch.setAdapter(auditTypeAdapter);
        auditTypeSearch.setSelection(AppPrefs.getIaFilterAuditType(context));

        auditTypeSearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (isFirstTime) {
                    isFirstTime = false;
                    if (AppPrefs.getIaFilterAuditType(context) > 0) {
                        auditTypeId = "" + position;
                    }
                }else {
                    if (position > 0){
                        AppPrefs.setIaFilterAuditType(context, position);
                        AppPrefs.setIaFilterMonth(context,"");
                        AppPrefs.setIaFilterBrand(context,0);
                        AppPrefs.setIaFilterLocation(context, 0);
                        AppPrefs.setIaFilterAuditName(context,0);
                        auditTypeId = "" + position;
                        auditMonthSearch.setText("Select");
                        brandSearch.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item,arrayList));
                        locationSearch.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item,arrayList));
                        auditNameSearch.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item,arrayList));
                    } else {
                        auditMonthSearch.setText("Select");
                        brandSearch.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item,arrayList));
                        locationSearch.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item,arrayList));
                        auditNameSearch.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item,arrayList));
                        brandId = "";
                        locationId = "";
                        auditNameId = "";
                        auditMonth = "";
                        auditTypeId = "";

                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setAuditMonth() {
        Calendar auditMonthCal = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        Calendar cal = Calendar.getInstance();
                        cal.set(year, monthOfYear, dayOfMonth);
                        auditMonth = AppUtils.getAuditMonth(cal.getTime());
                        brandId = "";
                        AppPrefs.setIaFilterBrand(context, 0);
                        brandSearch.setSelection(0);
                        getBrandFilter(auditMonth);
                        AppPrefs.setIaFilterMonth(context, auditMonth);
                        auditMonthSearch.setText(AppUtils.setAuditMonth(cal.getTime()));
                    }
                }, auditMonthCal.get(Calendar.YEAR), auditMonthCal.get(Calendar.MONTH),
                auditMonthCal.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void setBrand(ArrayList<BrandsInfo> brandsInfos){
        final ArrayList<BrandsInfo> brandList = new ArrayList<>();
        BrandsInfo brandsInfo = new BrandsInfo();
        brandsInfo.setBrand_id(0);
        brandsInfo.setBrand_name("Select");
        brandList.add(brandsInfo);
        brandList.addAll(brandsInfos);
        ArrayAdapter<String> brandAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_dropdown_item);
        for (int i = 0; i < brandList.size(); i++) {
            brandAdapter.add(brandList.get(i).getBrand_name());
        }
        brandSearch.setAdapter(brandAdapter);
        brandSearch.setSelection(AppPrefs.getIaFilterBrand(context));
        brandSearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (isFirstTimeBrand) {
                    isFirstTimeBrand = false;
                    if (AppPrefs.getIaFilterBrand(context) > 0) {
                        brandId = "" + brandList.get(position).getBrand_id();
                        getLocationFilter();
                    }
                }else {
                    if (position > 0) {
                        AppPrefs.setIaFilterBrand(context, position);
                        AppPrefs.setIaFilterLocation(context, 0);
                        AppPrefs.setIaFilterAuditName(context, 0);
                        brandId = "" + brandList.get(position).getBrand_id();
                        getLocationFilter();
                        locationSearch.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item,arrayList));
                        auditNameSearch.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item,arrayList));
                    }else {
                        auditNameId = "";
                        locationId = "";
                        brandId = "";
                        locationSearch.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item,arrayList));
                        auditNameSearch.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item,arrayList));
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setLocation(FilterLocationModel locationModel){
        final ArrayList<FilterLocationInfo> locationList = new ArrayList<>();
        FilterLocationInfo filterLocationInfo = new FilterLocationInfo();
        filterLocationInfo.setLocation_id(0);
        filterLocationInfo.setLocation_name("Select");
        locationList.add(filterLocationInfo);
        if (locationModel.getLocations() != null ) {
            locationList.addAll(locationModel.getLocations());
        }
        ArrayAdapter<String> locationAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_dropdown_item);
        for (int i = 0; i < locationList.size(); i++) {
            locationAdapter.add(locationList.get(i).getLocation_name());
        }
        locationSearch.setAdapter(locationAdapter);
        locationSearch.setSelection(AppPrefs.getIaFilterLocation(context));

        locationSearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (isFirstTimeLocation) {
                    isFirstTimeLocation = false;
                    if (AppPrefs.getIaFilterBrand(context) > 0) {
                        locationId = "" + locationList.get(position).getLocation_id();
                        getAuditNameFilter();
                    }
                }else {
                    if (position > 0) {
                        AppPrefs.setIaFilterLocation(context, position);
                        AppPrefs.setIaFilterAuditName(context, 0);
                        locationId = "" + locationList.get(position).getLocation_id();
                        getAuditNameFilter();
                        auditNameSearch.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item,arrayList));

                    } else {
                        locationId = "";
                        auditNameId = "";
                        auditNameSearch.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item,arrayList));

                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setAuditName(ArrayList<AuditName> nameArrayList){
        final ArrayList<AuditName> auditNames = new ArrayList<>();
        AuditName filterLocationInfo = new AuditName();
        filterLocationInfo.setAudit_id(0);
        filterLocationInfo.setAudit_name("Select");
        auditNames.add(filterLocationInfo);
        auditNames.addAll(nameArrayList);

        ArrayAdapter<String> locationAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_dropdown_item);
        for (int i = 0; i < auditNames.size(); i++) {
            locationAdapter.add(auditNames.get(i).getAudit_name());
        }
        auditNameSearch.setAdapter(locationAdapter);
        auditNameSearch.setSelection(AppPrefs.getIaFilterAuditName(context));

        auditNameSearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                auditNameId = "" + auditNames.get(position).getAudit_id();
                AppPrefs.setIaFilterAuditName(context, position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }






    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_audit_month:
                setAuditMonth();
                break;
            case R.id.btn_search:
                view.playSoundEffect(android.view.SoundEffectConstants.CLICK);
                getDashboard();
                break;
        }
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
                        IADashboardRootObject dashboardRootObject = new GsonBuilder().create()
                                .fromJson(object.toString(), IADashboardRootObject.class);
                        if (dashboardRootObject.getData() != null &&
                                dashboardRootObject.getData().toString().length() > 0) {
                            dashboardInfo = dashboardRootObject.getData();
                            setCurrentVsLastGraph();
                            overAllGraph();
                            setLastFiveOverallGraph();
                            setHighestDept();
                            setLowestDept();
                            overallLayout.setVisibility(View.VISIBLE);
                            departmentLayout.setVisibility(View.VISIBLE);
                            last5Layout.setVisibility(View.VISIBLE);
                            highestDepartmentalLayout.setVisibility(View.VISIBLE);
                            lowestDepartmentalLayout.setVisibility(View.VISIBLE);
                            dashBoardTextLayout.setVisibility(View.VISIBLE);
                        }
                    } else if (object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        AppUtils.toast((BaseActivity) context,
                                object.getString(ApiResponseKeys.RES_KEY_MESSAGE));
                        overallLayout.setVisibility(View.GONE);
                        departmentLayout.setVisibility(View.GONE);
                        last5Layout.setVisibility(View.GONE);
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

            }
        };
        AppLogger.e(TAG, "Brand Id: " + brandId);
        AppLogger.e(TAG, "AyditType Id: " + auditTypeId);
        AppLogger.e(TAG, "Audit Id: " + auditNameId);
        AppLogger.e(TAG, "Month: " + auditMonth);
        AppLogger.e(TAG, "Location Id: " + locationId);
        String dashboardUrl = ApiEndPoints.IADASHBOARD + "?"
                + "audit_type=" + auditTypeId + "&"
                + "brand_id=" + brandId + "&"
                + "location_id=" + locationId + "&"
                + "audit_id=" + auditNameId + "&"
                + "audit_month=" + auditMonth;

        GetReportRequest getReportRequest = new GetReportRequest(AppPrefs.getAccessToken(context),
                dashboardUrl, stringListener, errorListener);
        VolleyNetworkRequest.getInstance(context).addToRequestQueue(getReportRequest);
    }

    private void overAllGraph(){
        pieChart.setUsePercentValues(true);

        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.
        overallInfo = dashboardInfo.getOverall();
        if (!overallInfo.getScore().equals("") && !overallInfo.getScore().equals("NA")) {
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
            l.setFormSize(value2);
            l.setFormToTextSpace(5f);
            l.setEnabled(false);

            PieData data = new PieData(xVals, dataSet);
            // In Percentage
            data.setValueFormatter(new PercentFormatter());
            // Default value
            pieChart.setData(data);
            pieChart.setDescription("");
            pieChart.setDrawHoleEnabled(true);
            pieChart.setTransparentCircleRadius(58f);
            pieChart.invalidate(); //refresh
            pieChart.setRotationEnabled(false);
            pieChart.setHoleColorTransparent(true);

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
            tvLocation.setText(overallInfo.getLocation_name());
            tvAuditor.setText(overallInfo.getAuditor());
            tvReviewer.setText(overallInfo.getReviewer());
            tvAuditName.setText(overallInfo.getAudit_name());
            tvGM.setText(overallInfo.getGm());
            tvReportScore.setVisibility(View.GONE);
            tvAverageScore.setVisibility(View.GONE);
            tvTopScore.setVisibility(View.GONE);
        }
    }

    private void setCurrentVsLastGraph(){
        ArrayList<String> xAxis = new ArrayList<>();
        ArrayList<BarEntry> yValues = new ArrayList<>();
        ArrayList<BarEntry> yValues2 = new ArrayList<>();

        currentVsLastList = new ArrayList<>();
        currentVsLastList.clear();
        currentVsLastList.addAll(dashboardInfo.getCurr_vs_last());

        for (int i = 0; i < currentVsLastList.size(); i++) {
            String current = currentVsLastList.get(i).getScore().getCurrent();
            String currentNew = current.replace("%", "");

            String last = currentVsLastList.get(i).getScore().getScore();
            String lastNew = last.replace("%", "");

            if (!currentNew.equals("0") && !currentNew.equals("NA")) {
                BarEntry values1 = new BarEntry(Float.valueOf(currentNew), i);
                yValues.add(values1);
            }

            if (!lastNew.equals("0") && !lastNew.equals("NA")) {
                BarEntry values2 = new BarEntry(Float.valueOf(lastNew), i);
                yValues2.add(values2);
            }
            xAxis.add(currentVsLastList.get(i).getSection_group_title());


        }

        BarDataSet barDataSet1 = new BarDataSet(yValues, "Current");
        barDataSet1.setColor(Color.rgb(253, 206, 2));

        String auditDate = AppUtils.getAuditDate(dashboardInfo.getLast_audit_date());

        BarDataSet barDataSet2 = new BarDataSet(yValues2, "Last (" + auditDate + ")");
        barDataSet2.setColor(Color.rgb(70, 130, 162));

        ArrayList<BarDataSet> yAxis = new ArrayList<>();
        yAxis.add(barDataSet1);
        yAxis.add(barDataSet2);

        //String names[] = xAxis.toArray(new String[xAxis.size()]);
        float value = context.getResources().getDimensionPixelSize(R.dimen.gp_bar_graph_text_size);
        float value2 = context.getResources().getDimensionPixelSize(R.dimen.gp_bar_graph_text_size_1);
        //BarData data;
        BarData data = new BarData(xAxis, yAxis);
        data.setValueTextSize(value);
        data.setValueFormatter(new PercentFormatter());
        gpBarChart.setData(data);
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
        float value = context.getResources().getDimensionPixelSize(R.dimen.gp_bar_graph_text_size);
        float value2 = context.getResources().getDimensionPixelSize(R.dimen.gp_bar_graph_text_size_1);
        lastFiveList = new ArrayList<>();
        lastFiveList.clear();
        lastFiveList.addAll(dashboardInfo.getLast_five());
        dateLayout.removeAllViews();

        for (int i = 0; i < lastFiveList.size(); i++) {
            String current = lastFiveList.get(i).getScore();
            String currentNew = current.replace("%", "");

            if (!currentNew.equals("0") && !currentNew.equals("NA")) {
                BarEntry values1 = new BarEntry(Float.valueOf(currentNew), i);
                yValues.add(values1);
            }



            String date = AppUtils.getShowDate(lastFiveList.get(i).getAudit_date());
            AppLogger.e("Date", date);

            TextView textViewTIme = new TextView(context);
            textViewTIme.setText(date);
            int screenWidth = getDisplayDimensions();
            textViewTIme.setWidth(screenWidth / (lastFiveList.size()+1));
            textViewTIme.setTextSize(value);
            textViewTIme.setTextColor(getResources().getColor(R.color.colorBlack));
            textViewTIme.setGravity(Gravity.CENTER);
            dateLayout.addView(textViewTIme);
            xAxis.add("");

        }

        BarDataSet barDataSet1 = new BarDataSet(yValues, "Overall Score");
        barDataSet1.setColor(Color.rgb(140, 198, 227));

        ArrayList<BarDataSet> yAxis = new ArrayList<>();
        yAxis.add(barDataSet1);

        BarData data;
        data = new BarData(xAxis, yAxis);
        data.setValueTextSize(value);
        data.setValueFormatter(new PercentFormatter());
        barChart.setData(data);
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

    private void setHighestDept() {
        highestDeparmentList = new ArrayList<>();
        highestDeparmentList.clear();
        highestDeparmentList.addAll(dashboardInfo.getHighest_dept());
        AppLogger.e(TAG, "List Size: " + highestDeparmentList.size());
        dashBoardHighestDeptAdapter = new IADashBoardHighestDeptAdapter(context, highestDeparmentList);
        list1.setLayoutManager(new LinearLayoutManager(context));
        list1.setAdapter(dashBoardHighestDeptAdapter);
    }

    private void setLowestDept() {
        lowestDepartmentList = new ArrayList<>();
        lowestDepartmentList.clear();
        lowestDepartmentList.addAll(dashboardInfo.getLowest_dept());
        AppLogger.e(TAG, "List Size: " + lowestDepartmentList.size());
        dashBoardLowestDeptAdapter = new IADashBoardHighestDeptAdapter(context, lowestDepartmentList);
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
