package com.gdi.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gdi.R;
import com.gdi.activity.ReportAudioImageActivity;
import com.gdi.activity.ReportAuditActivity;
import com.gdi.activity.BaseActivity;
import com.gdi.activity.ReportBackHouseActivity;
import com.gdi.activity.ReportDetailSummaryActivity;
import com.gdi.activity.ReportExecutiveSummaryActivity;
import com.gdi.activity.ReportHighlightActivity;
import com.gdi.activity.MainActivity;
import com.gdi.activity.OverallBrandActivity;
import com.gdi.activity.ReportIntegrityActivity;
import com.gdi.activity.SignInActivity;
import com.gdi.api.FilterRequest;
import com.gdi.api.VolleyNetworkRequest;
import com.gdi.model.filter.FilterInfo;
import com.gdi.model.filter.FilterRootObject;
import com.gdi.utils.ApiResponseKeys;
import com.gdi.utils.AppConstant;
import com.gdi.utils.AppLogger;
import com.gdi.utils.AppPrefs;
import com.gdi.utils.AppUtils;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReportFragment extends Fragment implements View.OnClickListener {

    /*@BindView(R.id.dashboard_layout)
    LinearLayout dashboardLayout;*/
    @BindView(R.id.audit_layout)
    LinearLayout auditLayout;
    @BindView(R.id.overall_brand_layout)
    LinearLayout overallBrandLayout;
    @BindView(R.id.detailed_summary_layout)
    LinearLayout detailedSummaryLayout;
    @BindView(R.id.executive_summary_layout)
    LinearLayout executiveSummaryLayout;
    @BindView(R.id.highlights_layout)
    LinearLayout highlightsLayout;
    @BindView(R.id.audio_image_layout)
    LinearLayout audioImageLayout;
    @BindView(R.id.back_house_layout)
    LinearLayout backHouseLayout;
    @BindView(R.id.integrity_layout)
    LinearLayout integrityLayout;
    private FilterInfo filterInfo;
    private Context context;
    public static final String TAG = ReportFragment.class.getSimpleName();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reports, container, false);
        ButterKnife.bind(this, view);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        setActionBar();
        //dashboardLayout = (LinearLayout) view.findViewById(R.id.dashboard_layout);
        auditLayout = (LinearLayout) view.findViewById(R.id.audit_layout);
        overallBrandLayout = (LinearLayout) view.findViewById(R.id.overall_brand_layout);
        detailedSummaryLayout = (LinearLayout) view.findViewById(R.id.detailed_summary_layout);
        executiveSummaryLayout = (LinearLayout) view.findViewById(R.id.executive_summary_layout);
        highlightsLayout = (LinearLayout) view.findViewById(R.id.highlights_layout);
        audioImageLayout = (LinearLayout) view.findViewById(R.id.audio_image_layout);
        backHouseLayout = (LinearLayout) view.findViewById(R.id.back_house_layout);
        integrityLayout = (LinearLayout) view.findViewById(R.id.integrity_layout);

        //filterList();//filters api
        //set screen tabs layout
        auditLayout.setLayoutParams(new RelativeLayout.LayoutParams
                (AppConstant.boxSize,AppConstant.boxSize));
        overallBrandLayout.setLayoutParams(new RelativeLayout.LayoutParams
                (AppConstant.boxSize,AppConstant.boxSize));
        detailedSummaryLayout.setLayoutParams(new RelativeLayout.LayoutParams
                (AppConstant.boxSize,AppConstant.boxSize));
        executiveSummaryLayout.setLayoutParams(new RelativeLayout.LayoutParams
                (AppConstant.boxSize,AppConstant.boxSize));
        highlightsLayout.setLayoutParams(new RelativeLayout.LayoutParams
                (AppConstant.boxSize,AppConstant.boxSize));
        audioImageLayout.setLayoutParams(new RelativeLayout.LayoutParams
                (AppConstant.boxSize,AppConstant.boxSize));
        backHouseLayout.setLayoutParams(new RelativeLayout.LayoutParams
                (AppConstant.boxSize,AppConstant.boxSize));
        integrityLayout.setLayoutParams(new RelativeLayout.LayoutParams
                (AppConstant.boxSize,AppConstant.boxSize));


        auditLayout.setOnClickListener(this);
        overallBrandLayout.setOnClickListener(this);
        detailedSummaryLayout.setOnClickListener(this);
        executiveSummaryLayout.setOnClickListener(this);
        highlightsLayout.setOnClickListener(this);
        audioImageLayout.setOnClickListener(this);
        backHouseLayout.setOnClickListener(this);
        integrityLayout.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        switch (viewId) {
            case R.id.audit_layout:
                startActivity(new Intent(context, ReportAuditActivity.class));
                break;
            case R.id.overall_brand_layout:
                startActivity(new Intent(context, OverallBrandActivity.class));
                break;
            case R.id.detailed_summary_layout:
                startActivity(new Intent(context, ReportDetailSummaryActivity.class));
                break;
            case R.id.executive_summary_layout:
                startActivity(new Intent(context, ReportExecutiveSummaryActivity.class));
                break;
            case R.id.highlights_layout:
                startActivity(new Intent(context, ReportHighlightActivity.class));
                break;
            case R.id.audio_image_layout:
                startActivity(new Intent(context, ReportAudioImageActivity.class));
                break;
            case R.id.back_house_layout:
                startActivity(new Intent(context, ReportBackHouseActivity.class));
                break;
            case R.id.integrity_layout:
                startActivity(new Intent(context, ReportIntegrityActivity.class));
                break;
        }
    }

    public void filterList() {
        ((MainActivity)context).showProgressDialog();
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
                            //setFilter(filterInfo);
                        }

                    } else if (object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        /*AppUtils.toast((BaseActivity) context,
                                object.getString(ApiResponseKeys.RES_KEY_MESSAGE));*/
                        AppUtils.toast((BaseActivity) context,
                                object.getString(ApiResponseKeys.RES_KEY_MESSAGE));
                        ((MainActivity)context).finish();
                        startActivity(new Intent(context, SignInActivity.class));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ((MainActivity)context).hideProgressDialog();
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ((MainActivity)context).hideProgressDialog();
                AppLogger.e(TAG, "Filter Error: " + error.getMessage());

            }
        };
        FilterRequest auditRequest = new FilterRequest(AppPrefs.getAccessToken(context),
                stringListener, errorListener);
        VolleyNetworkRequest.getInstance(context).addToRequestQueue(auditRequest);
    }

    private void setActionBar() {
        ((BaseActivity)context).setTitle("Report");
        ActionBar actionBar = ((MainActivity)context).getSupportActionBar();
        if (actionBar != null) {
            // actionBar.setDisplayShowCustomEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setHomeButtonEnabled(false);
            actionBar.setDisplayShowHomeEnabled(false);
            MainActivity.mDraweToggle.setDrawerIndicatorEnabled(false);
        }
    }

}
