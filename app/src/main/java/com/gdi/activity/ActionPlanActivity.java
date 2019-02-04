package com.gdi.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gdi.R;
import com.gdi.adapter.ActionPlanAdapter;
import com.gdi.api.ActionPlanRequest;
import com.gdi.api.ApiEndPoints;
import com.gdi.api.FilterRequest;
import com.gdi.api.VolleyNetworkRequest;
import com.gdi.model.actionplan.ActionPlanModel;
import com.gdi.model.actionplan.ActionPlanRootObject;
import com.gdi.model.filter.BrandFilterRootObject;
import com.gdi.model.filter.BrandsInfo;
import com.gdi.model.filter.CampaignFilterRootObject;
import com.gdi.model.filter.CampaignsInfo;
import com.gdi.model.filter.CityInfo;
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
    private ArrayList<CountryInfo> countryList;
    private ArrayList<CityInfo> cityList;
    private ArrayList<FilterLocationInfo> locationList;
    private String brandId = "";
    private String campaignId = "";
    private String countryId = "";
    private String cityId = "";
    private String locationId = "";
    private boolean expand = false;
    private boolean isFirstTime = true;
    private boolean isFirstCompaignLoad = true;
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
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setActionBar();
        actionPlanRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_action_plan);
        actionPlanCard = (CardView) findViewById(R.id.action_plan_card);
        expandLayout = (RelativeLayout) findViewById(R.id.action_plan_expand_layout);
        search = (Button) findViewById(R.id.btn_search);
        brandSearch = (Spinner) findViewById(R.id.spinner_brand);
        auditRoundSearch = (Spinner) findViewById(R.id.spinner_audit_round);
        countrySearch = (Spinner) findViewById(R.id.spinner_country);
        citySearch = (Spinner) findViewById(R.id.spinner_city);
        locationSearch = (Spinner) findViewById(R.id.spinner_location);
        search.setOnClickListener(this);
        expandLayout.setOnClickListener(this);
        getBrandFilter();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_search:
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
                        if (object.getInt(ApiResponseKeys.RES_KEY_CODE) == AppConstant.ERROR){
                            AppUtils.toast((BaseActivity) context,
                                    object.getString(ApiResponseKeys.RES_KEY_MESSAGE));
                            finish();
                            startActivity(new Intent(context, SignInActivity.class));
                        }else {
                            AppUtils.toast((BaseActivity) context,
                                    object.getString(ApiResponseKeys.RES_KEY_MESSAGE));
                            actionPlanRecyclerView.setVisibility(View.GONE);
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
                AppLogger.e(TAG, "Audit Error: " + error.getMessage());

            }
        };
        AppLogger.e(TAG, "Brand Id: " + brandId);
        AppLogger.e(TAG, "Campaign Id: " + campaignId);
        AppLogger.e(TAG, "Country Id: " + countryId);
        AppLogger.e(TAG, "City Id: " + cityId);
        AppLogger.e(TAG, "Location Id: " + locationId);
        String auditUrl = ApiEndPoints.ACTIONPLAN + "?"
                + "brand_id=" + brandId + "&"
                + "campaign_id=" + campaignId + "&"
                + "location_id=" + locationId + "&"
                + "country_id=" + countryId + "&"
                + "city_id=" + cityId;
        ActionPlanRequest auditRequest = new ActionPlanRequest(AppPrefs.getAccessToken(context),
                auditUrl, stringListener, errorListener);
        VolleyNetworkRequest.getInstance(context).addToRequestQueue(auditRequest);
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
                            setCityFilter(locationCampaignRootObject.getData());
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

    private void setCityFilter(ArrayList<FilterLocationInfo> filterLocationInfos) {
        final ArrayList<FilterLocationInfo> cityList = new ArrayList<>();
        FilterLocationInfo cityInfo = new FilterLocationInfo();
        cityInfo.setCity_id(0);
        cityInfo.setCity_name("All");
        cityList.add(cityInfo);
        cityList.addAll(filterLocationInfos);
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
                cityId = "" + cityList.get(position).getCity_id();
                AppPrefs.setFilterCity(context, position);
                AppLogger.e(TAG, "City Id: " + cityId);
                AppLogger.e(TAG, "City Name: " + AppPrefs.getFilterCity(context));
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

    private void setActionBar() {
        initToolbar(toolbar);
        setTitle("Action Plan");
        enableBack(true);
        enableBackPressed();
    }
}
