package com.gdi.activity.Audit;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Button;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gdi.R;
import com.gdi.activity.BaseActivity;
import com.gdi.adapter.SubSectionTabAdapter;
import com.gdi.api.ApiEndPoints;
import com.gdi.api.GetReportRequest;
import com.gdi.api.VolleyNetworkRequest;
import com.gdi.model.audit.BrandStandard.BrandStandardInfo;
import com.gdi.model.audit.BrandStandard.BrandStandardRootObject;
import com.gdi.model.audit.BrandStandard.BrandStandardSection;
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

public class SubSectionsActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_sub_section_tab)
    RecyclerView subSectionTabList;
    @BindView(R.id.continue_btn)
    Button continueBtn;
    private String auditId = "";
    Context context;
    private static final String TAG = SubSectionsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_sections);
        context = this;
        ButterKnife.bind(SubSectionsActivity.this);
        initView();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setActionBar();
        subSectionTabList = (RecyclerView) findViewById(R.id.rv_sub_section_tab);
        continueBtn = (Button) findViewById(R.id.continue_btn);
        auditId = getIntent().getStringExtra("auditId");
        setBrandStandardQuestion();
    }

    private void setBrandStandardQuestion(){
        showProgressDialog();
        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AppLogger.e(TAG, "BrandStandardResponse: " + response);
                try {
                    JSONObject object = new JSONObject(response);

                    if (!object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        BrandStandardRootObject brandStandardRootObject = new GsonBuilder().create()
                                .fromJson(object.toString(), BrandStandardRootObject.class);
                        if (brandStandardRootObject.getData() != null &&
                                brandStandardRootObject.getData().toString().length() > 0) {
                            setQuestionList(brandStandardRootObject.getData());
                            //brandStandardAuditAdapter.notifyDataSetChanged();
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
                AppLogger.e(TAG, "AudioImageError: " + error.getMessage());
                AppUtils.toast((BaseActivity) context, "Server temporary unavailable, Please try again");

            }
        };

        String integrityUrl = ApiEndPoints.BRANDSTANDARD + "?"
                + "audit_id=" + auditId ;
        GetReportRequest getReportRequest = new GetReportRequest(AppPrefs.getAccessToken(context),
                integrityUrl, stringListener, errorListener);
        VolleyNetworkRequest.getInstance(context).addToRequestQueue(getReportRequest);
    }

    private void setQuestionList(BrandStandardInfo info){
        ArrayList<BrandStandardSection> brandStandardSections = new ArrayList<>();
        brandStandardSections.addAll(info.getSections());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context,3
                , LinearLayoutManager.VERTICAL,false);
        SubSectionTabAdapter subSectionTabAdapter = new SubSectionTabAdapter(context, brandStandardSections);
        subSectionTabList.setLayoutManager(gridLayoutManager);
        subSectionTabList.setAdapter(subSectionTabAdapter);
    }

    private void submitBrandStandardQuestion(){
        showProgressDialog();
        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AppLogger.e(TAG, "AudioImageResponse: " + response);
                try {
                    JSONObject object = new JSONObject(response);

                    if (!object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {

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
                AppLogger.e(TAG, "AudioImageError: " + error.getMessage());
                AppUtils.toast((BaseActivity) context, "Server temporary unavailable, Please try again");

            }
        };

        String integrityUrl = ApiEndPoints.BRANDSTANDARD + "?"
                + "audit_id=" + auditId ;
        GetReportRequest getReportRequest = new GetReportRequest(AppPrefs.getAccessToken(context),
                integrityUrl, stringListener, errorListener);
        VolleyNetworkRequest.getInstance(context).addToRequestQueue(getReportRequest);
    }

    private void setActionBar() {
        initToolbar(toolbar);
        setTitle("Audit Option");
        enableBack(true);
        enableBackPressed();
    }
}
