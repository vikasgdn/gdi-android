package com.gdi.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gdi.R;
import com.gdi.activity.BaseActivity;
import com.gdi.activity.OverallBrandActivity;
import com.gdi.activity.SignInActivity;
import com.gdi.adapter.OverallAdapter;
import com.gdi.api.ApiEndPoints;
import com.gdi.api.OverallBrandRequest;
import com.gdi.api.VolleyNetworkRequest;
import com.gdi.model.SampleModel;
import com.gdi.model.overallbrand.LocationsInfo;
import com.gdi.model.overallbrand.OverallBrandInfo;
import com.gdi.model.overallbrand.OverallBrandRootObject;
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

public class OverallFragment extends Fragment {

    @BindView(R.id.overall_card_view)
    CardView cardView;
    @BindView(R.id.overall_recycler)
    RecyclerView hotelList;
    @BindView(R.id.hotelOverallText)
    TextView hotelOverallText;
    @BindView(R.id.excel_icon)
    ImageView excelIcon;
    private ArrayList<LocationsInfo> locationsInfoArrayList;
    private OverallBrandInfo overallBrandInfo;
    private OverallAdapter overallAdapter;
    private Context context;
    private static final String TAG = OverallFragment.class.getSimpleName();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_overall, container, false);
        ButterKnife.bind(this, view);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        cardView = (CardView) view.findViewById(R.id.overall_card_view);
        hotelList = (RecyclerView) view.findViewById(R.id.overall_recycler);
        hotelOverallText = (TextView) view.findViewById(R.id.hotelOverallText);
        excelIcon = (ImageView) view.findViewById(R.id.excel_icon);
        overallList();
        //TODO : Static data testing
        //setAuditDeparmentOffline();
        excelIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((OverallBrandActivity)context).downloadExcel(overallBrandInfo.getOverall().getReport_urls().getExcel());
            }
        });
    }

    public void overallList(){
        ((BaseActivity)context).showProgressDialog();
        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AppLogger.e(TAG, "Overall Response: " + response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (!object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)){
                        OverallBrandRootObject overallBrandRootObject = new GsonBuilder().create()
                                .fromJson(object.toString(), OverallBrandRootObject.class);
                        if (overallBrandRootObject.getData() != null &&
                                overallBrandRootObject.getData().toString().length() > 0){
                            overallBrandInfo = overallBrandRootObject.getData();
                            setOverallList();
                            ((OverallBrandActivity)context).overallTab.setVisibility(View.VISIBLE);
                            ((OverallBrandActivity)context).departmentalTab.setVisibility(View.VISIBLE);
                            cardView.setVisibility(View.VISIBLE);
                            excelIcon.setVisibility(View.VISIBLE);
                        }

                    }else if (object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        if (object.getInt(ApiResponseKeys.RES_KEY_CODE) == AppConstant.ERROR){
                            AppUtils.toast((BaseActivity) context,
                                    object.getString(ApiResponseKeys.RES_KEY_MESSAGE));
                            ((OverallBrandActivity)context).finish();
                            startActivity(new Intent(context, SignInActivity.class));
                        }else {
                            AppUtils.toast((BaseActivity) context,
                                    object.getString(ApiResponseKeys.RES_KEY_MESSAGE));
                            cardView.setVisibility(View.GONE);
                            excelIcon.setVisibility(View.GONE);
                        }
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
                AppLogger.e(TAG, "Overall Error: " + error.getMessage());

            }
        };

        AppLogger.e(TAG, "Brand Id: " + ((OverallBrandActivity)context).brandId);
        AppLogger.e(TAG, "Campaign Id: " + ((OverallBrandActivity)context).campaignId);
        AppLogger.e(TAG, "Country Id: " + ((OverallBrandActivity)context).countryId);
        AppLogger.e(TAG, "City Id: " + ((OverallBrandActivity)context).cityId);
        AppLogger.e(TAG, "Location Id: " + ((OverallBrandActivity)context).locationId);
        String auditUrl = ApiEndPoints.OVERALLBRAND + "?"
                + "brand_id=" + ((OverallBrandActivity)context).brandId + "&"
                + "campaign_id=" + ((OverallBrandActivity)context).campaignId + "&"
                + "location_id=" + ((OverallBrandActivity)context).locationId + "&"
                + "country_id=" + ((OverallBrandActivity)context).countryId + "&"
                + "city_id=" + ((OverallBrandActivity)context).cityId ;

        OverallBrandRequest overallBrandRequest = new
                OverallBrandRequest(AppPrefs.getAccessToken(context),
                auditUrl, stringListener, errorListener);
        VolleyNetworkRequest.getInstance(context).addToRequestQueue(overallBrandRequest);
    }

    private void setOverallList() {
        locationsInfoArrayList = new ArrayList<>();
        locationsInfoArrayList.clear();
        locationsInfoArrayList.addAll(overallBrandInfo.getOverall().getLocations());
        overallAdapter = new OverallAdapter(context, locationsInfoArrayList);
        hotelList.setLayoutManager(new LinearLayoutManager(context));
        hotelList.setAdapter(overallAdapter);
    }

    private void setAuditDeparmentOffline() {
        ((OverallBrandActivity)context).overallTab.setVisibility(View.VISIBLE);
        ((OverallBrandActivity)context).departmentalTab.setVisibility(View.VISIBLE);
        cardView.setVisibility(View.VISIBLE);
        excelIcon.setVisibility(View.VISIBLE);
        ArrayList<SampleModel> sampleModels = SampleModel.createList(5);
        //overallAdapter = new OverallAdapter(context, sampleModels);
        hotelList.setLayoutManager(new LinearLayoutManager(context));
        hotelList.setAdapter(overallAdapter);
    }
}
