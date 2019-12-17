package com.gdi.activity.MysteryAuditReport;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.bumptech.glide.Glide;
import com.gdi.R;
import com.gdi.activity.AudioStreamingActivity;
import com.gdi.activity.BaseActivity;
import com.gdi.activity.ImageViewActivity;
import com.gdi.activity.PlayAudioActivity;
import com.gdi.activity.SignInActivity;
import com.gdi.adapter.AudioImageAdapter1;
import com.gdi.adapter.AudioImageAdapter3;
import com.gdi.api.ApiEndPoints;
import com.gdi.api.FilterRequest;
import com.gdi.api.GetReportRequest;
import com.gdi.api.SendToEmailRequest;
import com.gdi.api.VolleyNetworkRequest;
import com.gdi.model.reportaudioimages.AttachmentAudioImages;
import com.gdi.model.reportaudioimages.AudioImageInfo;
import com.gdi.model.reportaudioimages.AudioImageRootObject;
import com.gdi.model.reportaudioimages.SectionAudioImage;
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
import com.gdi.utils.CustomDialog;
import com.gdi.utils.DownloadAudioTask;
import com.gdi.utils.DownloadExcelTask;
import com.gdi.utils.DownloadPdfTask;
import com.gdi.utils.Headers;
import com.gdi.utils.Validation;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReportAudioImageActivity1 extends BaseActivity implements
        DownloadPdfTask.PDFDownloadFinishedListner,
        DownloadExcelTask.DownloadExcelFinishedListner, DownloadAudioTask.AudioDownloadFinishedListner {

    @BindView(R.id.recycler_view_audio_image)
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
    /*@BindView(R.id.ll_audio_image_head)
    LinearLayout audioImageHead;*/
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    Context context;
    private String brandId = "";
    private String campaignId = "";
    private String countryId = "";
    private String cityId = "";
    private String locationId = "";
    private FilterInfo filterInfo;
    private ArrayList<BrandsInfo> brandList;
    private ArrayList<CampaignsInfo> campaignList;
    private ArrayList<FilterCountryInfo> countryList;
    private ArrayList<FilterCityInfo> cityList;
    private ArrayList<FilterLocationInfo> locationList;
    private int REQUEST_FOR_READ = 1;
    private static final int REQUEST_FOR_WRITE_PDF = 1;
    private static final int REQUEST_FOR_WRITE_EXCEL = 10;
    private static final int REQUEST_FOR_WRITE_IMAGE = 100;
    private static final int REQUEST_FOR_WRITE_AUDIO = 1000;
    private MediaPlayer mediaPlayer = new MediaPlayer();
    //private AudioImageAdapter1 audioImageAdapter1;
    private double startTime = 0.0;
    private double finalTime = 0.0;
    private CustomDialog customDialog;
    public static int oneTimeOnly = 0;
    public Handler myHandler = new Handler();
    private ProgressDialog progressDialog;
    private boolean isFirstTime = true;
    private boolean isFirstCompaignLoad = true;
    private boolean isFirstCountryLoad = true;
    private boolean isFirstCityLoad = true;
    LayoutInflater inflater;
    private boolean isLoading = false;
    private AudioImageAdapter3 audioImageAdapter3;
    int mPreviousTotal = 0;
    int mOnScreenItems = 0;
    int mTotalItemsInList  = 0;
    int mFirstVisibleItem = 0;
    boolean mFirstTime = true;
    private int listSize = 0 ;
    private ArrayList<AttachmentAudioImages> audioImagesArrayList;
    private ArrayList<AttachmentAudioImages> mainAudioImagesArrayList;
    private static final String TAG = ReportAudioImageActivity1.class.getSimpleName();

    @Override
    protected void onResume() {
        super.onResume();
        AppUtils.hideKeyboard(ReportAudioImageActivity1.this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_audio_image1);
        inflater = getLayoutInflater();
        context = this;
        ButterKnife.bind(ReportAudioImageActivity1.this);
        initView();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        setActionBar();
        search = findViewById(R.id.btn_search);
        list1 = findViewById(R.id.recycler_view_audio_image);
        brandSearch = findViewById(R.id.spinner_brand);
        auditRoundSearch = findViewById(R.id.spinner_audit_round);
        countrySearch = findViewById(R.id.spinner_country);
        citySearch = findViewById(R.id.spinner_city);
        locationSearch = findViewById(R.id.spinner_location);
        //audioImageHead = (LinearLayout) findViewById(R.id.ll_audio_image_head);
        progressDialog = new ProgressDialog(context);

        getBrandFilter();//set filter by call filet api
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppPrefs.setFilterBrand(context, brandSearch.getSelectedItemPosition());
                AppPrefs.setFilterCampaign(context, auditRoundSearch.getSelectedItemPosition());
                AppPrefs.setFilterCity(context, citySearch.getSelectedItemPosition());
                AppPrefs.setFilterCountry(context, countrySearch.getSelectedItemPosition());
                AppPrefs.setFilterLocation(context, locationSearch.getSelectedItemPosition());
                view.playSoundEffect(android.view.SoundEffectConstants.CLICK);
                setData();
            }
        });

    }

    private void setData() {
        showProgressDialog();
        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AppLogger.e(TAG, "AudioImageResponse: " + response);
                try {
                    JSONObject object = new JSONObject(response);

                    if (!object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        AudioImageRootObject audioImageRootObject = new GsonBuilder().create()
                                .fromJson(object.toString(), AudioImageRootObject.class);
                        if (audioImageRootObject.getData() != null &&
                                audioImageRootObject.getData().toString().length() > 0) {
                            /*for(int i=0;i<100;i++){
                                audioImageInfos.addAll(audioImageRootObject.getData());
                            }*/
                            ArrayList<AudioImageInfo> arrayList = new ArrayList<>();
                            arrayList.addAll(audioImageRootObject.getData());
                            addRow(arrayList);
                            //audioImageAdapter1.notifyDataSetChanged();
                            //dashboardLayout.setVisibility(View.VISIBLE);
                        }
                    } else if (object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        AppUtils.toast((BaseActivity) context,
                                object.getString(ApiResponseKeys.RES_KEY_MESSAGE));
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
                AppUtils.toast(ReportAudioImageActivity1.this, "Server temporary unavailable, Please try again");

            }
        };
        AppLogger.e(TAG, "Brand Id: " + brandId);
        AppLogger.e(TAG, "Campaign Id: " + campaignId);
        AppLogger.e(TAG, "Country Id: " + countryId);
        AppLogger.e(TAG, "City Id: " + cityId);
        AppLogger.e(TAG, "Location Id: " + locationId);
        String audioImageUrl = ApiEndPoints.AUDIOIMAGE + "?"
                + "brand_id=" + brandId + "&"
                + "campaign_id=" + campaignId + "&"
                + "location_id=" + locationId + "&"
                + "country_id=" + countryId + "&"
                + "city_id=" + cityId;
        GetReportRequest getReportRequest = new GetReportRequest(AppPrefs.getAccessToken(context),
                audioImageUrl, stringListener, errorListener);
        VolleyNetworkRequest.getInstance(context).addToRequestQueue(getReportRequest);
    }

    private void setAudioImageList(ArrayList<AudioImageInfo> arrayList) {
        ArrayList<AudioImageInfo> audioImageInfos = new ArrayList<>();
        audioImageInfos.addAll(arrayList);
        AudioImageAdapter1 audioImageAdapter1 = new AudioImageAdapter1(context, audioImageInfos);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setAutoMeasureEnabled(false);
        list1.setLayoutManager(linearLayoutManager);
       // list1.setNestedScrollingEnabled(false);
        list1.setHasFixedSize(false);
        list1.setAdapter(audioImageAdapter1);
    }

    private void addRow(final ArrayList<AudioImageInfo> arrayList) {
        LinearLayout audioImageHead = findViewById(R.id.ll_audio_image_head);
        audioImageHead.removeAllViews();
        for (int i = 0; i < arrayList.size(); i++) {
            final AudioImageInfo audioImageInfo = arrayList.get(i);
            View view = inflater.inflate(R.layout.audio_image, null);
            RelativeLayout rlAudioImageExpand = view.findViewById(R.id.rl_audio_image_expand);
            TextView tvAudioImageTitle = view.findViewById(R.id.tv_audio_image_title);
            ImageView pdfIcon = view.findViewById(R.id.pdf_icon);
            ImageView mailIcon = view.findViewById(R.id.mail_icon);
            final LinearLayout subHead = view.findViewById(R.id.ll_audio_image_subHead);
            final ImageView ivExpandIcon = view.findViewById(R.id.iv_expand_icon);

            tvAudioImageTitle.setText(audioImageInfo.getLocation_name() + " | " + audioImageInfo.getCity_name());
            rlAudioImageExpand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(audioImageInfo.isExpand()){
                        subHead.setVisibility(View.GONE);
                        ivExpandIcon.setImageResource(R.drawable.expand_icon);
                        audioImageInfo.setExpand(false);
                    }else {
                        subHead.setVisibility(View.VISIBLE);
                        ivExpandIcon.setImageResource(R.drawable.compress_icon);
                        addSubRow(audioImageInfo.getSections(), subHead);
                        audioImageInfo.setExpand(true);
                    }
                }
            });
            pdfIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((ReportAudioImageActivity)context).downloadPdf(audioImageInfo.getReport_urls().getPdf());
                }
            });

            mailIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((ReportAudioImageActivity)context).emailAttachment(audioImageInfo.getReport_urls().getEmail());
                }
            });

            audioImageHead.addView(view);
        }
    }

    private void addSubRow(final ArrayList<SectionAudioImage> arrayList, LinearLayout subHead) {
        subHead.removeAllViews();
        //ArrayList<AttachmentAudioImages> audioImagesArrayList = new ArrayList<>();
        for (int i = 0; i < arrayList.size(); i++) {
            final SectionAudioImage sectionAudioImage = arrayList.get(i);
            View view = inflater.inflate(R.layout.audio_image2, null);
            TextView tvAudioImageTitle = view.findViewById(R.id.tv_audio_image_title);
            TextView score = view.findViewById(R.id.score_text);
            final RecyclerView recyclerViewAudioImage = view.findViewById(R.id.recycler_view_audio_image);
            ImageView pdfIcon = view.findViewById(R.id.pdf_icon);
            ImageView mailIcon = view.findViewById(R.id.mail_icon);
            //final LinearLayout audioImage = view.findViewById(R.id.audio_image_tab);

           tvAudioImageTitle.setText(sectionAudioImage.getSection_name());
            if (AppUtils.isStringEmpty(sectionAudioImage.getScore())){
                score.setVisibility(View.GONE);
            }else {
                score.setVisibility(View.VISIBLE);
                AppUtils.setScoreColor(sectionAudioImage.getScore(), score, context);
                score.setText("Score: " + sectionAudioImage.getScore());
            }

            audioImagesArrayList = new ArrayList<>();
            audioImagesArrayList.addAll(sectionAudioImage.getAttachments());
            mainAudioImagesArrayList = new ArrayList<>();

            listSize = audioImagesArrayList.size();
            //final int loop = listSize/2;
            audioImageAdapter3 = new AudioImageAdapter3(context, mainAudioImagesArrayList);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(context,2
                    , LinearLayoutManager.VERTICAL,false);
            recyclerViewAudioImage.setLayoutManager(gridLayoutManager);
            recyclerViewAudioImage.setAdapter(audioImageAdapter3);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < listSize; j++) {
                        mainAudioImagesArrayList.add(audioImagesArrayList.get(j));
                        audioImageAdapter3.notifyDataSetChanged();
                    }
                }
            });

            /*new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < listSize; i++) {
                        mainAudioImagesArrayList.add(audioImagesArrayList.get(i));
                        audioImageAdapter3.notifyDataSetChanged();
                    }
                }
            }, 1000);*/



            /*if (mFirstTime){
                mFirstTime = false;
                for (int j = 0 ; j < 1; j++){
                    ArrayList<AttachmentAudioImages> mainAudioImagesArrayList = new ArrayList<>();
                    mainAudioImagesArrayList.add(audioImagesArrayList.get(j));
                    AudioImageAdapter3 audioImageAdapter3 = new AudioImageAdapter3(context, mainAudioImagesArrayList);
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(context,2
                            , LinearLayoutManager.VERTICAL,false);
                    recyclerViewAudioImage.setLayoutManager(gridLayoutManager);
                    recyclerViewAudioImage.setAdapter(audioImageAdapter3);
                }
            }else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < listSize; i++) {
                            for (int j = 0 ; j < i; j++){
                                ArrayList<AttachmentAudioImages> mainAudioImagesArrayList = new ArrayList<>();
                                mainAudioImagesArrayList.add(audioImagesArrayList.get(j));
                                AudioImageAdapter3 audioImageAdapter3 = new AudioImageAdapter3(context, mainAudioImagesArrayList);
                                GridLayoutManager gridLayoutManager = new GridLayoutManager(context,2
                                        , LinearLayoutManager.VERTICAL,false);
                                recyclerViewAudioImage.setLayoutManager(gridLayoutManager);
                                recyclerViewAudioImage.setAdapter(audioImageAdapter3);
                            }
                        }

                    }
                }, 1000);
            }*/

            pdfIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    downloadPdf(sectionAudioImage.getReport_urls().getPdf());
                }
            });

            mailIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    emailAttachment(sectionAudioImage.getReport_urls().getEmail());
                }
            });

            subHead.addView(view);
        }

    }

    private void addAudioImageRow(final ArrayList<AttachmentAudioImages> arrayList, LinearLayout audioImage) {
        for (int i = 0; i < arrayList.size(); i++) {
            final AttachmentAudioImages audioImages = arrayList.get(i);
            View itemView = inflater.inflate(R.layout.audio_image_layout3, null);
            TextView tvImageAudioDescription = itemView.findViewById(R.id.tv_image_audio_description);
            RelativeLayout audioPlayLayout = itemView.findViewById(R.id.audio_play_layout);
            RelativeLayout imageLayout = itemView.findViewById(R.id.image_layout);
            ImageView ivImage = itemView.findViewById(R.id.iv_image);
            ImageView ivAudioPlayBtn = itemView.findViewById(R.id.iv_audio_play_btn);

            String fileType = audioImages.getFile_type();
            if (fileType.contains("image/")){
                imageLayout.setVisibility(View.VISIBLE);
                audioPlayLayout.setVisibility(View.GONE);
                if (!AppUtils.isStringEmpty(audioImages.getThumb_url())) {
                    Glide.with(context)
                            .load(Headers.getUrlWithHeaders(audioImages.getThumb_url(),
                                    AppPrefs.getAccessToken(context)))
                            .into(ivImage);
                }
                if (!AppUtils.isStringEmpty(audioImages.getFile_url())){
                    ivImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(context, ImageViewActivity.class);
                            intent.putExtra("fileUrl", audioImages.getFile_url());
                            context.startActivity(intent);
                        }
                    });
                }
            }else {
                audioPlayLayout.setVisibility(View.VISIBLE);
                imageLayout.setVisibility(View.GONE);

                if (!AppUtils.isStringEmpty(audioImages.getFile_url())){
                    audioPlayLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(context, PlayAudioActivity.class);
                            intent.putExtra("audioUrl", audioImages.getFile_url());
                            context.startActivity(intent);
                        }
                    });
                /*holder.audioPlayLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((ReportAudioImageActivity)context).downloadAudio(attachmentAudioImages.getFile_url());
                    }
                });*/
                }
            }

            tvImageAudioDescription.setText(String.valueOf(audioImages.getDescription()));

            audioImage.addView(itemView);
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
                        AppPrefs.setFilterBrand(context, position);
                        AppPrefs.setFilterCampaign(context, 0);
                        AppPrefs.setFilterCountry(context, 0);
                        AppPrefs.setFilterCity(context, 0);
                        AppPrefs.setFilterLocation(context, 0);
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
        } else {
            for (int i = 0; i < locationModel.getCities().size(); i++) {
                if (countryId.equals(String.valueOf(locationModel.getCities().get(i).getCountry_id()))) {
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
            } else {
                for (int i = 0; i < locationModel.getLocations().size(); i++) {
                    if (cityId.equals(String.valueOf(locationModel.getLocations().get(i).getCity_id()))) {
                        locationList.add(locationModel.getLocations().get(i));
                    }
                }
            }
        } else {
            if (cityId.equals("0")) {
                for (int i = 0; i < locationModel.getLocations().size(); i++) {
                    if (countryId.equals(String.valueOf(locationModel.getLocations().get(i).getCountry_id()))) {
                        locationList.add(locationModel.getLocations().get(i));
                    }
                }
            } else {
                for (int i = 0; i < locationModel.getLocations().size(); i++) {
                    if (cityId.equals(String.valueOf(locationModel.getLocations().get(i).getCity_id()))) {
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
        SendToEmailRequest sendToEmailRequest = new SendToEmailRequest(url,
                AppPrefs.getAccessToken(context), stringListener, errorListener);
        VolleyNetworkRequest.getInstance(context).addToRequestQueue(sendToEmailRequest);
    }

    public void downloadPdf(final String url) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(ReportAudioImageActivity1.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_FOR_WRITE_PDF);
        } else {
            DownloadPdfTask downloadTask = new DownloadPdfTask(context, url, ReportAudioImageActivity1.this);
        }
    }

    public void downloadExcel(final String url) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(ReportAudioImageActivity1.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_FOR_WRITE_EXCEL);
        } else {
            DownloadExcelTask downloadTask = new DownloadExcelTask(context, url, ReportAudioImageActivity1.this);
        }
    }

    public void downloadAudio(final String url) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(ReportAudioImageActivity1.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_FOR_WRITE_EXCEL);
        } else {
            DownloadAudioTask downloadTask = new DownloadAudioTask(context, url, ReportAudioImageActivity1.this);
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
        target.setDataAndType(Uri.fromFile(file), "application/pdf");
        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        Intent intent = Intent.createChooser(target, "Open File");
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            // Instruct the user to install a PDF reader here, or something
        }
    }

    @Override
    public void onExcelDownloadFinished(String path) {
        File file = new File(path);
        Intent target = new Intent(Intent.ACTION_VIEW);
        target.setDataAndType(Uri.fromFile(file), "application/vnd.ms-excel");
        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        Intent intent = Intent.createChooser(target, "Open File");
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            // Instruct the user to install a PDF reader here, or something
        }
    }

    @Override
    public void onAudioDownloadFinished(String file) {
        //File filePath = new File(file);
        Intent audioIntent = new Intent(context, AudioStreamingActivity.class);
        audioIntent.putExtra("audioFile", file);
        startActivity(audioIntent);

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

    public void openSeekBarDialog() {
        customDialog = new CustomDialog(context, R.layout.play_audio_layout);
        customDialog.setCancelable(false);
        final SeekBar seekbar = customDialog.findViewById(R.id.seekBar);
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
        seekbar.setProgress((int) startTime);
        myHandler.postDelayed(runnableMethod(seekbar, seekBarTime), 100);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                customDialog.dismiss();
                startTime = 0.0;
                finalTime = 0.0;
                seekbar.setProgress((int) startTime);
            }
        });
        customDialog.show();

    }

    public Runnable runnableMethod(final SeekBar seekBar, final TextView seekBarTime) {
        Runnable UpdateSongTime = new Runnable() {
            public void run() {
                startTime = mediaPlayer.getCurrentPosition();
                seekBarTime.setText(String.format("%d.%d",
                        TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                        toMinutes((long) startTime)))
                );
                seekBar.setProgress((int) startTime);
                myHandler.postDelayed(this, 100);
            }
        };
        return UpdateSongTime;
    }

}
