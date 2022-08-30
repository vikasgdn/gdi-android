package com.gdi.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gdi.api.GetReportRequest;
import com.gdi.hotel.mystery.audits.R;
import com.gdi.adapter.MistryAuditDashboardAdapter;
import com.gdi.api.NetworkURL;
import com.gdi.api.FilterRequest;
import com.gdi.api.VolleyNetworkRequest;
import com.gdi.model.dashboard.AuditDashboardRootObject;
import com.gdi.model.dashboard.IAMainDashboardInfo;
import com.gdi.model.filter.BrandFilterRootObject;
import com.gdi.model.filter.BrandsInfo;
import com.gdi.model.filter.FilterLocationInfo;
import com.gdi.model.filter.FilterLocationModel;
import com.gdi.model.filter.LocationFilterRootObject;
import com.gdi.utils.ApiResponseKeys;
import com.gdi.utils.AppConstant;
import com.gdi.utils.AppLogger;
import com.gdi.utils.AppPrefs;
import com.gdi.utils.AppUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MisteryAuditDashboardActivity extends BaseActivity {

    @BindView(R.id.recycler_view_audit_dashboard)
    RecyclerView listRecyclerView;
    @BindView(R.id.btn_search)
    Button search;
    @BindView(R.id.spinner_audit_type)
    Spinner auditTypeSearch;
    @BindView(R.id.spinner_brand)
    Spinner brandSearch;
    @BindView(R.id.spinner_location)
    Spinner locationSearch;
    @BindView(R.id.spinner_audit_status)
    Spinner auditStatusSearch;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    Context context;

    private boolean isFirstTimeAuditType = true;
    private String auditTypeId = "";

    private String brandId = "";
    private boolean isFirstTimeBrand = true;

    private String locationId = "";
    private boolean isFirstTimeLocation = true;

    private boolean isFirstTimeAuditStatus = true;
    private String auditStatusId = "";

    private static final String TAG = MisteryAuditDashboardActivity.class.getSimpleName();
    private ArrayList<IAMainDashboardInfo> iaMainDashboardInfos;

    @Override
    protected void onResume() {
        super.onResume();
        AppUtils.hideKeyboard(MisteryAuditDashboardActivity.this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mistry_audit_dashboard);
        ButterKnife.bind(MisteryAuditDashboardActivity.this);
        context = this;
        initView();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        setActionBar();
        search = findViewById(R.id.btn_search);
        listRecyclerView = findViewById(R.id.recycler_view_audit_dashboard);
        listRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        brandSearch = findViewById(R.id.spinner_brand);
        auditTypeSearch = findViewById(R.id.spinner_audit_type);
        locationSearch = findViewById(R.id.spinner_location);
        auditStatusSearch = findViewById(R.id.spinner_audit_status);
        iaMainDashboardInfos = new ArrayList<>();


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.playSoundEffect(android.view.SoundEffectConstants.CLICK);
                getDashboard();
            }
        });

        setAuditTypeFilter();
        setBrandFilter(new ArrayList<BrandsInfo>());
        setLocationFilter(new FilterLocationModel());
        setAuditStatusFilter();

    }

    private void setActionBar() {
        initToolbar(toolbar);
        setTitle("Audit Dashboard");
        enableBack(true);
        enableBackPressed();
    }


    private void setAuditTypeFilter() {
        ArrayList<String> auditTypes = new ArrayList<>();
        auditTypes.add("Select");
        auditTypes.add("Self Assessment");
        auditTypes.add("Heart of the House");
        auditTypes.add("Inspection");
        auditTypes.add("Pre Opening");

        ArrayAdapter<String> auditTypeAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_dropdown_item);
        for (int i = 0; i < auditTypes.size(); i++) {
            auditTypeAdapter.add(auditTypes.get(i));
        }
        auditTypeSearch.setAdapter(auditTypeAdapter);
        auditTypeSearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isFirstTimeAuditType) {
                    isFirstTimeAuditType = false;
                    /*if (AppPrefs.getIaFilterAuditType(context) > 0) {
                        auditTypeId = "" + position;
                    } else {
                        auditTypeSearch.setSelection(0);
                    }*/
                } else {
                    if (position > 0) {
                        locationSearch.setSelection(0);
                        /*AppPrefs.setIaFilterAuditType(context, position);
                        AppPrefs.setIaFilterLocation(context, 0);
                       */ auditTypeId = "" + position;
                        AppLogger.e(TAG, "AuditType Id: " + auditTypeId);
                       // AppLogger.e(TAG, "AuditType Position: " + AppPrefs.getIaFilterAuditType(context));
                        getBrandFilter();
                    } else {
                        brandSearch.setSelection(0);
                        locationSearch.setSelection(0);
                        brandId = "";
                        locationId = "";

                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
       // brandSearch.setSelection(AppPrefs.getFilterBrand(context));
        brandSearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (isFirstTimeBrand) {
                    isFirstTimeBrand = false;
                   /* if (AppPrefs.getIaFilterBrand(context) > 0) {
                        brandId = "" + brandList.get(position).getBrand_id();
                        getLocationFilter();
                    } else {
                        locationSearch.setSelection(0);
                    }*/
                } else {
                    if (position > 0) {
                        locationSearch.setSelection(0);
                        /*AppPrefs.setIaFilterBrand(context, position);
                        AppPrefs.setIaFilterLocation(context, 0);
                        AppPrefs.setIaFilterAuditName(context, 0);
                        */brandId = "" + brandList.get(position).getBrand_id();
                        getLocationFilter();
                        AppLogger.e(TAG, "Brand Id: " + brandId);
                        //AppLogger.e(TAG, "Brand Position: " + AppPrefs.getIaFilterBrand(context));
                    } else {
                        locationSearch.setSelection(0);
                        brandId = "";
                        locationId = "";

                    }
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
        if(locationModel.getLocations()!=null && locationModel.getLocations().size()>0)
        locationList.addAll(locationModel.getLocations());
        ArrayAdapter<String> locationAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_dropdown_item);
        for (int i = 0; i < locationList.size(); i++) {
            locationAdapter.add(locationList.get(i).getLocation_name());
        }
        locationSearch.setAdapter(locationAdapter);
        //locationSearch.setSelection(AppPrefs.getFilterLocation(context));
        locationSearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isFirstTimeLocation) {
                    isFirstTimeLocation = false;
                   /* if (AppPrefs.getIaFilterLocation(context) > 0) {
                        locationId = "" + locationList.get(position).getLocation_id();

                    } else {
                    }*/
                } else {
                    if (position > 0) {
                        /*AppPrefs.setIaFilterLocation(context, position);
                        AppPrefs.setIaFilterAuditName(context, 0);
                        */locationId = "" + locationList.get(position).getLocation_id();
                        AppLogger.e(TAG, "Location Id: " + locationId);
                        //AppLogger.e(TAG, "Location Position: " + AppPrefs.getIaFilterLocation(context));
                    } else {
                        locationId = "";

                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void setAuditStatusFilter() {
        ArrayList<String> auditTypes = new ArrayList<>();
        auditTypes.add("All");
        auditTypes.add("Pending");
        auditTypes.add("Completed");

        ArrayAdapter<String> auditTypeAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_dropdown_item);
        for (int i = 0; i < auditTypes.size(); i++) {
            auditTypeAdapter.add(auditTypes.get(i));
        }
        auditStatusSearch.setAdapter(auditTypeAdapter);
        auditStatusSearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isFirstTimeAuditStatus) {
                    isFirstTimeAuditStatus = false;
                } else {
                    if(iaMainDashboardInfos.size()>0){
                        if (auditStatusSearch.getSelectedItemPosition() == 0) {
                            setAdapter();
                        } else if (auditStatusSearch.getSelectedItemPosition() == 1) {
                            setFilterPending();
                        } else {
                            setFilterCompleted();
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
                            setLocationFilter(locationModel);
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
        String locationUrl = NetworkURL.FILTERLOCATION + "?"
                + "brand_id=" + brandId + "&"
                + "campaign_id=" + "";
      /*  FilterRequest filterRequest = new FilterRequest(locationUrl,
                AppPrefs.getAccessToken(context), stringListener, errorListener);
        VolleyNetworkRequest.getInstance(context).addToRequestQueue(filterRequest);
*/
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseAuth.getInstance().getCurrentUser().getIdToken(true)
                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            if (task.isSuccessful()) {
                                FilterRequest filterRequest = new FilterRequest(locationUrl, AppPrefs.getAccessToken(context),task.getResult().getToken(), stringListener, errorListener);
                                VolleyNetworkRequest.getInstance(context).addToRequestQueue(filterRequest);

                            }
                        }
                    });
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
        String brandUrl = NetworkURL.FILTERBRAND + "?"
                + "audit_type_id=" + auditTypeId;

   /*     FilterRequest filterRequest = new FilterRequest(brandUrl,
                AppPrefs.getAccessToken(context), stringListener, errorListener);
        VolleyNetworkRequest.getInstance(context).addToRequestQueue(filterRequest);
*/
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseAuth.getInstance().getCurrentUser().getIdToken(true)
                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            if (task.isSuccessful()) {
                                FilterRequest filterRequest = new FilterRequest(brandUrl, AppPrefs.getAccessToken(context),task.getResult().getToken(), stringListener, errorListener);
                                VolleyNetworkRequest.getInstance(context).addToRequestQueue(filterRequest);

                            }
                        }
                    });
        }

    }


    public void getDashboard() {
        showProgressDialog();
        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AppLogger.e(TAG, "InterAuditDashboard Response: " + response);
                try {
                    JSONObject object = new JSONObject(response);

                    if (!object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        AuditDashboardRootObject dashboardRootObject = new GsonBuilder().create()
                                .fromJson(object.toString(), AuditDashboardRootObject.class);
                        if (dashboardRootObject.getData() != null &&
                                dashboardRootObject.getData().size() > 0) {

                            iaMainDashboardInfos.addAll(dashboardRootObject.getData());
                            if(auditStatusSearch.getSelectedItemPosition()==0){
                                setAdapter();
                            }else if(auditStatusSearch.getSelectedItemPosition() == 1){
                                setFilterPending();
                            }else {
                                setFilterCompleted();
                            }
                            //setAdapter();
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

        AppLogger.e(TAG, "Location Id: " + locationId);
        String dashboardUrl = NetworkURL.INTERNALAUDITDASHBOARD + "?"
                + "audit_type_id=" + auditTypeId + "&"
                + "brand_id[]=" + brandId + "&"
                + "location_id[]=" + locationId + "&page=1";

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseAuth.getInstance().getCurrentUser().getIdToken(true)
                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            if (task.isSuccessful()) {
                                GetReportRequest getReportRequest = new GetReportRequest(AppPrefs.getAccessToken(context),task.getResult().getToken(), dashboardUrl, stringListener, errorListener);
                                VolleyNetworkRequest.getInstance(context).addToRequestQueue(getReportRequest);
                            }
                        }
                    });
        }
    }


    private void setAdapter(){
        listRecyclerView.setAdapter(new MistryAuditDashboardAdapter(context,iaMainDashboardInfos));
    }


    private void setFilterPending(){
        ArrayList<IAMainDashboardInfo> filterArray = new ArrayList<>();
        for(int i=0;i<iaMainDashboardInfos.size();i++){
            if(iaMainDashboardInfos.get(i).getReport_status()==0){
                filterArray.add(iaMainDashboardInfos.get(i));
            }
        }
        listRecyclerView.setAdapter(new MistryAuditDashboardAdapter(context,filterArray));

    }

    private void setFilterCompleted(){
        ArrayList<IAMainDashboardInfo> filterArray = new ArrayList<>();
        for(int i=0;i<iaMainDashboardInfos.size();i++){
            if(iaMainDashboardInfos.get(i).getReport_status()==1){
                filterArray.add(iaMainDashboardInfos.get(i));
            }
        }
        listRecyclerView.setAdapter(new MistryAuditDashboardAdapter(context,filterArray));
    }

}
