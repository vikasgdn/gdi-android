package com.gdi.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
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
    private ArrayList<LocationsInfo> locationList;
    private String brandId = "";
    private String campaignId = "";
    private String countryId = "";
    private String cityId = "";
    private String locationId = "";
    private boolean expand = false;
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
        filterList();
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
        setTitle("Action Plan");
        enableBack(true);
        enableBackPressed();
    }
}
