package com.gdi.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gdi.hotel.mystery.audits.R;

import com.gdi.adapter.ActionPlanAdapter;
import com.gdi.api.ApiEndPoints;
import com.gdi.api.FilterRequest;
import com.gdi.api.GetReportRequest;
import com.gdi.api.VolleyNetworkRequest;
import com.gdi.model.actionplan.ActionPlanModel;
import com.gdi.model.actionplan.ActionPlanRootObject;
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

public class ActionPlanActivity extends BaseActivity implements View.OnClickListener {

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
    @BindView(R.id.action_plan_card)
    CardView actionPlanCard;
    @BindView(R.id.recycler_view_action_plan)
    RecyclerView actionPlanRecyclerView;
    @BindView(R.id.action_plan_expand_layout)
    RelativeLayout expandLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.btn_search)
    Button search;
    Context context;
    private ActionPlanAdapter actionPlanAdapter;
    private ArrayList<ActionPlanModel> actionPlanModels;
    private FilterInfo filterInfo;
    private ArrayList<BrandsInfo> brandList;
    private ArrayList<CampaignsInfo> campaignList;
    private ArrayList<FilterCountryInfo> countryList;
    private ArrayList<FilterCityInfo> cityList;
    private ArrayList<FilterLocationInfo> locationList;
    private String brandId = "";
    private String campaignId = "";
    private String countryId = "";
    private String cityId = "";
    private String locationId = "";
    private boolean expand = false;
    private boolean isFirstTime = true;
    private boolean isFirstCompaignLoad = true;
    private boolean isFirstCountryLoad = true;
    private boolean isFirstCityLoad = true;
    private static final String TAG = ActionPlanActivity.class.getSimpleName();

    @Override
    protected void onResume() {
        super.onResume();
        AppUtils.hideKeyboard(ActionPlanActivity.this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_plan);
        context = this;
        ButterKnife.bind(ActionPlanActivity.this);
        initView();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        setActionBar();
        actionPlanRecyclerView = findViewById(R.id.recycler_view_action_plan);
        actionPlanCard = findViewById(R.id.action_plan_card);
        expandLayout = findViewById(R.id.action_plan_expand_layout);
        search = findViewById(R.id.btn_search);
        brandSearch = findViewById(R.id.spinner_brand);
        auditRoundSearch = findViewById(R.id.spinner_audit_round);
        countrySearch = findViewById(R.id.spinner_country);
        citySearch = findViewById(R.id.spinner_city);
        locationSearch = findViewById(R.id.spinner_location);
        search.setOnClickListener(this);
        expandLayout.setOnClickListener(this);
        getBrandFilter();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_search:
                AppPrefs.setFilterBrand(context, brandSearch.getSelectedItemPosition());
                AppPrefs.setFilterCampaign(context, auditRoundSearch.getSelectedItemPosition());
                AppPrefs.setFilterCity(context, citySearch.getSelectedItemPosition());
                AppPrefs.setFilterCountry(context, countrySearch.getSelectedItemPosition());
                AppPrefs.setFilterLocation(context, locationSearch.getSelectedItemPosition());
                view.playSoundEffect(android.view.SoundEffectConstants.CLICK);
                actionPlanList();
                break;
            case R.id.action_plan_expand_layout:
                break;
        }
    }

    public void actionPlanList() {
        showProgressDialog();
        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AppLogger.e(TAG, "Audit Response: " + response);
                try {
                    JSONObject object = new JSONObject(response);

                    if (!object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        ActionPlanRootObject actionPlanRootObject = new GsonBuilder().create()
                                .fromJson(object.toString(), ActionPlanRootObject.class);
                        if (actionPlanRootObject.getData() != null &&
                                actionPlanRootObject.getData().toString().length() > 0) {
                            ArrayList<ActionPlanModel> list = new ArrayList<>();
                            list.addAll(actionPlanRootObject.getData());
                            setActionPlanList(list);
                            actionPlanRecyclerView.setVisibility(View.VISIBLE);
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
                        actionPlanRecyclerView.setVisibility(View.GONE);
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

            }
        };
        AppLogger.e(TAG, "Brand Id: " + brandId);
        AppLogger.e(TAG, "Campaign Id: " + campaignId);
        AppLogger.e(TAG, "Country Id: " + countryId);
        AppLogger.e(TAG, "City Id: " + cityId);
        AppLogger.e(TAG, "Location Id: " + locationId);
        String actionPlanUrl = ApiEndPoints.ACTIONPLAN + "?"
                + "brand_id=" + brandId + "&"
                + "campaign_id=" + campaignId + "&"
                + "location_id=" + locationId + "&"
                + "country_id=" + countryId + "&"
                + "city_id=" + cityId;

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseAuth.getInstance().getCurrentUser().getIdToken(true)
                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            if (task.isSuccessful()) {
                                GetReportRequest getReportRequest = new GetReportRequest(AppPrefs.getAccessToken(context),task.getResult().getToken(), actionPlanUrl, stringListener, errorListener);
                                VolleyNetworkRequest.getInstance(context).addToRequestQueue(getReportRequest);
                            }

                        }
                    });

        }
    }

    private void setActionPlanList(ArrayList<ActionPlanModel> list) {
        actionPlanModels = new ArrayList<>();
        actionPlanModels.addAll(list);
        actionPlanAdapter = new ActionPlanAdapter(context, actionPlanModels);
        actionPlanRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        actionPlanRecyclerView.setAdapter(actionPlanAdapter);

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

      //  FilterRequest filterRequest = new FilterRequest(brandUrl, AppPrefs.getAccessToken(context), stringListener, errorListener);
       // VolleyNetworkRequest.getInstance(context).addToRequestQueue(filterRequest);

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

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseAuth.getInstance().getCurrentUser().getIdToken(true)
                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            if (task.isSuccessful()) {
                                FilterRequest filterRequest = new FilterRequest(campaignUrl, AppPrefs.getAccessToken(context),task.getResult().getToken(), stringListener, errorListener);
                                VolleyNetworkRequest.getInstance(context).addToRequestQueue(filterRequest);

                            }
                        }
                    });
        }

      //  FilterRequest filterRequest = new FilterRequest(campaignUrl, AppPrefs.getAccessToken(context), stringListener, errorListener);
       // VolleyNetworkRequest.getInstance(context).addToRequestQueue(filterRequest);
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
                            //setLocationFilter(locationModel);
                            setCountryFilter(locationModel);
                            //setCityFilter(locationModel);
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
        /*FilterRequest filterRequest = new FilterRequest(locationUrl,
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
        //cityList.addAll(locationModel.getCities());
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
        filterLocationInfo.setLocation_name("Select Location");
        locationList.add(filterLocationInfo);
        //locationList.addAll(locationModel.getLocations());
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
        setTitle("Action Plan");
        enableBack(true);
        enableBackPressed();
    }
}
