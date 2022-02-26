package com.gdi.fragment.Mystery;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.widget.Toolbar;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gdi.R;
import com.gdi.activity.Audit.AssignmentActivity;
import com.gdi.activity.BaseActivity;
import com.gdi.activity.SignInActivity;
import com.gdi.api.ApiEndPoints;
import com.gdi.api.FilterRequest;
import com.gdi.api.VolleyNetworkRequest;
import com.gdi.model.filter.BrandFilterRootObject;
import com.gdi.model.filter.BrandsInfo;
import com.gdi.model.filter.FilterLocationInfo;
import com.gdi.model.filter.FilterLocationModel;
import com.gdi.model.filter.LocationFilterRootObject;
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

public class MisteryAuditFilterActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.spinner_brand)
    Spinner brandSearch;
    @BindView(R.id.spinner_location)
    Spinner locationSearch;
    @BindView(R.id.btn_search)
    Button search;
    Context context;
    private String brandId = "";
    private String locationId = "";
    private String typeId = "";
    private String type = "";
    private static final String TAG = MisteryAuditFilterActivity.class.getSimpleName();

    @Override
    protected void onResume() {
        super.onResume();
        AppUtils.hideKeyboard(MisteryAuditFilterActivity.this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audit);
        context = this;
        ButterKnife.bind(MisteryAuditFilterActivity.this);
        initView();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        setActionBar();
        typeId = getIntent().getStringExtra("type_id");
        type = getIntent().getStringExtra("type");
        search = findViewById(R.id.btn_search);
        brandSearch = findViewById(R.id.spinner_brand);
        locationSearch = findViewById(R.id.spinner_location);
        search.setOnClickListener(this);
        getBrandFilter();


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_search:
                Intent intent = new Intent(context, AssignmentActivityForMistery.class);
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
                        AppUtils.toast((BaseActivity) context, object.getString(ApiResponseKeys.RES_KEY_MESSAGE));
                        finish();
                        AppPrefs.clear(context);
                        startActivity(new Intent(context, SignInActivity.class));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }catch (Exception e){
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
                }catch (Exception e){
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


}
