package com.gdi.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gdi.R;
import com.gdi.activity.AuditAnalysisActivity;
import com.gdi.activity.BaseActivity;
import com.gdi.activity.CompCityCompsetActivity;
import com.gdi.activity.CompGlobalActivity;
import com.gdi.activity.IAReportAuditActivity;
import com.gdi.activity.IAReportDashboardActivity;
import com.gdi.activity.MainActivity;
import com.gdi.activity.SignInActivity;
import com.gdi.api.FilterRequest;
import com.gdi.api.VolleyNetworkRequest;
import com.gdi.model.filter.FilterRootObject;
import com.gdi.utils.ApiResponseKeys;
import com.gdi.utils.AppConstant;
import com.gdi.utils.AppLogger;
import com.gdi.utils.AppPrefs;
import com.gdi.utils.AppUtils;
import com.gdi.utils.CustomDialog;
import com.gdi.utils.CustomTypefaceTextView;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.graphics.Color.TRANSPARENT;

public class InternalAuditReportFragment extends Fragment {

    @BindView(R.id.ia_dashboard_layout)
    LinearLayout iaDashboardLayout;
    @BindView(R.id.ia_internal_audit_layout)
    LinearLayout iaInternalAuditLayout;
    @BindView(R.id.ia_detailed_summary_layout)
    LinearLayout iaDetailedSummaryLayout;
    @BindView(R.id.ia_executive_summary_layout)
    LinearLayout iaExecutiveSummaryLayout;
    @BindView(R.id.ia_audio_images_layout)
    LinearLayout iaAudioImagesLayout;
    private Context context;
    private CustomDialog customDialog;
    public static final String TAG = InternalAuditReportFragment.class.getSimpleName();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_internal_audit_report, container, false);
        ButterKnife.bind(this, view);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        setActionBar();
        iaDashboardLayout = (LinearLayout) view.findViewById(R.id.ia_dashboard_layout);
        iaInternalAuditLayout = (LinearLayout) view.findViewById(R.id.ia_internal_audit_layout);
        iaDetailedSummaryLayout = (LinearLayout) view.findViewById(R.id.ia_detailed_summary_layout);
        iaExecutiveSummaryLayout = (LinearLayout) view.findViewById(R.id.ia_executive_summary_layout);
        iaAudioImagesLayout = (LinearLayout) view.findViewById(R.id.ia_audio_images_layout);
        //filterList();//check login
        iaDashboardLayout.setLayoutParams(new RelativeLayout.LayoutParams
                (AppConstant.boxSize,AppConstant.boxSize));
        iaInternalAuditLayout.setLayoutParams(new RelativeLayout.LayoutParams
                (AppConstant.boxSize,AppConstant.boxSize));
        iaDetailedSummaryLayout.setLayoutParams(new RelativeLayout.LayoutParams
                (AppConstant.boxSize,AppConstant.boxSize));
        iaExecutiveSummaryLayout.setLayoutParams(new RelativeLayout.LayoutParams
                (AppConstant.boxSize,AppConstant.boxSize));
        iaAudioImagesLayout.setLayoutParams(new RelativeLayout.LayoutParams
                (AppConstant.boxSize,AppConstant.boxSize));

        iaDashboardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, IAReportDashboardActivity.class));
            }
        });

        iaInternalAuditLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, IAReportAuditActivity.class));
            }
        });

        iaDetailedSummaryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, AuditAnalysisActivity.class));
            }
        });

        iaExecutiveSummaryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, AuditAnalysisActivity.class));
            }
        });

        iaAudioImagesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, AuditAnalysisActivity.class));
            }
        });
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
                        }

                    } else if (object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        /*AppUtils.toast((BaseActivity) context,
                                object.getString(ApiResponseKeys.RES_KEY_MESSAGE));*/
                        if (object.getInt(ApiResponseKeys.RES_KEY_CODE) == AppConstant.ERROR){
                            AppUtils.toast((BaseActivity) context,
                                    object.getString(ApiResponseKeys.RES_KEY_MESSAGE));
                            ((MainActivity)context).finish();
                            startActivity(new Intent(context, SignInActivity.class));
                        }
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

    private void openDCRDialog(){
        customDialog = new CustomDialog(context, R.layout.competetion_benchmarking_dailog);
        customDialog.setCancelable(true);
        customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(TRANSPARENT));
        CustomTypefaceTextView tv_subTile = (CustomTypefaceTextView) customDialog.findViewById(R.id.tv_subTile_footfall);
        tv_subTile.setText("Competetion Benchmarking");
        LinearLayout LL_newRegistration = (LinearLayout) customDialog.findViewById(R.id.LL_newRegistration);
        LinearLayout LL_dailySchedule = (LinearLayout) customDialog.findViewById(R.id.LL_dailySchedule);
        LL_newRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, CompCityCompsetActivity.class));
            }
        });
        LL_dailySchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, CompGlobalActivity.class));
            }
        });
        customDialog.show();

    }

    private void setActionBar() {
        ((BaseActivity)context).setTitle("GDI WORLDWIDE");
        ActionBar actionBar = ((MainActivity)context).getSupportActionBar();
        if (actionBar != null) {
            // actionBar.setDisplayShowCustomEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setHomeButtonEnabled(false);
            actionBar.setDisplayShowHomeEnabled(false);
            MainActivity.mDraweToggle.setDrawerIndicatorEnabled(true);
        }
    }
}
