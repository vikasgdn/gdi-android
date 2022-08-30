package com.gdi.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gdi.api.GetReportRequest;
import com.gdi.hotel.mystery.audits.R;
import com.gdi.activity.BaseActivity;
import com.gdi.activity.mysteryauditreport.ReportOverallBrandActivity;
import com.gdi.adapter.OverallAdapter;
import com.gdi.api.NetworkURL;
import com.gdi.api.VolleyNetworkRequest;
import com.gdi.model.reportoverallbrand.LocationsInfo;
import com.gdi.model.reportoverallbrand.OverallBrandInfo;
import com.gdi.model.reportoverallbrand.OverallBrandRootObject;
import com.gdi.utils.ApiResponseKeys;
import com.gdi.utils.AppLogger;
import com.gdi.utils.AppPrefs;
import com.gdi.utils.AppUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;
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
        cardView = view.findViewById(R.id.overall_card_view);
        hotelList = view.findViewById(R.id.overall_recycler);
        hotelOverallText = view.findViewById(R.id.hotelOverallText);
        excelIcon = view.findViewById(R.id.excel_icon);
        overallList();
        excelIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ReportOverallBrandActivity)context).downloadExcel(overallBrandInfo.getOverall().getReport_urls().getExcel());
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
                            ((ReportOverallBrandActivity)context).overallTab.setVisibility(View.VISIBLE);
                            ((ReportOverallBrandActivity)context).departmentalTab.setVisibility(View.VISIBLE);
                            cardView.setVisibility(View.VISIBLE);
                        }

                    }else if (object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {
                        AppUtils.toast((BaseActivity) context,
                                object.getString(ApiResponseKeys.RES_KEY_MESSAGE));
                        ((ReportOverallBrandActivity)context).overallTab.setVisibility(View.GONE);
                        ((ReportOverallBrandActivity)context).departmentalTab.setVisibility(View.GONE);
                        cardView.setVisibility(View.GONE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ((BaseActivity)context).hideProgressDialog();
            }

        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ((BaseActivity)context).hideProgressDialog();
                AppLogger.e(TAG, "Overall Error: " + error.getMessage());
                AppUtils.toast((BaseActivity) context, "Server temporary unavailable, Please try again");
            }
        };

        AppLogger.e(TAG, "Brand Id: " + ((ReportOverallBrandActivity)context).brandId);
        AppLogger.e(TAG, "Campaign Id: " + ((ReportOverallBrandActivity)context).campaignId);
        AppLogger.e(TAG, "Country Id: " + ((ReportOverallBrandActivity)context).countryId);
        AppLogger.e(TAG, "City Id: " + ((ReportOverallBrandActivity)context).cityId);
        AppLogger.e(TAG, "Location Id: " + ((ReportOverallBrandActivity)context).locationId);
        String overallUrl = NetworkURL.OVERALLBRAND + "?"
                + "brand_id=" + ((ReportOverallBrandActivity)context).brandId + "&"
                + "campaign_id=" + ((ReportOverallBrandActivity)context).campaignId + "&"
                + "location_id=" + ((ReportOverallBrandActivity)context).locationId + "&"
                + "country_id=" + ((ReportOverallBrandActivity)context).countryId + "&"
                + "city_id=" + ((ReportOverallBrandActivity)context).cityId ;

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseAuth.getInstance().getCurrentUser().getIdToken(true)
                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            if (task.isSuccessful()) {
                                GetReportRequest getReportRequest = new GetReportRequest(AppPrefs.getAccessToken(context),task.getResult().getToken(), overallUrl, stringListener, errorListener);
                                VolleyNetworkRequest.getInstance(context).addToRequestQueue(getReportRequest);
                            }
                        }
                    });
        }
    }

    private void setOverallList() {
        locationsInfoArrayList = new ArrayList<>();
        locationsInfoArrayList.clear();
        locationsInfoArrayList.addAll(overallBrandInfo.getOverall().getLocations());
        overallAdapter = new OverallAdapter(context, locationsInfoArrayList);
        hotelList.setLayoutManager(new LinearLayoutManager(context));
        hotelList.setAdapter(overallAdapter);
    }
}
