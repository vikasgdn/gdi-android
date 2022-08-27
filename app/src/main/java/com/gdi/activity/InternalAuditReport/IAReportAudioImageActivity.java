package com.gdi.activity.InternalAuditReport;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.gdi.activity.BaseActivity;
import com.gdi.adapter.IAAudioImageAdapter1;
import com.gdi.api.ApiEndPoints;
import com.gdi.api.FilterRequest;
import com.gdi.api.GetReportRequest;
import com.gdi.api.SendToEmailRequest;
import com.gdi.api.VolleyNetworkRequest;
import com.gdi.hotel.mystery.audits.R;
import com.gdi.model.filter.BrandFilterRootObject;
import com.gdi.model.filter.FilterLocationModel;
import com.gdi.model.filter.LocationFilterRootObject;
import com.gdi.model.iafilter.AuditNameRootObject;
import com.gdi.model.reportaudioimages.AudioImageLocation;
import com.gdi.model.reportaudioimages.IAAudioImageInfo;
import com.gdi.model.reportaudioimages.IAAudioImageRootObject;
import com.gdi.model.filter.BrandsInfo;
import com.gdi.model.filter.FilterLocationInfo;
import com.gdi.model.iafilter.Audit;
import com.gdi.model.iafilter.AuditName;
import com.gdi.model.iafilter.IAFilterInfo;
import com.gdi.utils.ApiResponseKeys;
import com.gdi.utils.AppLogger;
import com.gdi.utils.AppPrefs;
import com.gdi.utils.AppUtils;
import com.gdi.utils.CustomDialog;
import com.gdi.utils.DownloadAudioTask;
import com.gdi.utils.DownloadExcelTask;
import com.gdi.utils.DownloadImageTask;
import com.gdi.utils.DownloadPdfTask;
import com.gdi.utils.Validation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IAReportAudioImageActivity extends BaseActivity implements
        DownloadPdfTask.PDFDownloadFinishedListner,
        DownloadExcelTask.DownloadExcelFinishedListner,
        DownloadImageTask.ImageDownloadFinishedListner,
        DownloadAudioTask.AudioDownloadFinishedListner, View.OnClickListener {

    @BindView(R.id.recycler_view_audio_image)
    RecyclerView list1;
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
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    Context context;
    private String brandId = "";
    private String auditTypeId = "";
    private String auditNameId = "";
    private String auditMonth = "";
    private String locationId = "";
    private IAFilterInfo iaFilterInfo;
    private ArrayList<BrandsInfo> brandList;
    private ArrayList<Audit> audits;
    private ArrayList<AuditName> auditTypes;
    private ArrayList<FilterLocationInfo> locationList;
    private int REQUEST_FOR_READ = 1;
    private static final int REQUEST_FOR_WRITE_PDF = 1;
    private static final int REQUEST_FOR_WRITE_EXCEL = 10;
    private static final int REQUEST_FOR_WRITE_IMAGE = 100;
    private static final int REQUEST_FOR_WRITE_AUDIO = 1000;
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private double startTime = 0.0;
    private double finalTime = 0.0;
    private CustomDialog customDialog;
    public static int oneTimeOnly = 0;
    public Handler myHandler = new Handler();
    private ProgressDialog progressDialog;
    private IAAudioImageAdapter1 audioImageAdapter1;
    ArrayList<IAAudioImageInfo> audioImageInfos;
    private boolean isFirstTime = true;
    private boolean isFirstTimeLocation = true;
    private boolean isFirstTimeAuditType = true;
    private boolean isFirstTimeBrand = true;
    ArrayList<String> arrayList = new ArrayList<>();
    private static final String TAG = IAReportAudioImageActivity.class.getSimpleName();
    private DownloadAudioTask.AudioDownloadFinishedListner audioDownloadFinishedListner;

    @Override
    protected void onResume() {
        super.onResume();
        AppUtils.hideKeyboard(IAReportAudioImageActivity.this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ia_report_audio_image);
        context = this;
        ButterKnife.bind(IAReportAudioImageActivity.this);
        initView();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        setActionBar();
        list1 = findViewById(R.id.recycler_view_audio_image);
        brandSearch = findViewById(R.id.spinner_brand);
        auditTypeSearch = findViewById(R.id.spinner_audit_type);
        auditMonthSearch = findViewById(R.id.tv_audit_month);
        auditNameSearch = findViewById(R.id.spinner_audit_name);
        locationSearch = findViewById(R.id.spinner_location);
        search = findViewById(R.id.btn_search);
        /*if (!AppUtils.isStringEmpty(AppPrefs.getIaFilterMonth(context))){
            auditMonthSearch.setText(AppPrefs.getIaFilterMonth(context));
        }*/
        /*setAuditTypeFilter();
        setBrandFilter(new ArrayList<BrandsInfo>());
        setLocationFilter(new FilterLocationModel());
        setAuditNameFilter(new ArrayList<AuditName>());*/

        arrayList.add("Select");

        brandId = "" + AppPrefs.getIaFilterBrand(context);
        auditTypeId = "" + AppPrefs.getIaFilterAuditType(context);
        auditNameId = "" + AppPrefs.getIaFilterAuditName(context);
        auditMonth = "" + AppPrefs.getIaFilterMonth(context);
        locationId = "" + AppPrefs.getIaFilterLocation(context);

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
        search.setOnClickListener(this);
        auditMonthSearch.setOnClickListener(this);

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

     /*   FilterRequest filterRequest = new FilterRequest(brandUrl,
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

     /*   FilterRequest filterRequest = new FilterRequest(locationUrl,
                AppPrefs.getAccessToken(context), stringListener, errorListener);
        VolleyNetworkRequest.getInstance(context).addToRequestQueue(filterRequest);*/
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


     /*   FilterRequest filterRequest = new FilterRequest(campaignUrl,
                AppPrefs.getAccessToken(context), stringListener, errorListener);
        VolleyNetworkRequest.getInstance(context).addToRequestQueue(filterRequest);
*/    }

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
                audioImageInfos = new ArrayList<>();
                setData();
                audioImageAdapter1 = new IAAudioImageAdapter1(context, audioImageInfos, audioDownloadFinishedListner);
                list1.setLayoutManager(new LinearLayoutManager(context));
                list1.setAdapter(audioImageAdapter1);
                break;
        }
    }

    private void setData(){
        showProgressDialog();
        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AppLogger.e(TAG, "AudioImageResponse: " + response);
                try {
                    JSONObject object = new JSONObject(response);

                    if (!object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        IAAudioImageRootObject audioImageRootObject = new GsonBuilder().create()
                                .fromJson(object.toString(), IAAudioImageRootObject.class);
                        if (audioImageRootObject.getData() != null &&
                                audioImageRootObject.getData().toString().length() > 0) {
                            AudioImageLocation audioImageLocation = new AudioImageLocation();
                            audioImageLocation = audioImageRootObject.getData();
                            audioImageInfos.addAll(audioImageLocation.getLocations());
                            audioImageAdapter1.notifyDataSetChanged();
                            //dashboardLayout.setVisibility(View.VISIBLE);
                        }
                    } else if (object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        /*if (object.getInt(ApiResponseKeys.RES_KEY_CODE) == AppConstant.ERROR){
                            AppUtils.toast((BaseActivity) context,
                                    object.getString(ApiResponseKeys.RES_KEY_MESSAGE));
                            finish();
                            startActivity(new Intent(context, SignInActivity.class));
                        }else {
                            AppUtils.toast((BaseActivity) context,
                                    object.getString(ApiResponseKeys.RES_KEY_MESSAGE));
                            //dashboardLayout.setVisibility(View.GONE);
                        }*/
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

            }
        };
        AppLogger.e(TAG, "Brand Id: " + brandId);
        AppLogger.e(TAG, "AyditType Id: " + auditTypeId);
        AppLogger.e(TAG, "Audit Id: " + auditNameId);
        AppLogger.e(TAG, "Month: " + auditMonth);
        AppLogger.e(TAG, "Location Id: " + locationId);
        String audioImageUrl = ApiEndPoints.IAAUDIOIMAGE + "?"
                + "audit_type=" + auditTypeId + "&"
                + "brand_id=" + brandId + "&"
                + "location_id=" + locationId + "&"
                + "audit_id[]=" + auditNameId + "&"
                + "audit_month=" + auditMonth;
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseAuth.getInstance().getCurrentUser().getIdToken(true)
                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            if (task.isSuccessful()) {
                                GetReportRequest getReportRequest = new GetReportRequest(AppPrefs.getAccessToken(context),task.getResult().getToken(), audioImageUrl, stringListener, errorListener);
                                VolleyNetworkRequest.getInstance(context).addToRequestQueue(getReportRequest);
                            }
                        }
                    });
        }

    }

    /*private void getBrandFilter(String auditMonth) {
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
        String brandUrl = ApiEndPoints.FILTERBRAND + "?"
                + "audit_type_id=" + auditTypeId + "&"
                + "audit_month=" + auditMonth;

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
                            setAuditNameFilter(auditNameRootObject.getData());
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

    private void setAuditTypeFilter() {
        ArrayList<String> auditTypes = new ArrayList<>();
        auditTypes.add("Select");
        auditTypes.add("Self Assessment");
        auditTypes.add("Heart of the House");
        auditTypes.add("Inspection");

        ArrayAdapter<String> auditTypeAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_dropdown_item);
        for (int i = 0; i < auditTypes.size(); i++) {
            auditTypeAdapter.add(auditTypes.get(i));
        }
        auditTypeSearch.setAdapter(auditTypeAdapter);
        auditTypeSearch.setSelection(AppPrefs.getIaFilterAuditType(context));
        auditTypeSearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (isFirstTimeAuditType) {
                    isFirstTimeAuditType = false;

                    if (AppPrefs.getIaFilterAuditType(context) > 0) {
                        auditTypeId = "" + AppPrefs.getIaFilterAuditType(context);
                        auditTypeSearch.setSelection(AppPrefs.getIaFilterAuditType(context));
                    } else {

                    }
                } else {
                    if (position > 0) {
                        AppPrefs.setIaFilterAuditType(context, position);
                        AppPrefs.setIaFilterLocation(context, 0);
                        AppPrefs.setIaFilterMonth(context,"");
                        AppPrefs.setIaFilterBrand(context,0);
                        AppPrefs.setIaFilterAuditName(context,0);
                        auditTypeId = "" + position;
                        AppLogger.e(TAG, "AuditType Id: " + auditTypeId);
                        AppLogger.e(TAG, "AuditType Position: " + AppPrefs.getIaFilterAuditType(context));
                        auditMonthSearch.setText("Select");
                        brandSearch.setSelection(0);
                        locationSearch.setSelection(0);
                        auditNameSearch.setSelection(0);
                        setBrandFilter(new ArrayList<BrandsInfo>());
                        setLocationFilter(new FilterLocationModel());
                        setAuditNameFilter(new ArrayList<AuditName>());
                    } else {
                        auditMonthSearch.setText("Select");
                        brandSearch.setSelection(0);
                        locationSearch.setSelection(0);
                        auditNameSearch.setSelection(0);
                        setBrandFilter(new ArrayList<BrandsInfo>());
                        setLocationFilter(new FilterLocationModel());
                        setAuditNameFilter(new ArrayList<AuditName>());
                        brandId = "";
                        locationId = "";
                        auditNameId = "";
                        auditMonth = "";

                    }
                }






                *//*auditTypeId = "" + position;
                AppConstant.IA_FILTER_AUDIT_TYPE = position;
                setAuditMonth();
                AppLogger.e(TAG, "Campaign Id: " + auditTypeId);
                AppLogger.e(TAG, "Campaign position: " + AppConstant.IA_FILTER_AUDIT_TYPE);*//*
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
                        getBrandFilter(auditMonth);
                        AppPrefs.setIaFilterMonth(context, auditMonth);
                        //AppConstant.IA_FILTER_MONTH = auditMonth;
                        auditMonthSearch.setText(AppUtils.setAuditMonth(cal.getTime()));
                    }
                }, auditMonthCal.get(Calendar.YEAR), auditMonthCal.get(Calendar.MONTH),
                auditMonthCal.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void setBrandFilter(ArrayList<BrandsInfo> brandsInfos) {
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
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (isFirstTime) {
                    isFirstTime = false;
                    if (AppPrefs.getIaFilterBrand(context) > 0) {
                        brandId = "" + brandList.get(position).getBrand_id();
                        getLocationFilter();
                    } else {
                        locationSearch.setSelection(0);
                        auditNameSearch.setSelection(0);
                    }
                } else {
                    if (position > 0) {
                        locationSearch.setSelection(0);
                        auditNameSearch.setSelection(0);
                        AppPrefs.setIaFilterBrand(context, position);
                        AppPrefs.setIaFilterLocation(context, 0);
                        AppPrefs.setIaFilterAuditName(context, 0);
                        brandId = "" + brandList.get(position).getBrand_id();
                        getLocationFilter();
                        AppLogger.e(TAG, "Brand Id: " + brandId);
                        AppLogger.e(TAG, "Brand Position: " + AppPrefs.getIaFilterBrand(context));
                        setLocationFilter(new FilterLocationModel());
                        setAuditNameFilter(new ArrayList<AuditName>());
                    } else {
                        locationSearch.setSelection(0);
                        auditNameSearch.setSelection(0);
                        auditNameId = "";
                        locationId = "";
                        brandId = "";
                        setLocationFilter(new FilterLocationModel());
                        setAuditNameFilter(new ArrayList<AuditName>());

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
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isFirstTimeLocation) {
                    isFirstTimeLocation = false;
                    if (AppPrefs.getIaFilterLocation(context) > 0) {
                        locationId = "" + locationList.get(position).getLocation_id();
                        getAuditNameFilter();
                    } else {
                        auditNameSearch.setSelection(0);
                    }
                } else {
                    if (position > 0) {
                        auditNameSearch.setSelection(0);
                        AppPrefs.setIaFilterLocation(context, position);
                        AppPrefs.setIaFilterAuditName(context, 0);
                        locationId = "" + locationList.get(position).getLocation_id();
                        getAuditNameFilter();
                        AppLogger.e(TAG, "Location Id: " + locationId);
                        AppLogger.e(TAG, "Location Position: " + AppPrefs.getIaFilterLocation(context));
                        setAuditNameFilter(new ArrayList<AuditName>());

                    } else {
                        auditNameSearch.setSelection(0);
                        locationId = "";
                        auditNameId = "";
                        setAuditNameFilter(new ArrayList<AuditName>());

                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void setAuditNameFilter(ArrayList<AuditName> locationModel) {
        final ArrayList<AuditName> auditNames = new ArrayList<>();
        AuditName filterLocationInfo = new AuditName();
        filterLocationInfo.setAudit_id(0);
        filterLocationInfo.setAudit_name("Select");
        auditNames.add(filterLocationInfo);
        auditNames.addAll(locationModel);

        ArrayAdapter<String> locationAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_dropdown_item);
        for (int i = 0; i < auditNames.size(); i++) {
            locationAdapter.add(auditNames.get(i).getAudit_name());
        }
        auditNameSearch.setAdapter(locationAdapter);
        auditNameSearch.setSelection(AppPrefs.getIaFilterAuditName(context));
        auditNameSearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                auditNameId = "" + auditNames.get(position).getAudit_id();
                AppPrefs.setIaFilterAuditName(context, position);
                AppLogger.e(TAG, "Location Id: " + locationId);
                AppLogger.e(TAG, "Location position: " + AppPrefs.getIaFilterAuditName(context));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }*/

    private void setActionBar() {
        initToolbar(toolbar);
        setTitle("Audio/Image");
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

    public void emailAttachment(final String apiUrl) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        final View view = layoutInflater.inflate(R.layout.send_email_layout, null);
        dialog.setView(view);

        final EditText emailId = view.findViewById(R.id.send_email_edt_txt);

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
     //   SendToEmailRequest sendToEmailRequest = new SendToEmailRequest(url,AppPrefs.getAccessToken(context), stringListener, errorListener);
      //  VolleyNetworkRequest.getInstance(context).addToRequestQueue(sendToEmailRequest);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseAuth.getInstance().getCurrentUser().getIdToken(true)
                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            if (task.isSuccessful()) {
                                SendToEmailRequest sendToEmailRequest = new SendToEmailRequest(url, AppPrefs.getAccessToken(context),task.getResult().getToken(), stringListener, errorListener);
                                VolleyNetworkRequest.getInstance(context).addToRequestQueue(sendToEmailRequest);
                            }
                        }
                    });
        }
    }

    public void downloadPdf(final String url) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(IAReportAudioImageActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_FOR_WRITE_PDF);
        } else {
            DownloadPdfTask downloadTask = new DownloadPdfTask(context, url, IAReportAudioImageActivity.this);
        }
    }

    public void downloadExcel(final String url) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(IAReportAudioImageActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_FOR_WRITE_EXCEL);
        } else {
            DownloadExcelTask downloadTask = new DownloadExcelTask(context, url, IAReportAudioImageActivity.this);
        }
    }

    public void downloadImage(final String url) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(IAReportAudioImageActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_FOR_WRITE_EXCEL);
        } else {
            DownloadImageTask downloadTask = new DownloadImageTask(context, url, IAReportAudioImageActivity.this);
        }
    }

    public void downloadAudio(final String url) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(IAReportAudioImageActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_FOR_WRITE_EXCEL);
        } else {
            DownloadAudioTask downloadTask = new DownloadAudioTask(context, url, IAReportAudioImageActivity.this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_FOR_WRITE_PDF) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
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
                //downloadExcel(hotelOverallInfo.getReport_urls().getExcel());
            } else {
                // permission denied, boo! Disable the
                // functionality that depends on this permission.
            }
        }

        if (requestCode == REQUEST_FOR_WRITE_IMAGE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                /*String fileUrl = dashBoardInfo.getReport_urls().getPdf();
                downloadPdf(fileUrl);*/
                //downloadExcel(hotelOverallInfo.getReport_urls().getExcel());
            } else {
                // permission denied, boo! Disable the
                // functionality that depends on this permission.
            }
        }

        if (requestCode == REQUEST_FOR_WRITE_AUDIO) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                /*String fileUrl = dashBoardInfo.getReport_urls().getPdf();
                downloadPdf(fileUrl);*/
                //downloadExcel(hotelOverallInfo.getReport_urls().getExcel());
            } else {
                // permission denied, boo! Disable the
                // functionality that depends on this permission.
            }
        }

    }

    @Override
    public void onPDFDownloadFinished(String path) {
        if (TextUtils.isEmpty(path))
            AppUtils.toast(this,getString(R.string.oops));
        else {
            File file = new File(path);
            Uri excelPath;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                excelPath = FileProvider.getUriForFile(this, "com.gdi.android.fileprovider", file);
            else
                excelPath = Uri.fromFile(file);
            //  Uri data = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID +".provider",file);
            Log.e("PDF file===> ", "" + excelPath.toString());
            Intent target = new Intent(Intent.ACTION_VIEW);
            target.setDataAndType(excelPath, "application/pdf");
            target.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            target.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            target.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

            Intent intent = Intent.createChooser(target, "Open File");
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
                AppUtils.toast(this, e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onExcelDownloadFinished(String path) {
        if (TextUtils.isEmpty(path))
            AppUtils.toast(this, getString(R.string.oops));
        else {
            File file = new File(path);
            Uri excelPath;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                excelPath = FileProvider.getUriForFile(this, "com.gdi.android.fileprovider", file);
            else
                excelPath = Uri.fromFile(file);
            //  Uri data = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID +".provider",file);
            Log.e("PDF file===> ", "" + excelPath.toString());
            Intent target = new Intent(Intent.ACTION_VIEW);
            target.setDataAndType(excelPath, "application/vnd.ms-excel");
            target.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            target.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            target.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

            Intent intent = Intent.createChooser(target, "Open File");
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
                AppUtils.toast(this, e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onImageDownloadFinished(String file) {

    }

    @Override
    public void onAudioDownloadFinished(String file) {

    }

    public void playAudio(String audioUrl) throws IOException {
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        Uri uri = Uri.parse(audioUrl);
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("access-token", AppPrefs.getAccessToken(context));
        mediaPlayer.reset();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setDataSource(context, uri, headers);
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                progressDialog.dismiss();
                mediaPlayer.start();
                openSeekBarDialog();
            }
        });
        mediaPlayer.prepare();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mediaPlayer != null) {
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public void openSeekBarDialog(){
        customDialog = new CustomDialog(context, R.layout.play_audio_layout);
        customDialog.setCancelable(false);
        SeekBar seekbar = customDialog.findViewById(R.id.seekBar);
        TextView seekBarTime = customDialog.findViewById(R.id.seekBar_time);
        ImageView close = customDialog.findViewById(R.id.close_btn);

        finalTime = mediaPlayer.getDuration();
        startTime = mediaPlayer.getCurrentPosition();
        if (oneTimeOnly == 0) {
            seekbar.setMax((int) finalTime);
            oneTimeOnly = 1;
        }

        seekBarTime.setText(String.format("%d.%d",
                TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                finalTime)))
        );
        seekbar.setProgress((int)startTime);
        myHandler.postDelayed(runnableMethod(seekbar, seekBarTime),100);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                customDialog.dismiss();
                startTime = 0.0;
                finalTime = 0.0;
            }
        });
        customDialog.show();

    }

    public Runnable runnableMethod(final SeekBar seekBar, final TextView seekBarTime){
        Runnable UpdateSongTime = new Runnable() {
            public void run() {
                startTime = mediaPlayer.getCurrentPosition();
                seekBarTime.setText(String.format("%d.%d",
                        TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                        toMinutes((long) startTime)))
                );
                seekBar.setProgress((int)startTime);
                myHandler.postDelayed(this, 100);
            }
        };
        return UpdateSongTime;
    }
}
