package com.gdi.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.gdi.api.IAFilterRequest;
import com.gdi.api.VolleyNetworkRequest;
import com.gdi.model.dashboard.CurrentVsLastInfo;
import com.gdi.model.dashboard.DashboardInfo;
import com.gdi.model.dashboard.DashboardRootObject;
import com.gdi.model.dashboard.GlobalInfo;
import com.gdi.model.dashboard.HighestDeparmentInfo;
import com.gdi.model.dashboard.IACurrentVsLastInfo;
import com.gdi.model.dashboard.IADashboardInfo;
import com.gdi.model.dashboard.IADashboardRootObject;
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
import com.gdi.model.iafilter.Audit;
import com.gdi.model.iafilter.AuditTypes;
import com.gdi.model.iafilter.IAFilterInfo;
import com.gdi.model.iafilter.IAFilterRootObject;
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
    private IAFilterInfo iaFilterInfo;
    private ArrayList<BrandsInfo> brandList;
    private ArrayList<Audit> audits;
    private ArrayList<AuditTypes> auditTypes;
    private ArrayList<LocationsInfo> locationList;
    private String brandId = "";
    private String auditTypeId = "";
    private String auditId = "";
    private String month = "";
    private String locationId = "";
    private IADashboardInfo dashboardInfo;
    private RanksInfo ranksInfo;
    private OverallInfo overallInfo;
    private com.gdi.model.dashboard.CityInfo cityInfo;
    private com.gdi.model.dashboard.CountryInfo countryInfo;
    private GlobalInfo globalInfo;
    private ArrayList<HighestDeparmentInfo> highestDeparmentList;
    private ArrayList<LowestDepartmentInfo> lowestDepartmentList;
    private DashBoardHighestDeptAdapter dashBoardHighestDeptAdapter;
    private DashBoardLowestDeptAdapter dashBoardLowestDeptAdapter;
    ArrayList<IACurrentVsLastInfo> currentVsLastList;
    ArrayList<LastFiveInfo> lastFiveList;
    protected String[] mMonths = new String[] {
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dec"
    };
    protected Typeface mTfLight;
    Context context;
    private final int itemcount = 12;
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
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setActionBar();
        search = (Button) findViewById(R.id.btn_search);
        brandSearch = (Spinner) findViewById(R.id.spinner_brand);
        auditTypeSearch = (Spinner) findViewById(R.id.spinner_audit_type);
        auditMonthSearch = (TextView) findViewById(R.id.tv_audit_month);
        auditNameSearch = (Spinner) findViewById(R.id.spinner_audit_name);
        locationSearch = (Spinner) findViewById(R.id.spinner_location);
        gpBarChart = (HorizontalBarChart) findViewById(R.id.gp_bar_chart);
        pieChart = (PieChart) findViewById(R.id.pie_chart);
        //pieChart.setUsePercentValues(true);
        barChart = (BarChart) findViewById(R.id.bar_chart);
        overallLayout = (CardView) findViewById(R.id.overall_layout);
        departmentLayout = (CardView) findViewById(R.id.department_layout);
        last5Layout = (CardView) findViewById(R.id.last5_layout);
        filterList();
        search.setOnClickListener(this);
        auditMonthSearch.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_audit_month:
                Calendar auditMonthCal = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                Calendar cal = Calendar.getInstance();
                                cal.set(year, monthOfYear, dayOfMonth);
                                month = AppUtils.getAuditMonth(cal.getTime());
                                AppConstant.IA_FILTER_MONTH = month;
                                auditMonthSearch.setText(AppUtils.setAuditMonth(cal.getTime()));
                            }
                        }, auditMonthCal.get(Calendar.YEAR), auditMonthCal.get(Calendar.MONTH),
                        auditMonthCal.get(Calendar.YEAR));
                datePickerDialog.show();
                break;
            case R.id.btn_search:
                view.playSoundEffect(android.view.SoundEffectConstants.CLICK);
                getDashboard();
                break;
        }
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
                        IAFilterRootObject iaFilterRootObject = new GsonBuilder().create()
                                .fromJson(object.toString(), IAFilterRootObject.class);
                        if (iaFilterRootObject.getData() != null &&
                                iaFilterRootObject.getData().toString().length() > 0) {
                            iaFilterInfo = iaFilterRootObject.getData();
                            setFilter(iaFilterInfo);
                            auditMonthSearch.setText(AppConstant.IA_FILTER_MONTH);
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
        IAFilterRequest iaFilterRequest = new IAFilterRequest(AppPrefs.getAccessToken(context),
                stringListener, errorListener);
        VolleyNetworkRequest.getInstance(context).addToRequestQueue(iaFilterRequest);
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
                            overallLayout.setVisibility(View.VISIBLE);
                            departmentLayout.setVisibility(View.VISIBLE);
                            last5Layout.setVisibility(View.VISIBLE);
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
        AppLogger.e(TAG, "AyditType Id: " + auditTypeId);
        AppLogger.e(TAG, "Audit Id: " + auditId);
        AppLogger.e(TAG, "Month: " + month);
        AppLogger.e(TAG, "Location Id: " + locationId);
        String dashboardUrl = ApiEndPoints.IADASHBOARD + "?"
                + "audit_type=" + auditTypeId + "&"
                + "brand_id=" + brandId + "&"
                + "location_id=" + locationId + "&"
                + "audit_id=" + auditId + "&"
                + "audit_month=" + "2019-01";

        DashboardRequest dashboardRequest = new DashboardRequest(AppPrefs.getAccessToken(context),
                dashboardUrl, stringListener, errorListener);
        VolleyNetworkRequest.getInstance(context).addToRequestQueue(dashboardRequest);
    }

    private void setBrandFilter(IAFilterInfo iaFilterInfo){
        brandList = new ArrayList<>();
        BrandsInfo brandsInfo = new BrandsInfo();
        brandsInfo.setBrand_id(0);
        brandsInfo.setBrand_name("--select--");
        brandList.add(brandsInfo);
        brandList.addAll(iaFilterInfo.getBrands());
        ArrayAdapter<String> brandAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_dropdown_item);
        for (int i = 0; i < brandList.size(); i++) {
            brandAdapter.add(brandList.get(i).getBrand_name());
        }
        brandSearch.setAdapter(brandAdapter);

        brandSearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AppConstant.IA_FILTER_BRAND = position;
                brandId = ""+brandList.get(position).getBrand_id();
                AppLogger.e(TAG, "Brand Id: " + brandId);
                AppLogger.e(TAG, "Brand Position: " + AppConstant.IA_FILTER_BRAND);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        brandSearch.setSelection(AppConstant.IA_FILTER_BRAND);


    }

    private void setAuditTypeFilter(IAFilterInfo iaFilterInfo){
        auditTypes = new ArrayList<>();
        AuditTypes auditType = new AuditTypes();
        auditType.setId("0");
        auditType.setName("--select--");
        auditTypes.add(auditType);
        auditTypes.addAll(iaFilterInfo.getAudit_types());
        ArrayAdapter<String> auditTypeAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_dropdown_item);
        for (int i = 0; i < auditTypes.size(); i++) {
            auditTypeAdapter.add(auditTypes.get(i).getName());
        }
        auditTypeSearch.setAdapter(auditTypeAdapter);
        auditTypeSearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                auditTypeId = ""+auditTypes.get(position).getId();
                AppConstant.IA_FILTER_AUDIT_TYPE = position;
                AppLogger.e(TAG, "Campaign Id: " + auditTypeId);
                AppLogger.e(TAG, "Campaign position: " + AppConstant.IA_FILTER_AUDIT_TYPE);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        auditTypeSearch.setSelection(AppConstant.IA_FILTER_AUDIT_TYPE);
    }

    private void setAuditNameFilter(IAFilterInfo iaFilterInfo){
        audits = new ArrayList<>();
        Audit audit = new Audit();
        audit.setAudit_id("0");
        audit.setAudit_name("--select--");
        audits.add(audit);
        if (iaFilterInfo.getAudits() != null) {
            audits.addAll(iaFilterInfo.getAudits());
        }
        ArrayAdapter<String> auditAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_dropdown_item);
        for (int i = 0; i < audits.size(); i++) {
            auditAdapter.add(audits.get(i).getAudit_name());
        }
        auditNameSearch.setAdapter(auditAdapter);
        auditNameSearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                auditId = ""+audits.get(position).getAudit_id();
                AppConstant.IA_FILTER_AUDIT = position;
                AppLogger.e(TAG, "audit Id: " + auditId);
                AppLogger.e(TAG, "Audit Id: " + AppConstant.IA_FILTER_AUDIT);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        auditNameSearch.setSelection(AppConstant.IA_FILTER_AUDIT);

    }

    private void setLocationFilter(IAFilterInfo iaFilterInfo){
        locationList = new ArrayList<>();
        LocationsInfo locationsInfo = new LocationsInfo();
        locationsInfo.setLocation_id(0);
        locationsInfo.setLocation_name("--select--");
        locationList.add(locationsInfo);
        locationList.addAll(iaFilterInfo.getLocations());
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
                AppConstant.IA_FILTER_LOCATION = position;
                AppLogger.e(TAG, "Location Id: " + locationId);
                AppLogger.e(TAG, "Location position: " + AppConstant.IA_FILTER_LOCATION);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        locationSearch.setSelection(AppConstant.IA_FILTER_LOCATION);
    }

    private void setFilter(IAFilterInfo iaFilterInfo) {
        setBrandFilter(iaFilterInfo);
        setAuditTypeFilter(iaFilterInfo);
        setAuditNameFilter(iaFilterInfo);
        setLocationFilter(iaFilterInfo);
    }

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

            String last = currentVsLastList.get(i).getScore().getScore();
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
        barChart.setTouchEnabled(false);
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
