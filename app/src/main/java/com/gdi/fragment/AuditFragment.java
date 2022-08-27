package com.gdi.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gdi.hotel.mystery.audits.R;
import com.gdi.activity.Audit.AuditFilterActivity;
import com.gdi.activity.BaseActivity;
import com.gdi.activity.MainActivity;
import com.gdi.adapter.AuditTypeAdapter;
import com.gdi.api.ApiEndPoints;
import com.gdi.api.GetReportRequest;
import com.gdi.api.VolleyNetworkRequest;
import com.gdi.model.audit.AuditRootObject;
import com.gdi.model.audit.AuditType;
import com.gdi.model.filter.FilterInfo;
import com.gdi.model.iafilter.Audit;
import com.gdi.utils.ApiResponseKeys;
import com.gdi.utils.AppConstant;
import com.gdi.utils.AppLogger;
import com.gdi.utils.AppPrefs;
import com.gdi.utils.AppUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AuditFragment extends Fragment implements AuditTypeAdapter.CustomItemClickListener {

 /*   @BindView(R.id.pre_opening_layout)
    LinearLayout preOpeningLayout;
    @BindView(R.id.product_layout)
    LinearLayout productLayout;
    @BindView(R.id.self_assessment_layout)
    LinearLayout selfAssessmentLayout;
    @BindView(R.id.heart_house_layout)
    LinearLayout heartHouseLayout;
    private FilterInfo filterInfo;*/
    private Context context;
    public static final String TAG = AuditFragment.class.getSimpleName();

    private List<AuditType> mAuditTypeList;

    private RecyclerView mAUditRecycle;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_standard_report, container, false);
        ButterKnife.bind(this, view);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        setActionBar();

        mAuditTypeList=new ArrayList<AuditType>();
      /*  productLayout = view.findViewById(R.id.product_layout);
        selfAssessmentLayout = view.findViewById(R.id.self_assessment_layout);
        heartHouseLayout = view.findViewById(R.id.heart_house_layout);
        preOpeningLayout = view.findViewById(R.id.pre_opening_layout);*/

        mAUditRecycle=(RecyclerView)view.findViewById(R.id.rv_auditlist);

        GridLayoutManager manager = new GridLayoutManager(getActivity(), 2, RecyclerView.VERTICAL, false);
        mAUditRecycle.setLayoutManager(manager);


        setAuditTypeList();

        //set screen tabs layout
     /*   productLayout.setLayoutParams(new RelativeLayout.LayoutParams(AppConstant.boxSize,AppConstant.boxSize));
        preOpeningLayout.setLayoutParams(new RelativeLayout.LayoutParams(AppConstant.boxSize,AppConstant.boxSize));
        selfAssessmentLayout.setLayoutParams(new RelativeLayout.LayoutParams(AppConstant.boxSize,AppConstant.boxSize));
        heartHouseLayout.setLayoutParams(new RelativeLayout.LayoutParams(AppConstant.boxSize,AppConstant.boxSize));


        selfAssessmentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AuditFilterActivity.class);
                intent.putExtra("type_id", "1");
                intent.putExtra("type", "Self Assessment");
                startActivity(intent);
            }
        });
        heartHouseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AuditFilterActivity.class);
                intent.putExtra("type_id", "2");
                intent.putExtra("type", "Product Audit");  // Heart of House Change to Product Audit VIKAS
                startActivity(intent);
            }
        });
        productLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AuditFilterActivity.class);
                intent.putExtra("type_id", "3");
                intent.putExtra("type", "Inspection");
                startActivity(intent);
            }
        });
        preOpeningLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AuditFilterActivity.class);
                intent.putExtra("type_id", "4");
                intent.putExtra("type", "Pre Opening");
                startActivity(intent);
            }
        });*/
    }



    private void setActionBar() {
        ((BaseActivity)context).setTitle("Internal Audit");
        ActionBar actionBar = ((MainActivity)context).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setHomeButtonEnabled(false);
            actionBar.setDisplayShowHomeEnabled(false);
            MainActivity.mDraweToggle.setDrawerIndicatorEnabled(false);
        }
    }


    private void setAuditTypeList(){
        ((BaseActivity)context).showProgressDialog();
        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AppLogger.e("", "AuditListingResponse: " + response);
                try {
                    JSONObject object = new JSONObject(response);

                    Log.e("AUDIT TYPE==> ",""+response);

                    if (!object.getBoolean(ApiResponseKeys.RES_KEY_ERROR)) {

                        JSONArray jsonArray=object.getJSONArray("data");

                        for(int i=0;i<jsonArray.length();i++)
                        {
                            AuditType type=new AuditType();
                            type.type_id=jsonArray.getJSONObject(i).optInt("type_id");
                            type.name=jsonArray.getJSONObject(i).optString("name");
                            mAuditTypeList.add(type);

                        }

                        AuditTypeAdapter  typeAdapter=new AuditTypeAdapter(getActivity(),mAuditTypeList,AuditFragment.this);
                        mAUditRecycle.setAdapter(typeAdapter);


                    } else
                    {
                        AppUtils.toast((BaseActivity) context, object.getString(ApiResponseKeys.RES_KEY_MESSAGE));
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
                AppLogger.e("", "AuditListingError: " + error.getMessage());
                AppUtils.toast((BaseActivity) context, "Server temporary unavailable, Please try again");

            }
        };


        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseAuth.getInstance().getCurrentUser().getIdToken(true)
                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            if (task.isSuccessful()) {
                                GetReportRequest getReportRequest = new GetReportRequest(AppPrefs.getAccessToken(context),task.getResult().getToken(), ApiEndPoints.AUDIT_TYPE_LIST, stringListener, errorListener);
                                VolleyNetworkRequest.getInstance(context).addToRequestQueue(getReportRequest);
                            }
                        }
                    });
        }
    }


    @Override
    public void onItemClick(AuditType type) {
       // Toast.makeText(getActivity(),"===> "+type.name,Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(context, AuditFilterActivity.class);
        intent.putExtra("type_id", ""+type.type_id);
        intent.putExtra("type", type.name);
        startActivity(intent);
    }
}
