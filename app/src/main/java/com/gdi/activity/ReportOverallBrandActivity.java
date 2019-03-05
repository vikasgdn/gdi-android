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
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gdi.R;
import com.gdi.adapter.OverallAdapter;
import com.gdi.api.ApiEndPoints;
import com.gdi.api.FilterRequest;
import com.gdi.api.SendToEmailRequest;
import com.gdi.api.VolleyNetworkRequest;
import com.gdi.fragment.DepartmentalFragment;
import com.gdi.fragment.OverallFragment;
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
import com.gdi.model.overallbrand.OverallBrandInfo;
import com.gdi.utils.ApiResponseKeys;
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
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReportOverallBrandActivity extends BaseActivity implements
        DownloadPdfTask.PDFDownloadFinishedListner, DownloadExcelTask.DownloadExcelFinishedListner {

    @BindView(R.id.detail_summary_recycler1)
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
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.overallFrame)
    FrameLayout loginFrame;
    @BindView(R.id.overall_txt)
    public TextView overallTab;
    @BindView(R.id.departmental_txt)
    public TextView departmentalTab;
    private FilterInfo filterInfo;
    private ArrayList<BrandsInfo> brandList;
    private ArrayList<CampaignsInfo> campaignList;
    private ArrayList<FilterCountryInfo> countryList;
    private ArrayList<FilterCityInfo> cityList;
    private ArrayList<FilterLocationInfo> locationList;
    public String brandId = "";
    public String campaignId = "";
    public String countryId = "";
    public String cityId = "";
    public String locationId = "";
    private OverallBrandInfo overallBrandInfo;
    private OverallAdapter overallAdapter;
    private boolean isOverall = false;
    private static int REQUEST_FOR_WRITE_PDF = 1;
    private static int REQUEST_FOR_WRITE_EXCEL = 100;
    Context context;
    private boolean isFirstTime = true;
    private boolean isFirstCompaignLoad = true;
    private boolean isFirstCountryLoad = true;
    private boolean isFirstCityLoad = true;
    private static final String TAG = ReportOverallBrandActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overall_brand);
        context = this;
        ButterKnife.bind(ReportOverallBrandActivity.this);
        initView();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setActionBar();
        //isStarted = true;
        getBrandFilter();
        brandSearch = (Spinner) findViewById(R.id.spinner_brand);
        auditRoundSearch = (Spinner) findViewById(R.id.spinner_audit_round);
        countrySearch = (Spinner) findViewById(R.id.spinner_country);
        citySearch = (Spinner) findViewById(R.id.spinner_city);
        locationSearch = (Spinner) findViewById(R.id.spinner_location);
        search = (Button)findViewById(R.id.btn_search);
        overallTab = (TextView)findViewById(R.id.overall_txt);
        departmentalTab = (TextView)findViewById(R.id.departmental_txt);
        overallTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppPrefs.setFilterBrand(context, brandSearch.getSelectedItemPosition());
                AppPrefs.setFilterCampaign(context, auditRoundSearch.getSelectedItemPosition());
                AppPrefs.setFilterCity(context, citySearch.getSelectedItemPosition());
                AppPrefs.setFilterCountry(context, countrySearch.getSelectedItemPosition());
                AppPrefs.setFilterLocation(context, locationSearch.getSelectedItemPosition());
                setOnclickOverall();
            }
        });
        departmentalTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppPrefs.setFilterBrand(context, brandSearch.getSelectedItemPosition());
                AppPrefs.setFilterCampaign(context, auditRoundSearch.getSelectedItemPosition());
                AppPrefs.setFilterCity(context, citySearch.getSelectedItemPosition());
                AppPrefs.setFilterCountry(context, countrySearch.getSelectedItemPosition());
                AppPrefs.setFilterLocation(context, locationSearch.getSelectedItemPosition());
                setOnclickDeparmental();
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppPrefs.setFilterBrand(context, brandSearch.getSelectedItemPosition());
                AppPrefs.setFilterCampaign(context, auditRoundSearch.getSelectedItemPosition());
                AppPrefs.setFilterCity(context, citySearch.getSelectedItemPosition());
                AppPrefs.setFilterCountry(context, countrySearch.getSelectedItemPosition());
                AppPrefs.setFilterLocation(context, locationSearch.getSelectedItemPosition());
                v.playSoundEffect(android.view.SoundEffectConstants.CLICK);
                setOnclickOverall();
            }
        });
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
        filterLocationInfo.setLocation_name("All");
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

    public void emailAttachment(final String apiUrl) {

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
                    } else if (!object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
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
            ActivityCompat.requestPermissions(ReportOverallBrandActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_FOR_WRITE_PDF);

        } else {

            DownloadPdfTask downloadTask = new DownloadPdfTask(context, url, ReportOverallBrandActivity.this);
        }
    }

    public void downloadExcel(final String url) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(ReportOverallBrandActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_FOR_WRITE_EXCEL);
        } else {
            DownloadExcelTask downloadTask = new DownloadExcelTask(context, url, ReportOverallBrandActivity.this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_FOR_WRITE_PDF) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                /*String fileUrl = dashBoardInfo.getReport_urls().getPdf();
                downloadPdf(fileUrl);*/
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

    private void setActionBar() {
        initToolbar(toolbar);
        setTitle("Overall Brand");
        enableBack(true);
        enableBackPressed();
    }

    private void setOnclickOverall(){
        departmentalTab.setBackground(getResources()
                .getDrawable(R.drawable.departmental_border_layout2));
        overallTab.setBackground(getResources().getDrawable(R.drawable.overall_border_layout));
        overallTab.setTextColor(getResources().getColor(R.color.colorWhite));
        departmentalTab.setTextColor(getResources().getColor(R.color.extremeDarkGrey));
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.overallFrame, new OverallFragment());
        fragmentTransaction.commit();
    }

    private void setOnclickDeparmental(){
        overallTab.setBackground(getResources().getDrawable(R.drawable.overall_border_layout2));
        departmentalTab.setBackground(getResources().getDrawable(R.drawable.departmental_border_layout));
        overallTab.setTextColor(getResources().getColor(R.color.extremeDarkGrey));
        departmentalTab.setTextColor(getResources().getColor(R.color.colorWhite));
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.overallFrame, new DepartmentalFragment());
        fragmentTransaction.commit();
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
