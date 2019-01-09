package com.gdi.activity;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import com.gdi.adapter.AuditAdapter;
import com.gdi.api.ApiEndPoints;
import com.gdi.api.AuditRequest;
import com.gdi.api.FilterRequest;
import com.gdi.api.SendToEmailRequest;
import com.gdi.api.VolleyNetworkRequest;
import com.gdi.model.SampleModel;
import com.gdi.model.audit.AuditInfo;
import com.gdi.model.audit.AuditRootObject;
import com.gdi.model.audit.DashBoardInfo;
import com.gdi.model.audit.DepatmentOverallInfo;
import com.gdi.model.audit.HotelOverallInfo;
import com.gdi.model.filter.BrandsInfo;
import com.gdi.model.filter.CampaignsInfo;
import com.gdi.model.filter.CityInfo;
import com.gdi.model.filter.CountryInfo;
import com.gdi.model.filter.FilterInfo;
import com.gdi.model.filter.FilterRootObject;
import com.gdi.model.filter.LocationsInfo;
import com.gdi.utils.ApiResponseKeys;
import com.gdi.utils.AppConstant;
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

public class AuditActivity extends BaseActivity implements
        DownloadPdfTask.PDFDownloadFinishedListner,
        DownloadExcelTask.DownloadExcelFinishedListner {

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
    Context context;
    private ArrayList<DepatmentOverallInfo> auditDepartmentList;
    private AuditInfo auditInfos;
    private FilterInfo filterInfo;
    private ArrayList<BrandsInfo> brandList;
    private ArrayList<CampaignsInfo> campaignList;
    private ArrayList<CountryInfo> countryList;
    private ArrayList<CityInfo> cityList;
    private ArrayList<LocationsInfo> locationList;
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
    private static int REQUEST_FOR_WRITE_PDF = 1;
    private static int REQUEST_FOR_WRITE_EXCEL = 100;
    private static final String TAG = AuditActivity.class.getSimpleName();

    @Override
    protected void onResume() {
        super.onResume();
        AppUtils.hideKeyboard(AuditActivity.this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audit);
        context = this;
        ButterKnife.bind(AuditActivity.this);
        initView();
        //webView.loadUrl("https://docs.google.com/viewer?url=" + "url of pdf file");
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setActionBar();
        search = (Button) findViewById(R.id.btn_search);
        list1 = (RecyclerView) findViewById(R.id.audit_recycler);
        dashboardScore = (TextView) findViewById(R.id.dashboard_score_txt);
        dashboardCity = (TextView) findViewById(R.id.dashboard_city_txt);
        dashboardCountry = (TextView) findViewById(R.id.dashboard_country_txt);
        dashboardPdfIcon = (ImageView) findViewById(R.id.dashboard_pdf_icon);
        dashboardMailIcon = (ImageView) findViewById(R.id.dashboard_mail_icon);
        hotelScore = (TextView) findViewById(R.id.hotel_score_txt);
        hotelCity = (TextView) findViewById(R.id.hotel_city_txt);
        hotelCountry = (TextView) findViewById(R.id.hotel_country_txt);
        hotelPdfIcon = (ImageView) findViewById(R.id.hotel_pdf_icon);
        hotelExcelIcon = (ImageView) findViewById(R.id.hotel_excel_icon);
        hotelMailIcon = (ImageView) findViewById(R.id.hotel_mail_icon);
        brandSearch = (Spinner) findViewById(R.id.spinner_brand);
        auditRoundSearch = (Spinner) findViewById(R.id.spinner_audit_round);
        countrySearch = (Spinner) findViewById(R.id.spinner_country);
        citySearch = (Spinner) findViewById(R.id.spinner_city);
        locationSearch = (Spinner) findViewById(R.id.spinner_location);
        expandLayout = (RelativeLayout) findViewById(R.id.expandLayout2);
        dashboardLayout = (CardView) findViewById(R.id.dashboard_card);
        hotelOverallLayout = (CardView) findViewById(R.id.hotel_overall_card);
        departmentLayout = (CardView) findViewById(R.id.audit_department_card);
        expandIcon = (ImageView) findViewById(R.id.expand_icon);

        filterList();//set filter by call filet api

        expandLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!expand) {
                    expand = true;
                    list1.setVisibility(View.VISIBLE);
                    expandIcon.setImageResource(R.drawable.compress_icon);
                    setAuditDeparment();
                    //setAuditDeparmentOffline();
                } else if (expand) {
                    expand = false;
                    list1.setVisibility(View.GONE);
                    expandIcon.setImageResource(R.drawable.expand_icon);
                }

            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.playSoundEffect(android.view.SoundEffectConstants.CLICK);
                auditList();
                //TODO : Static data testing
                //setAuditDeparmentOffline();

            }
        });

        dashboardPdfIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadPdf(dashBoardInfo.getReport_urls().getPdf());
            }
        });

        dashboardMailIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sentEmail(dashBoardInfo.getReport_urls().getEmail());
            }
        });

        hotelPdfIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadPdf(hotelOverallInfo.getReport_urls().getPdf());
            }
        });

        hotelExcelIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadExcel(hotelOverallInfo.getReport_urls().getExcel());
            }
        });

        hotelMailIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sentEmail(hotelOverallInfo.getReport_urls().getEmail());
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
        FilterRequest filterRequest = new FilterRequest(AppPrefs.getAccessToken(context),
                stringListener, errorListener);
        VolleyNetworkRequest.getInstance(context).addToRequestQueue(filterRequest);
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
                        AuditRootObject auditRootObject = new GsonBuilder().create()
                                .fromJson(object.toString(), AuditRootObject.class);
                        if (auditRootObject.getData() != null &&
                                auditRootObject.getData().toString().length() > 0) {
                            auditInfos = auditRootObject.getData();
                            setAuditDashboard();
                            setAuditHotelOverall();
                            dashboardLayout.setVisibility(View.VISIBLE);
                            hotelOverallLayout.setVisibility(View.VISIBLE);
                            departmentLayout.setVisibility(View.VISIBLE);
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
                            dashboardLayout.setVisibility(View.GONE);
                            hotelOverallLayout.setVisibility(View.GONE);
                            departmentLayout.setVisibility(View.GONE);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } /*catch (Exception e){
                    e.printStackTrace();
                    AppUtils.toast((BaseActivity) context,
                           "No result found!");
                }*/
                hideProgressDialog();
            }

        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
                AppLogger.e(TAG, "Audit Error: " + error.getMessage());

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
        AuditRequest auditRequest = new AuditRequest(AppPrefs.getAccessToken(context),
                auditUrl, stringListener, errorListener);
        VolleyNetworkRequest.getInstance(context).addToRequestQueue(auditRequest);
    }

    private void setAuditDashboard() {
        dashBoardInfo = auditInfos.getDashboard();
        dashboardScore.setText(dashBoardInfo.getScore());
        dashboardCity.setText(dashBoardInfo.getCity());
        dashboardCountry.setText(dashBoardInfo.getCountry());
    }

    private void setAuditHotelOverall() {
        hotelOverallInfo = auditInfos.getHotel_overall();
        hotelScore.setText(hotelOverallInfo.getScore());
        hotelCity.setText(hotelOverallInfo.getCity());
        hotelCountry.setText(hotelOverallInfo.getCountry());
    }

    private void setAuditDeparment() {
        auditDepartmentList = new ArrayList<>();
        auditDepartmentList.clear();
        auditDepartmentList.addAll(auditInfos.getDepartment_overall());
        AppLogger.e(TAG, "List Size: " + auditDepartmentList.size());
        auditAdapter = new AuditAdapter(context, auditDepartmentList);
        list1.setLayoutManager(new LinearLayoutManager(context));
        list1.setAdapter(auditAdapter);
    }

    private void setAuditDeparmentOffline() {
        departmentLayout.setVisibility(View.VISIBLE);
        ArrayList<SampleModel> sampleModels = SampleModel.createList(5);
        //auditAdapter = new AuditAdapter(context, sampleModels);
        list1.setLayoutManager(new LinearLayoutManager(context));
        list1.setAdapter(auditAdapter);
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

    private void setCityFilter(FilterInfo filterInfo){
        cityList = new ArrayList<>();
        CityInfo cityInfo = new CityInfo();
        cityInfo.setCity_id(0);
        cityInfo.setCity_name("--select--");
        cityList.add(cityInfo);
        cityList.addAll(filterInfo.getCity());
        ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_dropdown_item);
        for (int i = 0; i < cityList.size(); i++) {
            cityAdapter.add(cityList.get(i).getCity_name());
        }
        citySearch.setAdapter(cityAdapter);
        citySearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cityId = ""+cityList.get(position).getCity_id();
                AppConstant.FILTER_CITY = position;
                AppLogger.e(TAG, "City Id: " + cityId);
                AppLogger.e(TAG, "City Name: " + AppConstant.FILTER_CITY);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        citySearch.setSelection(AppConstant.FILTER_CITY);

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
        setCityFilter(filterInfo);
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

        final EditText emailId = (EditText) view.findViewById(R.id.send_email_edt_txt);

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
                                /*AppUtils.toast((BaseActivity) context,
                                        getString(R.string.alert_msg_invalid_response));*/
                        }
                        e1.printStackTrace();
                    } catch (JSONException e2) {
                        if (context != null) {
                            Toast.makeText(context, "Invalid Responce", Toast.LENGTH_SHORT).show();
                                /*AppUtils.toast((BaseActivity) context,
                                        getString(R.string.alert_msg_invalid_response));  */
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
            ActivityCompat.requestPermissions(AuditActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_FOR_WRITE_PDF);
        } else {
            DownloadPdfTask downloadTask = new DownloadPdfTask(context, url, AuditActivity.this);
        }
    }

    public void downloadExcel(final String url) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(AuditActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_FOR_WRITE_EXCEL);
        } else {
            DownloadExcelTask downloadTask = new DownloadExcelTask(context, url, AuditActivity.this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_FOR_WRITE_PDF) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                String fileUrl = dashBoardInfo.getReport_urls().getPdf();
                downloadPdf(fileUrl);
            } else {
                // permission denied, boo! Disable the
                // functionality that depends on this permission.
            }
        }

        if (requestCode == REQUEST_FOR_WRITE_EXCEL) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                /*String fileUrl = dashBoardInfo.getReport_urls().getPdf();
                downloadPdf(fileUrl);*/
                downloadExcel(hotelOverallInfo.getReport_urls().getExcel());
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
        target.setDataAndType(Uri.fromFile(file),"application/pdf");
        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        Intent intent = Intent.createChooser(target, "Open File");
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
            // Instruct the user to install a PDF reader here, or something
        }
    }

    @Override
    public void onExcelDownloadFinished(String path) {
        File file = new File(path);
        Intent target = new Intent(Intent.ACTION_VIEW);
        target.setDataAndType(Uri.fromFile(file),"application/vnd.ms-excel");
        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        Intent intent = Intent.createChooser(target, "Open File");
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
            // Instruct the user to install a PDF reader here, or something
        }
    }
}
