package com.gdi.activity.Audit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gdi.R;
import com.gdi.activity.BaseActivity;
import com.gdi.activity.SignInActivity;
import com.gdi.adapter.AuditActionAdapter;
import com.gdi.api.ApiEndPoints;
import com.gdi.api.FilterRequest;
import com.gdi.api.VolleyNetworkRequest;
import com.gdi.model.filter.BrandFilterRootObject;
import com.gdi.model.filter.BrandsInfo;
import com.gdi.model.filter.FilterLocationInfo;
import com.gdi.model.filter.FilterLocationModel;
import com.gdi.model.filter.LocationFilterRootObject;
import com.gdi.model.reportdetailedsummary.LocationInfo;
import com.gdi.utils.ApiResponseKeys;
import com.gdi.utils.AppLogger;
import com.gdi.utils.AppPrefs;
import com.gdi.utils.AppUtils;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AuditFilterActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.spinner_brand)
    Spinner brandSearch;
    @BindView(R.id.spinner_location)
    Spinner locationSearch;
    @BindView(R.id.btn_search)
    Button search;
    /*@BindView(R.id.audit_card)
    CardView auditCard;
    @BindView(R.id.recycler_view_audit)
    RecyclerView auditRecyclerView;
    @BindView(R.id.spinner_status)
    Spinner spinnerStatus;
    @BindView(R.id.spinner_brand_standard)
    Spinner spinnerBrandStandard;
    @BindView(R.id.spinner_detailed_summary)
    Spinner spinnerDetailedSummary;
    @BindView(R.id.spinner_executive_summary)
    Spinner spinnerExecutiveSummary;
    @BindView(R.id.cb_overdue)
    CheckBox cbOverdue;
    @BindView(R.id.cb_close_to_due_date)
    CheckBox cbCloseToDueDate;
    @BindView(R.id.rb_all)
    RadioButton rbAll;
    @BindView(R.id.rb_any)
    RadioButton rbAny;*/
    Context context;
    private ArrayList<BrandsInfo> brandList;
    private ArrayList<LocationInfo> locationList;
    private String brandId = "";
    private String locationId = "";
    private String typeId = "";
    private String type = "";
    private String bsFilter = "";
    private String dsFilter = "";
    private String esFilter = "";
    private String statusFilter = "";
    private String typeFilter = "";
    private String overDueFilter = "";
    private AuditActionAdapter auditActionAdapter;
    private boolean isFirstTime = false;
    private static final String TAG = AuditFilterActivity.class.getSimpleName();

    @Override
    protected void onResume() {
        super.onResume();
        AppUtils.hideKeyboard(AuditFilterActivity.this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audit);
        context = this;
        ButterKnife.bind(AuditFilterActivity.this);
        initView();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        setActionBar();
        typeId = getIntent().getStringExtra("type_id");
        type = getIntent().getStringExtra("type");
        //auditRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_audit);
        //auditCard = (CardView) findViewById(R.id.audit_card);
        search = findViewById(R.id.btn_search);
        brandSearch = findViewById(R.id.spinner_brand);
        locationSearch = findViewById(R.id.spinner_location);

        //locationCampaignRounds = new ArrayList<>();
        search.setOnClickListener(this);
        getBrandFilter();


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_search:
                //view.playSoundEffect(android.view.SoundEffectConstants.CLICK);
                //isFirstTime = true;
                Intent intent = new Intent(context, AssignmentActivity.class);
                intent.putExtra("brandId", brandId);
                intent.putExtra("locationId", locationId);
                intent.putExtra("typeId", typeId);
                intent.putExtra("type", type);
                startActivity(intent);
                //setAuditList();
                break;
        }
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
                            try {
                                AppLogger.e(TAG, "print1 ");
                                setBrandFilter(brandFilterRootObject.getData());
                                setLocationFilter(new FilterLocationModel());
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                        }

                    } else if (object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        AppUtils.toast((BaseActivity) context,
                                object.getString(ApiResponseKeys.RES_KEY_MESSAGE));
                        finish();
                        AppPrefs.clear(context);
                        startActivity(new Intent(context, SignInActivity.class));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }catch (IndexOutOfBoundsException e){
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

    private void getLocationFilter(String brandId) {
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
                            try {
                                setLocationFilter(locationCampaignRootObject.getData());
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                        }

                    } else if (object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        AppUtils.toast((BaseActivity) context,
                                object.getString(ApiResponseKeys.RES_KEY_MESSAGE));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }catch (IndexOutOfBoundsException e){
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
                + "campaign_id=";
        FilterRequest filterRequest = new FilterRequest(locationUrl,
                AppPrefs.getAccessToken(context), stringListener, errorListener);
        VolleyNetworkRequest.getInstance(context).addToRequestQueue(filterRequest);
    }

    private void setBrandFilter(ArrayList<BrandsInfo> brandsInfos) {
        try {
            final ArrayList<BrandsInfo> brandList = new ArrayList<>();
            BrandsInfo brandsInfo = new BrandsInfo();
            brandsInfo.setBrand_id(0);
            brandsInfo.setBrand_name("Select Brand");
            brandList.add(brandsInfo);
            brandList.addAll(brandsInfos);
            AppLogger.e(TAG, "print2 ");
            ArrayAdapter<String> brandAdapter = new ArrayAdapter<String>(context, R.layout.audit_filter_spinner_layout);
            for (int i = 0; i < brandList.size(); i++) {
                brandAdapter.add(brandList.get(i).getBrand_name());
            }
            // Specify the layout to use when the list of choices appears
            brandAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Apply the adapter to the spinner
            brandSearch.setAdapter(brandAdapter);
            brandSearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position > 0) {
                        AppLogger.e(TAG, "print3 ");
                        //AppPrefs.setFilterBrand(context, position);
                        brandId = "" + brandList.get(position).getBrand_id();
                        AppLogger.e(TAG, "print4 ");
                        locationSearch.setSelection(0);
                        getLocationFilter(brandId);
                        AppLogger.e(TAG, "Brand Id: " + brandId);
                        //AppLogger.e(TAG, "Brand Position: " + AppPrefs.getFilterBrand(context));
                    } else {
                        locationSearch.setSelection(0);
                        brandId = "";
                        locationId = "";
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setLocationFilter(FilterLocationModel locationModel) {
        try {
            final ArrayList<FilterLocationInfo> locationList = new ArrayList<>();
            FilterLocationInfo filterLocationInfo = new FilterLocationInfo();
            filterLocationInfo.setLocation_id(0);
            filterLocationInfo.setLocation_name("Select Location");
            locationList.add(filterLocationInfo);
            AppLogger.e(TAG, "Locatonprint1 ");
            if (locationModel.getLocations() != null) {
                locationList.addAll(locationModel.getLocations());
            }
            AppLogger.e(TAG, "Locatonprint2 ");
            ArrayAdapter<String> locationAdapter = new ArrayAdapter<String>(context,
                    R.layout.audit_filter_spinner_layout);
            // Specify the layout to use when the list of choices appears
            locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Apply the adapter to the spinner
            for (int i = 0; i < locationList.size(); i++) {
                locationAdapter.add(locationList.get(i).getLocation_name());
            }
            AppLogger.e(TAG, "Locatonprint3 ");
            locationSearch.setAdapter(locationAdapter);
            AppLogger.e(TAG, "Locatonprint4 ");
            locationSearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    AppLogger.e(TAG, "Locatonprint5");
                    locationId = "" + locationList.get(position).getLocation_id();
                    //AppPrefs.setFilterLocation(context, position);
                    AppLogger.e(TAG, "Location Id: " + locationId);
                    AppLogger.e(TAG, "Location position: " + AppPrefs.getFilterLocation(context));

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setActionBar() {
        initToolbar(toolbar);
        setTitle("Select Filters");
        enableBack(true);
        enableBackPressed();
    }

    /*private void setAuditList(){
        showProgressDialog();
        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AppLogger.e(TAG, "AudioImageResponse: " + response);
                try {
                    JSONObject object = new JSONObject(response);

                    if (!object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        AuditRootObject auditRootObject = new GsonBuilder().create()
                                .fromJson(object.toString(), AuditRootObject.class);
                        if (auditRootObject.getData() != null &&
                                auditRootObject.getData().toString().length() > 0) {
                            ArrayList<AuditInfo> arrayList = new ArrayList<>();
                            arrayList.addAll(auditRootObject.getData());
                            setAuditList(arrayList);
                            auditActionAdapter.notifyDataSetChanged();
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
                AppLogger.e(TAG, "AudioImageError: " + error.getMessage());
                AppUtils.toast((BaseActivity) context, "Server temporary unavailable, Please try again");

            }
        };
        AppLogger.e(TAG, "Brand Id: " + brandId);
        AppLogger.e(TAG, "Location Id: " + locationId);
        String integrityUrl = ApiEndPoints.AUDITlIST + "?"
                + "brand_id=" + brandId + "&"
                + "location_id=" + locationId  + "&"
                + "filter_brand_std_status=" + bsFilter + "&"
                + "filter_detailed_sum_status=" + dsFilter + "&"
                + "filter_exec_sum_status=" + esFilter + "&"
                + "status=" + statusFilter + "&"
                + "type=" + typeFilter + "&"
                + "overdue=" + overDueFilter;
        GetReportRequest getReportRequest = new GetReportRequest(AppPrefs.getAccessToken(context),
                integrityUrl, stringListener, errorListener);
        VolleyNetworkRequest.getInstance(context).addToRequestQueue(getReportRequest);
    }

    private void setAuditList(ArrayList<AuditInfo> arrayList){
        ArrayList<AuditInfo> integrityInfos = new ArrayList<>();
        integrityInfos.addAll(arrayList);
        auditActionAdapter = new AuditActionAdapter(context, integrityInfos);
        auditRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        auditRecyclerView.setAdapter(auditActionAdapter);
    }

    private void filterChecked(){

        spinnerStatus = (Spinner) findViewById(R.id.spinner_status);
        spinnerBrandStandard = (Spinner) findViewById(R.id.spinner_brand_standard);
        spinnerDetailedSummary = (Spinner) findViewById(R.id.spinner_detailed_summary);
        spinnerExecutiveSummary = (Spinner) findViewById(R.id.spinner_executive_summary);
        cbOverdue = (CheckBox) findViewById(R.id.cb_overdue);
        cbCloseToDueDate = (CheckBox) findViewById(R.id.cb_close_to_due_date);
        rbAll = (RadioButton) findViewById(R.id.rb_all);
        rbAny = (RadioButton) findViewById(R.id.rb_any);

        ArrayAdapter<CharSequence> dealerClassificationAdapter = ArrayAdapter.createFromResource(this,
                R.array.audit_action_filter, R.layout.audit_filter_spinner_layout);
        // Specify the layout to use when the list of choices appears
        dealerClassificationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerDetailedSummary.setAdapter(dealerClassificationAdapter);
        spinnerBrandStandard.setAdapter(dealerClassificationAdapter);
        spinnerExecutiveSummary.setAdapter(dealerClassificationAdapter);
        spinnerStatus.setAdapter(dealerClassificationAdapter);

        spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (isFirstTime) {

                    switch (i) {
                        case 0:
                            rbAll.setChecked(true);
                            rbAll.setEnabled(false);
                            rbAny.setEnabled(false);
                            spinnerExecutiveSummary.setEnabled(true);
                            spinnerBrandStandard.setEnabled(true);
                            spinnerDetailedSummary.setEnabled(true);
                            statusFilter = "0";
                            setAuditList();
                            break;
                        case 1:
                            statusFilter = "1";
                            rbAll.setEnabled(true);
                            rbAny.setEnabled(true);
                            spinnerExecutiveSummary.setEnabled(false);
                            spinnerBrandStandard.setEnabled(false);
                            spinnerDetailedSummary.setEnabled(false);
                            setAuditList();
                            break;
                        case 2:
                            statusFilter = "2";
                            rbAll.setEnabled(true);
                            rbAny.setEnabled(true);
                            spinnerExecutiveSummary.setEnabled(false);
                            spinnerBrandStandard.setEnabled(false);
                            spinnerDetailedSummary.setEnabled(false);
                            setAuditList();
                            break;
                        case 3:
                            statusFilter = "3";
                            rbAll.setEnabled(true);
                            rbAny.setEnabled(true);
                            spinnerExecutiveSummary.setEnabled(false);
                            spinnerBrandStandard.setEnabled(false);
                            spinnerDetailedSummary.setEnabled(false);
                            setAuditList();
                            break;
                        case 4:
                            statusFilter = "4";
                            rbAll.setEnabled(true);
                            rbAny.setEnabled(true);
                            spinnerExecutiveSummary.setEnabled(false);
                            spinnerBrandStandard.setEnabled(false);
                            spinnerDetailedSummary.setEnabled(false);
                            setAuditList();
                            break;
                        case 5:
                            statusFilter = "5";
                            rbAll.setEnabled(true);
                            rbAny.setEnabled(true);
                            spinnerExecutiveSummary.setEnabled(false);
                            spinnerBrandStandard.setEnabled(false);
                            spinnerDetailedSummary.setEnabled(false);
                            setAuditList();
                            break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerDetailedSummary.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (isFirstTime) {
                    switch (i) {
                        case 0:
                            dsFilter = "0";
                            setAuditList();
                            break;
                        case 1:
                            dsFilter = "1";
                            setAuditList();
                            break;
                        case 2:
                            dsFilter = "2";
                            setAuditList();
                            break;
                        case 3:
                            dsFilter = "3";
                            setAuditList();
                            break;
                        case 4:
                            dsFilter = "4";
                            setAuditList();
                            break;
                        case 5:
                            dsFilter = "5";
                            setAuditList();
                            break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerBrandStandard.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (isFirstTime) {
                    switch (i) {
                        case 0:
                            bsFilter = "0";
                            setAuditList();
                            break;
                        case 1:
                            bsFilter = "1";
                            setAuditList();
                            break;
                        case 2:
                            bsFilter = "2";
                            setAuditList();
                            break;
                        case 3:
                            bsFilter = "3";
                            setAuditList();
                            break;
                        case 4:
                            bsFilter = "4";
                            setAuditList();
                            break;
                        case 5:
                            bsFilter = "5";
                            setAuditList();
                            break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerExecutiveSummary.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (isFirstTime) {
                    switch (i) {
                        case 0:
                            esFilter = "0";
                            setAuditList();
                            break;
                        case 1:
                            esFilter = "1";
                            setAuditList();
                            break;
                        case 2:
                            esFilter = "2";
                            setAuditList();
                            break;
                        case 3:
                            esFilter = "3";
                            setAuditList();
                            break;
                        case 4:
                            esFilter = "4";
                            setAuditList();
                            break;
                        case 5:
                            esFilter = "5";
                            setAuditList();
                            break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        cbOverdue.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    overDueFilter = "1";
                    cbCloseToDueDate.setChecked(false);
                    setAuditList();
                } else {

                }

            }
        });

        cbCloseToDueDate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    overDueFilter = "2";
                    cbOverdue.setChecked(false);
                    setAuditList();
                } else {

                }

            }
        });

        rbAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    overDueFilter = "1";
                    rbAny.setChecked(false);
                    setAuditList();
                } else {

                }
            }
        });

        rbAny.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    overDueFilter = "0";
                    rbAll.setChecked(false);
                    setAuditList();
                } else {

                }
            }
        });
    }*/
}
