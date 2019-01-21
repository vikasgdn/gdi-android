package com.gdi.activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.gdi.R;
import com.gdi.adapter.AudioImageAdapter1;
import com.gdi.adapter.IAAudioImageAdapter1;
import com.gdi.api.ApiEndPoints;
import com.gdi.api.AudioImageRequest;
import com.gdi.api.FilterRequest;
import com.gdi.api.IAFilterRequest;
import com.gdi.api.SendToEmailRequest;
import com.gdi.api.VolleyNetworkRequest;
import com.gdi.model.audioimages.AudioImageInfo;
import com.gdi.model.audioimages.AudioImageLocation;
import com.gdi.model.audioimages.AudioImageRootObject;
import com.gdi.model.audioimages.IAAudioImageInfo;
import com.gdi.model.audioimages.IAAudioImageRootObject;
import com.gdi.model.filter.BrandsInfo;
import com.gdi.model.filter.CampaignsInfo;
import com.gdi.model.filter.CityInfo;
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
import com.gdi.utils.DownloadAudioTask;
import com.gdi.utils.DownloadExcelTask;
import com.gdi.utils.DownloadImageTask;
import com.gdi.utils.DownloadPdfTask;
import com.gdi.utils.Validation;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;

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
    private String auditId = "";
    private String month = "";
    private String locationId = "";
    private IAFilterInfo iaFilterInfo;
    private ArrayList<BrandsInfo> brandList;
    private ArrayList<Audit> audits;
    private ArrayList<AuditTypes> auditTypes;
    private ArrayList<LocationsInfo> locationList;
    private int REQUEST_FOR_READ = 1;
    private static final int REQUEST_FOR_WRITE_PDF = 1;
    private static final int REQUEST_FOR_WRITE_EXCEL = 10;
    private static final int REQUEST_FOR_WRITE_IMAGE = 100;
    private static final int REQUEST_FOR_WRITE_AUDIO = 1000;
    private MediaPlayer mPlayer;
    private IAAudioImageAdapter1 audioImageAdapter1;
    ArrayList<IAAudioImageInfo> audioImageInfos;
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
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setActionBar();
        list1 = (RecyclerView) findViewById(R.id.recycler_view_audio_image);
        brandSearch = (Spinner) findViewById(R.id.spinner_brand);
        auditTypeSearch = (Spinner) findViewById(R.id.spinner_audit_type);
        auditMonthSearch = (TextView) findViewById(R.id.tv_audit_month);
        auditNameSearch = (Spinner) findViewById(R.id.spinner_audit_name);
        locationSearch = (Spinner) findViewById(R.id.spinner_location);
        search = (Button)findViewById(R.id.btn_search);

        filterList();//set filter by call filet api
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
                audioImageInfos = new ArrayList<>();
                setData();
                audioImageAdapter1 = new IAAudioImageAdapter1(context, audioImageInfos, audioDownloadFinishedListner);
                list1.setLayoutManager(new LinearLayoutManager(context));
                list1.setAdapter(audioImageAdapter1);
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

            }
        };
        IAFilterRequest iaFilterRequest = new IAFilterRequest(AppPrefs.getAccessToken(context),
                stringListener, errorListener);
        VolleyNetworkRequest.getInstance(context).addToRequestQueue(iaFilterRequest);
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
                        if (object.getInt(ApiResponseKeys.RES_KEY_CODE) == AppConstant.ERROR){
                            AppUtils.toast((BaseActivity) context,
                                    object.getString(ApiResponseKeys.RES_KEY_MESSAGE));
                            finish();
                            startActivity(new Intent(context, SignInActivity.class));
                        }else {
                            AppUtils.toast((BaseActivity) context,
                                    object.getString(ApiResponseKeys.RES_KEY_MESSAGE));
                            //dashboardLayout.setVisibility(View.GONE);
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
                AppLogger.e(TAG, "AudioImageError: " + error.getMessage());

            }
        };
        AppLogger.e(TAG, "Brand Id: " + brandId);
        AppLogger.e(TAG, "AyditType Id: " + auditTypeId);
        AppLogger.e(TAG, "Audit Id: " + auditId);
        AppLogger.e(TAG, "Month: " + month);
        AppLogger.e(TAG, "Location Id: " + locationId);
        String audioImageUrl = ApiEndPoints.IAAUDIOIMAGE + "?"
                + "audit_type=" + auditTypeId + "&"
                + "brand_id=" + brandId + "&"
                + "location_id=" + locationId + "&"
                + "audit_id=" + auditId + "&"
                + "audit_month=" + "2019-01";
        AudioImageRequest audioImageRequest = new AudioImageRequest(AppPrefs.getAccessToken(context),
                audioImageUrl, stringListener, errorListener);
        VolleyNetworkRequest.getInstance(context).addToRequestQueue(audioImageRequest);
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

    private void playAudio(){
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Disable the play button
                search.setEnabled(false);

                // The audio url to play
                String audioUrl = "http://www.all-birds.com/Sound/western%20bluebird.wav";

                // Initialize a new media player instance
                mPlayer = new MediaPlayer();

                // Set the media player audio stream type
                mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                //Try to play music/audio from url
                try{
                    /*
                        void setDataSource (String path)
                            Sets the data source (file-path or http/rtsp URL) to use.

                        Parameters
                            path String : the path of the file, or the http/rtsp URL of the stream you want to play

                        Throws
                            IllegalStateException : if it is called in an invalid state

                                When path refers to a local file, the file may actually be opened by a
                                process other than the calling application. This implies that the
                                pathname should be an absolute path (as any other process runs with
                                unspecified current working directory), and that the pathname should
                                reference a world-readable file. As an alternative, the application
                                could first open the file for reading, and then use the file
                                descriptor form setDataSource(FileDescriptor).

                            IOException
                            IllegalArgumentException
                            SecurityException
                    */
                    // Set the audio data source
                    mPlayer.setDataSource(audioUrl);

                    /*
                        void prepare ()
                            Prepares the player for playback, synchronously. After setting the
                            datasource and the display surface, you need to either call prepare()
                            or prepareAsync(). For files, it is OK to call prepare(), which blocks
                            until MediaPlayer is ready for playback.

                        Throws
                            IllegalStateException : if it is called in an invalid state
                            IOException
                    */
                    // Prepare the media player
                    mPlayer.prepare();

                    // Start playing audio from http url
                    mPlayer.start();

                    // Inform user for audio streaming
                    Toast.makeText(context,"Playing",Toast.LENGTH_SHORT).show();
                }catch (IOException e){
                    // Catch the exception
                    e.printStackTrace();
                }catch (IllegalArgumentException e){
                    e.printStackTrace();
                }catch (SecurityException e){
                    e.printStackTrace();
                }catch (IllegalStateException e){
                    e.printStackTrace();
                }

                mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        Toast.makeText(context,"End",Toast.LENGTH_SHORT).show();
                        search.setEnabled(true);
                        /** MediaPlayer onCompletion event handler. Method which calls then song playing is complete*/
                        //buttonPlayPause.setImageResource(R.drawable.button_play);
                    }
                });

                mPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                    @Override
                    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
                        /** Method which updates the SeekBar secondary progress by current song loading from URL position*/
                        //seekBarProgress.setSecondaryProgress(percent);
                    }
                });
            }
        });
    }

    @Override
    public void onImageDownloadFinished(String file) {

    }

    @Override
    public void onAudioDownloadFinished(String file) {

    }
}
