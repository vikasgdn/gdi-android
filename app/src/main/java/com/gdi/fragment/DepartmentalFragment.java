package com.gdi.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gdi.R;
import com.gdi.activity.BaseActivity;
import com.gdi.activity.ReportOverallBrandActivity;
import com.gdi.adapter.DepartmentalAdapter1;
import com.gdi.api.ApiEndPoints;
import com.gdi.api.GetReportRequest;
import com.gdi.api.VolleyNetworkRequest;
import com.gdi.model.SampleModel;
import com.gdi.model.overallbrand.DepartmentOverallInfo;
import com.gdi.model.overallbrand.OverallBrandInfo;
import com.gdi.model.overallbrand.OverallBrandRootObject;
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

public class DepartmentalFragment extends Fragment {

    @BindView(R.id.departmental_recycler)
    RecyclerView list;
    private OverallBrandInfo overallBrandInfo;
    private ArrayList<DepartmentOverallInfo> departmentOverallInfoArrayList;
    private DepartmentalAdapter1 departmentalAdapter1;
    private Context context;
    private static final String TAG = ScoreCardFragment.class.getSimpleName();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_departmental, container, false);
        ButterKnife.bind(this, view);

        initViews(view);
        return view;
    }

    private void initViews(View view) {
        list = (RecyclerView) view.findViewById(R.id.departmental_recycler);
        departmentList();
        //TODO : Static data testing
        //setAuditDeparmentOffline();
    }

    public void departmentList(){
        ((BaseActivity)context).showProgressDialog();
        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AppLogger.e(TAG, "Audit Response: " + response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (!object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)){
                        OverallBrandRootObject overallBrandRootObject = new GsonBuilder().create()
                                .fromJson(object.toString(), OverallBrandRootObject.class);
                        if (overallBrandRootObject.getData() != null &&
                                overallBrandRootObject.getData().toString().length() > 0){
                            overallBrandInfo = overallBrandRootObject.getData();
                            ((ReportOverallBrandActivity)context).overallTab.setVisibility(View.VISIBLE);
                            ((ReportOverallBrandActivity)context).departmentalTab.setVisibility(View.VISIBLE);
                            setDepartmentalList();
                        }

                    }else if (object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        /*if (object.getInt(ApiResponseKeys.RES_KEY_CODE) == AppConstant.ERROR){
                            AppUtils.toast((BaseActivity) context,
                                    object.getString(ApiResponseKeys.RES_KEY_MESSAGE));
                            ((ReportOverallBrandActivity)context).finish();
                            startActivity(new Intent(context, SignInActivity.class));
                        }else {
                            AppUtils.toast((BaseActivity) context,
                                    object.getString(ApiResponseKeys.RES_KEY_MESSAGE));
                            list.setVisibility(View.GONE);
                        }*/
                        AppUtils.toast((BaseActivity) context,
                                object.getString(ApiResponseKeys.RES_KEY_MESSAGE));
                        ((ReportOverallBrandActivity)context).overallTab.setVisibility(View.GONE);
                        ((ReportOverallBrandActivity)context).departmentalTab.setVisibility(View.GONE);
                        list.setVisibility(View.GONE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }/*catch (Exception e){
                    e.printStackTrace();
                    AppUtils.toast((BaseActivity) context,
                            "No result found!");
                }*/
                ((BaseActivity)context).hideProgressDialog();
            }

        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ((BaseActivity)context).hideProgressDialog();
                AppLogger.e(TAG, "DepartmentalError: " + error.getMessage());
                AppUtils.toast((BaseActivity) context, "Server temporary unavailable, Please try again");
            }
        };

        AppLogger.e(TAG, "Brand Id: " + ((ReportOverallBrandActivity)context).brandId);
        AppLogger.e(TAG, "Campaign Id: " + ((ReportOverallBrandActivity)context).campaignId);
        AppLogger.e(TAG, "Country Id: " + ((ReportOverallBrandActivity)context).countryId);
        AppLogger.e(TAG, "City Id: " + ((ReportOverallBrandActivity)context).cityId);
        AppLogger.e(TAG, "Location Id: " + ((ReportOverallBrandActivity)context).locationId);
        String departmentalUrl = ApiEndPoints.OVERALLBRAND + "?"
                + "brand_id=" + ((ReportOverallBrandActivity)context).brandId + "&"
                + "campaign_id=" + ((ReportOverallBrandActivity)context).campaignId + "&"
                + "location_id=" + ((ReportOverallBrandActivity)context).locationId + "&"
                + "country_id=" + ((ReportOverallBrandActivity)context).countryId + "&"
                + "city_id=" + ((ReportOverallBrandActivity)context).cityId ;

        GetReportRequest getReportRequest = new GetReportRequest(AppPrefs.getAccessToken(context),
                departmentalUrl, stringListener, errorListener);
        VolleyNetworkRequest.getInstance(context).addToRequestQueue(getReportRequest);
    }

    private void setDepartmentalList() {
        departmentOverallInfoArrayList = new ArrayList<>();
        departmentOverallInfoArrayList.clear();
        departmentOverallInfoArrayList.addAll(overallBrandInfo.getDepartment_overall());
        departmentalAdapter1 = new DepartmentalAdapter1(context, departmentOverallInfoArrayList);
        list.setLayoutManager(new LinearLayoutManager(context));
        list.setAdapter(departmentalAdapter1);
    }

    private void setAuditDeparmentOffline() {
        ArrayList<SampleModel> sampleModels = SampleModel.createList(5);
        //departmentalAdapter1 = new DepartmentalAdapter1(context, sampleModels);
        list.setLayoutManager(new LinearLayoutManager(context));
        list.setAdapter(departmentalAdapter1);
    }
}
